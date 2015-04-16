package org.zeroturnaround.jf.hw.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class RemotePluginLoader extends ClassLoader {
  private final URL imageUrl;

  public RemotePluginLoader(String imageUrl) {
    try {
      this.imageUrl = new URL(imageUrl);
    } catch (MalformedURLException e) {
      throw new RuntimeException("Invalid URL for the image - " + imageUrl, e);
    }
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    Class clazz;
    try {
      clazz = getParent().loadClass(name);
      return clazz;
    } catch (ClassNotFoundException e) {
    }
    byte[] imageBytes = getImageBytes(imageUrl);
    int offset = findClassBytesStartIndexInBytes(imageBytes);
    if (offset != -1)
      return defineClass(name, imageBytes, offset, imageBytes.length - offset);
    throw new ClassNotFoundException();
  }

  private byte[] getImageBytes(URL imageUrl) {
    try (InputStream in = imageUrl.openStream();
         ByteArrayOutputStream byteStream = new ByteArrayOutputStream();) {
      byte[] buffer = new byte[4096];
      int n;
      while ((n = in.read(buffer)) != -1)
        byteStream.write(buffer, 0, n);
      return byteStream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException("There was an error while reading bytes from the image " + imageUrl.getPath(), e);
    }
  }

  private int findClassBytesStartIndexInBytes(byte[] bytes) {
    for (int i = bytes.length - 4; i >= 0; i--)
      if (isStartOfClassFile(bytes, i))
        return i;
    return -1;
  }

  private boolean isStartOfClassFile(byte[] bytes, int index) {
    return bytes[index]     == (byte) 0xCA &&
           bytes[index + 1] == (byte) 0xFE &&
           bytes[index + 2] == (byte) 0xBA &&
           bytes[index + 3] == (byte) 0xBE;
  }
}