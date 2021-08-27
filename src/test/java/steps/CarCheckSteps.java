package steps;

import com.identity.e2e.utils.Utils;
import com.identity.e2e.web.WebParent;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;

public class CarCheckSteps {

  static final Logger log = Logger.getLogger(CarCheckSteps.class);

  WebParent web;
  List<String> carRegistrationNumbersFromInput = new ArrayList<>();

  Map<String, List<String>> actualCarDetails = new HashMap<>();
  Map<String, List<String>> expectedCarDetails = new HashMap<>();

  @After
  public void cleanup() {
    web.destroy();
  }

  @Given("user launches the car check website")
  public void launchTheWebsite() {
    log.info("launchTheWebsite");
    web = new WebParent();
    Utils.loadProperties("application.properties");
    web.initialiseWebDriver();
  }

  @And("read car registration numbers from input file")
  public void readCarRegistrationsFromInput() throws IOException, URISyntaxException {
    Pattern carRegexPattern = Pattern.compile("[A-Z]{2}[0-9]{2}\\s*[A-Z]{3}");

    Path path =
        Paths.get(
            Objects.requireNonNull(getClass().getClassLoader().getResource("input/givenInput.txt"))
                .toURI());

    for (String line : Files.readAllLines(path)) {
      Matcher matcher = carRegexPattern.matcher(line);
      while (matcher.find()) {
        carRegistrationNumbersFromInput.add(matcher.group());
      }
    }
  }

  @And("retrieved car details should match to output file")
  public void compareResults() throws IOException, URISyntaxException {

    Path path =
        Paths.get(
            Objects.requireNonNull(
                    getClass().getClassLoader().getResource("output/expectedOutput.csv"))
                .toURI());

    int firstRecord = 0;
    for (String line : Files.readAllLines(path)) {
      if (++firstRecord == 1) continue;

      List<String> carDetails = Arrays.asList(line.split(","));
      expectedCarDetails.put(carDetails.get(0), carDetails);
    }

    // assertions
    boolean assertionFailure = false;

    for (String eachCar : expectedCarDetails.keySet()) {

      List<String> actualCar = actualCarDetails.get(eachCar);
      List<String> expectedCar = expectedCarDetails.get(eachCar);

      if (actualCar == null) {
        log.error("car " + eachCar + " not found");
        assertionFailure = true;
        continue;
      }

      if (!CollectionUtils.isEqualCollection(actualCar, expectedCar)) {
        log.error("car " + eachCar + " expected " + expectedCar + " but was " + actualCar);
        assertionFailure = true;
      }
    }

    assertFalse(assertionFailure);
  }

  @When("perform the free car check")
  public void performTheCarCheck() throws InterruptedException {

    for (String eachCarNumber : carRegistrationNumbersFromInput) {
      web.driver.get("https://cartaxcheck.co.uk/");
      web.typeById("vrm-input", eachCarNumber);
      web.clickBypath("//*[@id=\"m\"]/div[2]/div/div/div/div/form/button");

      String registrationNum =
          web.getTextByPath("//*[@id=\"m\"]/div[2]/div[5]/div[1]/div/span/div[2]/dl[1]/dd");

      String make =
          web.getTextByPath("//*[@id=\"m\"]/div[2]/div[5]/div[1]/div/span/div[2]/dl[2]/dd");

      String model =
          web.getTextByPath("//*[@id=\"m\"]/div[2]/div[5]/div[1]/div/span/div[2]/dl[3]/dd");

      String color =
          web.getTextByPath("//*[@id=\"m\"]/div[2]/div[5]/div[1]/div/span/div[2]/dl[4]/dd");

      String year =
          web.getTextByPath("//*[@id=\"m\"]/div[2]/div[5]/div[1]/div/span/div[2]/dl[5]/dd");

      registrationNum =
          web.getTextByPath("//*[@id=\"m\"]/div[2]/div[5]/div[1]/div/span/div[2]/dl[1]/dd");

      actualCarDetails.put(
          eachCarNumber.replaceAll(" ", ""),
          Arrays.asList(registrationNum, make, model, color, year));
    }
  }
}
