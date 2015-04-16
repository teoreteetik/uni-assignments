package org.zeroturnaround.jf.hw.remote;

import org.zeroturnaround.jf.hw.Plugin;

import java.util.HashSet;
import java.util.Set;

public class RemotePluginManager {

  public static Set<RemotePluginInfo> findAllPlugins() {
    Set<RemotePluginInfo> result = new HashSet<>();
    result.add(new RemotePluginInfo("https://raw.github.com/zeroturnaround/jf-hw-classloaders/master/plugins-remote/NomNomNomPlugin/README.properties"));
    result.add(new RemotePluginInfo("https://raw.github.com/zeroturnaround/jf-hw-classloaders/master/plugins-remote/ChickenPlugin/README.properties"));
    result.add(new RemotePluginInfo("https://raw.github.com/zeroturnaround/jf-hw-classloaders/master/plugins-remote/HeadAndShouldersPlugin/README.properties"));
    return result;
  }

  public static Plugin getPluginInstance(RemotePluginInfo info) {

    ClassLoader cl = new RemotePluginLoader(info.getImageUrl());
    Class clazz = null;

    try {
      clazz = cl.loadClass(info.getClassName());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    try {
      return (Plugin) clazz.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}