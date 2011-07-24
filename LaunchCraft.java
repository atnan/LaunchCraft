import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;

import netscape.javascript.*;
import java.applet.Applet;

public class LaunchCraft extends Applet {
  private JSObject jsObject;

  public void init() {
      System.setSecurityManager(null);
      jsObject = JSObject.getWindow(this);
  }

  public void start() {
      Object[] args = { this };
      jsObject.call("LaunchCraft_onAppletLoaded", args);
  }

  public String jsIsMinecraftDownloaded() {
    return isMinecraftDownloaded() ? "true" : "false";
  }

  public void jsDownloadMinecraft() {
    downloadMinecraft();
  }

  public String jsLaunchMinecraft(String username, String password, String host) {
    return launchMinecraft(username, password, host) ? "true" : "false";
  }

  private Boolean isMinecraftDownloaded() {
    File minecraft = new File(minecraftPath());
    return minecraft.isFile();
  }

  private void downloadMinecraft() {
    (new Thread(new Runnable() {
        public void run() {
          Boolean success = blockingDownloadMinecraft();
          Object[] args = { null };

          if (success) {
            jsObject.call("LaunchCraft_onAppletDownloadMinecraftSuccess", args);

          } else {
            jsObject.call("LaunchCraft_onAppletDownloadMinecraftFail", args);
          }
        }
    })).start();
  }

  private Boolean launchMinecraft(String username, String password, String host) {
    if (!isMinecraftDownloaded()) {
      return false;
    }

    try {
      String command = "java -Xmx1024M -Xms512M -cp " + minecraftPath() + " net.minecraft.LauncherFrame " + username + " " + password + " " + host;
      Runtime.getRuntime().exec(command);

    } catch (IOException e) {
      return false;
    }
    return true;
  }

  private Boolean blockingDownloadMinecraft() {
    URL minecraftURL = null;

    BufferedInputStream inputStream = null;
    BufferedOutputStream outputStream = null;

    try {
      minecraftURL = new URL("https://s3.amazonaws.com/MinecraftDownload/launcher/minecraft.jar");

      int bufferSize = 8 * 1024;
      inputStream = new BufferedInputStream(minecraftURL.openConnection().getInputStream(), bufferSize);

      File outDir = new File(launchCraftBasePath());
      if (!outDir.isDirectory()) {
        outDir.mkdirs();
      }

      File minecraft = new File(minecraftPath());
      FileOutputStream fileOutputStream = new FileOutputStream(minecraft);
      outputStream = new BufferedOutputStream(fileOutputStream, bufferSize);

      int bytesRead = -1;
      byte[] buffer = new byte[bufferSize];
      while ((bytesRead = inputStream.read(buffer, 0, bufferSize)) >= 0) {
          outputStream.write(buffer, 0, bytesRead);
      }
      outputStream.flush();

    } catch (MalformedURLException e) {
      return false;

    } catch (IOException e) {
      return false;

    } catch (SecurityException e) {
      return false;

    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) { }
      }

      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) { }
      }
    }
    return true;
  }

  private String minecraftPath() {
    return launchCraftBasePath() + "minecraft.jar";
  }

  private String launchCraftBasePath() {
    String homeDirectory = System.getProperty("user.home");
    String fileSeparator = System.getProperty("file.separator");

    return homeDirectory + fileSeparator + ".launchcraft" + fileSeparator;
  }

}
