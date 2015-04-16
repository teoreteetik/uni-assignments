package ee.ut.jf2014.homework2;

import ee.ut.jf2014.homework2.http.HttpServer;
import ee.ut.jf2014.homework2.tcp.TcpServer;

public class ChatServer {

  private static final int TCP_PORT = 8888;
  private static final int HTTP_PORT = 8080;

  public ChatServer() throws Exception {
    TcpServer tcpServer = new TcpServer(TCP_PORT);
    new HttpServer(HTTP_PORT, new Cb<String>() {
      @Override
      public void call(String msg) {
        tcpServer.forwardMsgToClients(msg);
      }
    });
  }
}