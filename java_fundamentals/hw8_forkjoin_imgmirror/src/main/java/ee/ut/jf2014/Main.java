package ee.ut.jf2014;

import ee.ut.jf2014.homework7.ImageFrame;
import ee.ut.jf2014.homework7.MirrorBlurrer;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {
  public static void main(String[] args) throws Exception {
    String fileName = args[0];
    BufferedImage original = ImageIO.read(new File(fileName));

    new ImageFrame("Original", original);

    BufferedImage mirrorBlurred = new MirrorBlurrer(original).blur();
    new ImageFrame("MirrorBlurred", mirrorBlurred);
  }
}