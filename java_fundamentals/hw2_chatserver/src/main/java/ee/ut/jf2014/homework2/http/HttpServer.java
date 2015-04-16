package ee.ut.jf2014.homework2.http;

import ee.ut.jf2014.homework2.Cb;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class HttpServer {

  public HttpServer(int port, Cb<String> msgCb) throws Exception {
    Server server = new Server(port);
    server.setHandler(new AbstractHandler() {
      @Override
      public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String msg = request.getReader().readLine();
        String author = request.getHeader("author");
        msgCb.call(author + ": " + msg);
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
      }
    });
    server.start();
    System.out.println("Listening for POST messages on localhost:" + port);
    server.join();
  }
}