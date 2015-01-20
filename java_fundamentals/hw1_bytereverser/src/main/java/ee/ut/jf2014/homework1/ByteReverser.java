package ee.ut.jf2014.homework1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.commons.lang.ArrayUtils;

public class ByteReverser {
  public static final int BUFFER_SIZE = 8192;
  private final File file;
  private final long length;
  private final long midPoint;
  private final long sizeInKb;

  public ByteReverser(String fileName) throws IOException {
    file = getFile(fileName);
    length = file.length();
    midPoint = length / 2;
    sizeInKb = length / 1024;
  }

  public void reverseBytes() throws IOException {
    final long startTime = System.currentTimeMillis();

    System.out.println("File name: " + file.getName() + ", size: " + sizeInKb + "kB");
    byte[] startBuffer;
    byte[] endBuffer;
    try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
      for (int startOffset = 0; startOffset < midPoint; startOffset += BUFFER_SIZE) {
        long bytesLeftToRead = midPoint - startOffset;
        int bytesToRead = (int) (bytesLeftToRead > BUFFER_SIZE ? BUFFER_SIZE : bytesLeftToRead);
        long endOffset = length - startOffset - bytesToRead;

        startBuffer = new byte[bytesToRead];
        endBuffer = new byte[bytesToRead];

        raf.read(startBuffer);
        ArrayUtils.reverse(startBuffer);

        raf.seek(endOffset);
        raf.read(endBuffer);
        ArrayUtils.reverse(endBuffer);

        raf.seek(endOffset);
        raf.write(startBuffer);

        raf.seek(startOffset);
        raf.write(endBuffer);
      }
    }
    double seconds = (System.currentTimeMillis() - startTime) / 1000.0;
    System.out.println("Reversing bytes took " + seconds + "s, speed: " + sizeInKb / seconds + "kB/s");
  }

  private File getFile(String fileName) throws IOException {
    File file = new File(fileName);
    if (!file.isFile()) {
      throw new FileNotFoundException("Cannot find file " + file.getCanonicalPath());
    }
    return file;
  }
}