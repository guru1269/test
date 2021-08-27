package com.identity.e2e.utils;

import java.util.Properties;

public class Utils implements IConstants {

  public static boolean getBooleanProperty(String key) {
    String value = System.getProperty(key);
    if (value != null && value.trim().length() > 0) {
      return value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("true");
    }
    return false;
  }

  public static String getProperty(String key) {
    String value = System.getProperty(key);
    if (value != null && value.trim().length() > 0) {
      return value;
    }
    return "";
  }

  public static void loadProperties(String filename) {
    Properties appProperties = new Properties(System.getProperties());
    Properties newProperties = new Properties();

    try {
      newProperties.load(Utils.class.getClassLoader().getResourceAsStream(filename));
      for (String key : newProperties.stringPropertyNames()) {
        String value = newProperties.getProperty(key);
        appProperties.setProperty(key.trim(), value.trim());
      }
      System.setProperties(appProperties);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static boolean isNullOrEmpty(String... input) {
    if (input != null) {
      for (String str : input) {
        if (str == null || str.trim().length() == 0) {
          return true;
        }
      }
    }
    return false;
  }
}
