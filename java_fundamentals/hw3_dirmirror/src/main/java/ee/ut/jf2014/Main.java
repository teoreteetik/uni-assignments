package ee.ut.jf2014;

import ee.ut.jf2014.homework2.DirectoryMirror;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
        System.out.println("Usage: java -jar jarname.jar <source dir> <target dir>");
        return;
    }
    Path source = Paths.get(args[0]);
    Path target = Paths.get(args[1]);

    if (!Files.isDirectory(source)) {
      throw new IllegalArgumentException(source.toAbsolutePath() + " is not a directory!");
    }

    DirectoryMirror mirror = new DirectoryMirror(source, target);
    mirror.watchEvents();
  }
}
