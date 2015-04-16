package ee.ut.jf2014.homework7;

import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MirrorBlurrer {

  private final static int TRESHOLD = 10000;

  private final int imgWidth;
  private final int imgHeight;
  private final int[] src;

  public MirrorBlurrer(BufferedImage original) {
    this.imgWidth = original.getWidth();
    this.imgHeight = original.getHeight();
    this.src = original.getRGB(0, 0, imgWidth, imgHeight, null, 0, imgWidth);
  }

  public BufferedImage blur() {
    BufferedImage mirrorBlurred = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
    ForkJoinPool pool = new ForkJoinPool();
    Action action = new Action(0, src.length);
    pool.invoke(action);
    mirrorBlurred.setRGB(0, 0, imgWidth, imgHeight, src, 0, imgWidth);
    return mirrorBlurred;
  }

  private class Action extends RecursiveAction {
    private final int start;
    private final int length;

    public Action(int start, int length) {
      this.start = start;
      this.length = length;
    }

    @Override
    protected void compute() {
      if (length < TRESHOLD) {
        computeDirectly();
        return;
      }
      int split = length / 2;
      invokeAll(new Action(start, split), 
                new Action(start + split, length - split));
    }

    private void computeDirectly() {
      for (int index = start; index < start + length; index++) {
        if (isPixelInFirstHalf(index))
          continue;
        src[index] = avg(src[index], 
                         src[index - 2 * getOffsetFromCenterline(index)]);
      }
    }

    private int avg(int... pixels) {
      float rt = 0, gt = 0, bt = 0;
      for (int pixel : pixels) {
        rt += (float) ((pixel & 0x00ff0000) >> 16) / 2;
        gt += (float) ((pixel & 0x0000ff00) >> 8) / 2;
        bt += (float) ((pixel & 0x000000ff) >> 0) / 2;
      }
      return (0xff000000) | (((int) rt) << 16) | (((int) gt) << 8) | (((int) bt) << 0);
    }

    private boolean isPixelInFirstHalf(int index) {
      return (index % imgWidth) <= (imgWidth / 2);
    }

    private int getOffsetFromCenterline(int index) {
      return (index % imgWidth) - (imgWidth / 2);
    }
  }
}