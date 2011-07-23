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
  
  public String launchMinecraft(String username, String password, String host) {
    if (isMinecraftDownloaded() == "false" && downloadMinecraft() == "false") {
      return "false";
    }
    
    try {
      String command = "java -Xmx1024M -Xms512M -cp " + minecraftPath() + " net.minecraft.LauncherFrame " + username + " " + password + " " + host;      
      Runtime.getRuntime().exec(command);
      
    } catch (IOException e) {
      jsLog(e);
      return "false";
    }
    return "true";
  }
  
  public String downloadMinecraft() {
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
      jsLog(e);
      return "false";
      
    } catch (IOException e) {
      jsLog(e);
      return "false";
      
    } catch (SecurityException e) {
      jsLog(e);
      return "false";
      
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          jsLog(e);
        }
      }
      
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          jsLog(e);
        }
      }
    }
    return "true";
  }
  
  public String isMinecraftDownloaded() {
    File minecraft = new File(minecraftPath());
    return minecraft.isFile() ? "true" : "false";
  }
  
  private void jsLog(Object o) {
    Object[] args = { o };
    jsObject.call("LaunchCraft_log", args);
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
