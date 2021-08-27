package com.identity.e2e.utils;

public interface IConstants {

  // driver
  String D_DRIVER_LOC = Utils.getProperty("driver.app.location");
  boolean D_CHROME = Utils.getBooleanProperty("driver.chrome");
  boolean D_PHANTOM = Utils.getBooleanProperty("driver.phantomjs");

  // application constants
}
