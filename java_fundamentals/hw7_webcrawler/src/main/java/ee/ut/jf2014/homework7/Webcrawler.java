package ee.ut.jf2014.homework7;

import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRules;
import crawlercommons.robots.SimpleRobotRules.RobotRulesMode;
import crawlercommons.robots.SimpleRobotRulesParser;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Webcrawler {

  private final ConcurrentMap<String, AtomicInteger> countByUrl;
  private final String allowedDomain;
  private final BaseRobotRules robotRules;
  private final String rootUrl;
  private CountDownLatch latch;
  private AtomicInteger uniqueUrlsCrawled;
  private final int maxCount;

  public Webcrawler(String rootUrl, int maxCount) throws IOException {
    this.rootUrl = rootUrl;
    this.maxCount = maxCount;
    countByUrl = new ConcurrentHashMap<>();
    robotRules = getRobotRules(rootUrl);
    allowedDomain = getDomain(rootUrl);
    uniqueUrlsCrawled = new AtomicInteger(1);
  }

  public Map<String, Number> crawl() {
    List<CrawlWorker> workers = new ArrayList<>();
    for (String linkUrl : getCrawlableUrls(rootUrl)) {
      countByUrl.put(linkUrl, new AtomicInteger(1));
      workers.add(new CrawlWorker(linkUrl));
    }
    latch = new CountDownLatch(workers.size());
    for (CrawlWorker worker : workers) {
      System.out.println("Starting new crawler thread with " + worker.startUrl);
      new Thread(worker).start();
    }
    try {
      latch.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return new HashMap<>(countByUrl);
  }

  private List<String> getCrawlableUrls(String rootUrl) {
    List<String> result = new ArrayList<>();
    try {
      Document doc = Jsoup.connect(rootUrl).timeout(0).get();
      Elements links = doc.select("a[href]");
      for (Element link : links) {
        String linkUrl = stripAnchors(link.attr("abs:href"));
        String linkDomain = getDomain(linkUrl);
        if (robotRules.isAllowed(linkUrl) && allowedDomain.equals(linkDomain))
          result.add(linkUrl);
      }
    } catch (UnsupportedMimeTypeException | HttpStatusException e) {
      // don't try to crawl non-text pages and 404s
    } catch (Exception e) {
      System.err.println("Error while getting crawlable urls from " + rootUrl + ": " + e.getMessage());
    }
    return result;
  }

  private String getDomain(String url) {
    int index = url.indexOf("://");
    if (index != -1)
      url = url.substring(index + 3);
    index = url.indexOf('/');
    if (index != -1)
      url = url.substring(0, index);
    url = url.replaceFirst("^www.*?\\.", "");
    return url;
  }

  private String stripAnchors(String url) {
    int i = url.indexOf('#');
    if (i != -1)
      return url.substring(0, i);
    return url;
  }
  
  private BaseRobotRules getRobotRules(String url) throws IOException {
    URL urlObj = new URL(url);
    String hostId = urlObj.getProtocol() + "://" + urlObj.getHost() + (urlObj.getPort() > -1 ? ":" + urlObj.getPort() : "");
    BaseRobotRules rules;
    HttpGet httpget = new HttpGet(hostId + "/robots.txt");
    BasicHttpContext context = new BasicHttpContext();
    HttpClient httpClient = new DefaultHttpClient();
    HttpResponse response = httpClient.execute(httpget, context);
    if (response.getStatusLine() != null && response.getStatusLine().getStatusCode() == 404) {
      rules = new SimpleRobotRules(RobotRulesMode.ALLOW_ALL);
      EntityUtils.consumeQuietly(response.getEntity());
    } else {
      BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
      SimpleRobotRulesParser robotParser = new SimpleRobotRulesParser();
      rules = robotParser.parseContent(hostId, IOUtils.toByteArray(entity.getContent()), "text/plain", "mycrawler");
    }
    return rules;
  }

  private class CrawlWorker implements Runnable {
    private final String startUrl;

    public CrawlWorker(String startUrl) {
      this.startUrl = startUrl;
    }

    private void crawl(String url) {
      System.out.println(Thread.currentThread().getName() + " crawling " + url);
      for (String linkUrl : getCrawlableUrls(url)) {
        AtomicInteger count = new AtomicInteger(1);
        AtomicInteger prevCount = countByUrl.putIfAbsent(linkUrl, count);
        if (prevCount != null) {
          prevCount.incrementAndGet();
        } else if (uniqueUrlsCrawled.incrementAndGet() <= maxCount) {
          crawl(linkUrl);
        }
      }
    }

    @Override
    public void run() {
      try {
        crawl(startUrl);
      } finally {
        latch.countDown();
      }
    }
  }
}