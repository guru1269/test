package com.identity.e2e.web;

import com.identity.e2e.utils.IConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WebParent implements IConstants {

  public WebDriver driver = null;

  public void initialiseWebDriver() {
    if (D_CHROME) {
      initialiseChromeWebDriver();
      return;
    }
    initialiseHtmlWebDriver();
  }

  public synchronized WebDriver getDriver() {
    return driver;
  }

  public void initialiseChromeWebDriver() {

    System.setProperty("webdriver.chrome.driver", D_DRIVER_LOC + "/chromedriver.exe");

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--start-maximized");
    options.addArguments("--start-minimized");
    options.addArguments("--disable-web-security");
    options.addArguments("--no-proxy-server");

    System.setProperty("webdriver.chrome.args", "--disable-logging");
    System.setProperty("webdriver.chrome.silentOutput", "true");

    Map<String, Object> prefs = new HashMap<String, Object>();
    prefs.put("credentials_enable_service", false);
    prefs.put("profile.password_manager_enabled", false);

    driver = new ChromeDriver(options);

    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
  }

  public WebDriver initialiseHtmlWebDriver() {
    driver = new HtmlUnitDriver();
    driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    return driver;
  }

  public void destroy() {
    if (driver != null) {
      driver.close();
      driver.quit();
    }
  }

  public void typeById(String elementId, String value) {
    driver.findElement(By.id(elementId)).clear();
    driver.findElement(By.id(elementId)).sendKeys(value);
  }

  public void clickBypath(String xpath) {
    WebElement element = driver.findElement(By.xpath(xpath));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
  }

  public WebElement getElement(By by) {
    WebDriverWait wait = new WebDriverWait(driver, 10);
    return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
  }

  public String getTextByPath(String path) {
    return driver.findElement(By.xpath(path)).getText().trim();
  }
}
