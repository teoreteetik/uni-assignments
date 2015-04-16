package ee.ut.jf2014.homework7;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageFrame extends JFrame {

  public ImageFrame(String title, BufferedImage image) {
    super(title);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(image.getWidth(), image.getHeight());
    add(new ImagePanel(image));
    setVisible(true);
  }

  private static class ImagePanel extends JPanel {
    BufferedImage mImage;

    public ImagePanel(BufferedImage image) {
      mImage = image;
    }

    protected void paintComponent(Graphics g) {
      int x = (getWidth() - mImage.getWidth()) / 2;
      int y = (getHeight() - mImage.getHeight()) / 2;
      g.drawImage(mImage, x, y, this);
    }
  }
}