package ee.ut.jf2014.homework2.tcp;

import ee.ut.jf2014.homework2.Cb;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TcpServer {
  private final ServerSocket server;
  private final Set<TcpWorker> workers = new HashSet<>();

  public TcpServer(int port) throws IOException {
    server = new ServerSocket(port);
    System.out.println("Listening for incoming TCP connections on port " + port);
    new Thread(new WorkerManager()).start();
  }

  private class WorkerManager implements Runnable {
    @Override
    public void run() {
      while (true) {
        try {
          TcpWorker worker = new TcpWorker(server.accept(), new Cb<String>() {
            @Override
            public void call(String msg) {
              forwardMsgToClients(msg);
            }
          });
          System.out.println("New TCP connection, author=" + worker.getAuthor());
          workers.add(worker);
          forwardMsgToClients(worker.getAuthor() + " joined the chat");
          new Thread(worker).start();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public synchronized void forwardMsgToClients(String msg) {
    Iterator<TcpWorker> iterator = workers.iterator();
    while (iterator.hasNext()) {
      TcpWorker serverTCPWorker = iterator.next();
      if (serverTCPWorker.isConnected()) {
        System.out.println("Sending '" + msg + "' to " + serverTCPWorker.getAuthor());
        serverTCPWorker.sendMsgToClient(msg);
      } else {
        iterator.remove();
      }
    }
  }

}
