package org.zeroturnaround.jf.hw.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class RemotePluginInfo {
  private final Properties properties;
  private final String imageUrl;

  public RemotePluginInfo(String propertiesUrl) {
    URL infoUrl;
    try {
      infoUrl = new URL(propertiesUrl);
    } catch (MalformedURLException e) {
      throw new RuntimeException("Invalid URL for the properties - " + propertiesUrl, e);
    }

    try (BufferedReader in = new BufferedReader(new InputStreamReader(infoUrl.openStream()))) {
      properties = new Properties();
      properties.load(in);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    imageUrl = propertiesUrl.substring(0, propertiesUrl.lastIndexOf('/')) + '/' + getName() + ".png";
  }

  public String getName() {
    return properties.getProperty("name");
  }

  public String getClassName() {
    return properties.getProperty("class");
  }

  public String getImageUrl() {
    return imageUrl;
  }
}