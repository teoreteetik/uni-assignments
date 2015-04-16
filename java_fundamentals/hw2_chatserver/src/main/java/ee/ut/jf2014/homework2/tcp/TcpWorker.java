package ee.ut.jf2014.homework2.tcp;

import ee.ut.jf2014.homework2.Cb;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class TcpWorker implements Runnable {
  private final BufferedReader in;
  private final BufferedWriter out;
  private final String author;
  private final Cb<String> msgCb;
  private final Queue<String> msgsToForwardToClient = new LinkedList<>();
  private boolean connected;

  public TcpWorker(Socket socket, Cb<String> msgCb) throws IOException {
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    this.msgCb = msgCb;
    author = in.readLine();
    connected = true;
  }

  public boolean isConnected() {
    return connected;
  }

  private void forwardMessagesToClient() throws IOException {
    while (!msgsToForwardToClient.isEmpty()) {
      String msg = msgsToForwardToClient.poll() + '\n';
      out.write(msg.toCharArray());
      out.flush();
    }
  }

  @Override
  public void run() {
    try {
      while (true) {
        forwardMessagesToClient();
      }
    } catch (IOException e) {
      try {
        in.close();
        out.close();
      } catch (IOException e2) {
        e2.printStackTrace();
      }
      connected = false;
      msgCb.call(author + " disconnected");
    }
  }

  public void sendMsgToClient(String msg) {
    msgsToForwardToClient.add(msg);
  }

  public String getAuthor() {
    return author;
  }
}