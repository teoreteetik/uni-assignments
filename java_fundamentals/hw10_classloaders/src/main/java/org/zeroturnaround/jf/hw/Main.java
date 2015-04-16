package org.zeroturnaround.jf.hw;

import org.zeroturnaround.jf.hw.local.LocalPluginManager;
import org.zeroturnaround.jf.hw.remote.RemotePluginInfo;
import org.zeroturnaround.jf.hw.remote.RemotePluginManager;

import java.util.Set;

/**
 * Iterates through all plugins and prints them out. First
 * iterates through local plugins and then through remote plugins.
 */
public class Main {

  public static void main(String[] args) {
    System.out.println("Find all plugins from local plugin folder");
    System.out.println();

    String[] allPluginNames = LocalPluginManager.findAllPlugins();
    for (int i = 0; i < allPluginNames.length; i++) {
      System.out.print(allPluginNames[i] + "\t- ");
      Plugin plugin = LocalPluginManager.getPluginInstance(allPluginNames[i]);
      System.out.println(plugin);
    }

    System.out.println();
    System.out.println("Find all remote plugins from the interwebs");
    System.out.println();

    Set<RemotePluginInfo> plugins = RemotePluginManager.findAllPlugins();
    for (RemotePluginInfo rpi : plugins) {
      System.out.print(rpi.getName() + "\t- ");
      Plugin plugin = RemotePluginManager.getPluginInstance(rpi);
      System.out.println(plugin);
    }
  }
}
