package ee.ut.jf2014;

import java.io.BufferedOutputStream;
import java.io.PrintStream;

public class Main {
  public static void main(String[] args) {
    int n = Integer.valueOf(args[0]);
    System.out.println("Please wait...");
    PrintStream out = new PrintStream(new BufferedOutputStream(System.out));
    new Homework(n, out).run();
    out.flush();
  }
}