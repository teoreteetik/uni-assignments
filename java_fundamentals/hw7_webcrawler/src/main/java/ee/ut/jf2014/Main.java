package ee.ut.jf2014;

import ee.ut.jf2014.homework7.Webcrawler;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Main {

  public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

    String url = args[0];
    if (!url.contains("://"))
      throw new RuntimeException("URL must contain protocol");
    int maxCount = args.length == 2 ? Integer.valueOf(args[1]) : 100;
    
    Webcrawler crawler = new Webcrawler(url, maxCount);
    Map<String, Number> countByUrl = crawler.crawl();

    TreeMap<String, Number> sorted = new TreeMap<>(new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return new Integer(countByUrl.get(o2).intValue()).compareTo(new Integer(countByUrl.get(o1).intValue()));
      }
    });
    sorted.putAll(countByUrl);
    System.out.println("\nResults:");
    for (Entry<String, Number> entry : sorted.entrySet()) {
      System.out.println(entry.getValue() + ": " + entry.getKey());
    }
  }
}