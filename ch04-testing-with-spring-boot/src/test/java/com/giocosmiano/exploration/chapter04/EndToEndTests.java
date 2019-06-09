package com.giocosmiano.exploration.chapter04;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;
import static org.openqa.selenium.chrome.ChromeDriverService.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTests {

    /*
     @RunWith(SpringRunner.class) ensures the Spring Boot annotations integrate with JUnit

     @SpringBootTest is the test annotation where we can activate all of Spring Boot in a controlled
     fashion. With webEnvironment switched from the default setting of a mocked web environment to
     SpringBootTest.WebEnvironment.RANDOM_PORT, a real embedded version of the app will launch on a
     random available port

     This configuration will spin up a copy of our application on an open port, with a full-blown
     auto-configuration, and all of our CommandLineRunners will run. That means our InitDatabase
     class that pre-loads MongoDB will kick in

     ChromeDriverService gives us a handle on the bridge between Selenium and the Chrome handling library

     ChromeDriver is an implementation of the WebDriver interface, giving us all the operations to drive

     @LocalServerPort is a Spring Boot annotation that instructs Boot to autowire the port number of
     the web container into port a test browser

     To use ChromeDriver, not only do we need the browser Chrome downloaded and installed in
     its default location, we also need a separate executable: chromedriver.
     Visit https://sites.google.com/a/chromium.org/chromedriver/downloads, download the bundle
     unzipped it, and put the executable in a folder named ext
     */
    static ChromeDriverService service;
    static ChromeDriver driver;

    @LocalServerPort
    int port;

    /*
     @BeforeClass directs JUnit to run this method before any test method inside this class
     runs and to only run this method once

     setUp method sets the webdriver.chrome.driver property to the relative path of chromedriver

     We create a default service then we create a new ChromeDriver to be used by all the test methods

     We create a test directory to capture screenshots
     */
    @BeforeClass
    public static void setUp() throws IOException {
        System.setProperty("webdriver.chrome.driver",
                "ext/chromedriver");
        service = createDefaultService();
        driver = new ChromeDriver(service);
        Path testResults = Paths.get("build", "test-results");
        if (!Files.exists(testResults)) {
            Files.createDirectory(testResults);
        }
    }

    /*
     @AfterClass directs JUnit to run the tearDown method after ALL tests have run in this class

     It then commands ChromeDriverService to shut down. Otherwise, the server process will stay up and
     running
     */
    @AfterClass
    public static void tearDown() {
        service.stop();
    }

    /*
     driver navigates to the home page using the injected port

     It takes a screenshot so we can inspect things after the fact

     We verify the title of the page is as expected

     We grab the entire page's HTML content and verify one of the links

     We hunt down that link using a W3C CSS selector (there are other options as well), move to
     it, and click on it

     We grab another snapshot and then click on the back button
     */
    @Test
    public void homePageShouldWork() throws IOException {
        driver.get("http://localhost:" + port);
        takeScreenshot("homePageShouldWork-1");
        assertThat(driver.getTitle())
                .isEqualTo("Learning Spring Boot: Spring-a-Gram");
        String pageContent = driver.getPageSource();
        assertThat(pageContent)
                .contains("<a href=\"/images/bazinga.png/raw\">");
        WebElement element = driver.findElement(By.cssSelector("a[href*=\"bazinga.png\"]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
        takeScreenshot("homePageShouldWork-2");
        driver.navigate().back();
    }

    /*
     driver.getScreenshotAs(OutputType.FILE) taps the TakesScreenshot sub-interface to grab
     a snapshot of the screen and put it into a temp file

     Spring's FileCopyUtils utility method is used to copy that temp file into the project's
     build/test-results folder using the input argument to give it a custom name

     Taking screenshots is a key reason to use either ChromeDriver, FirefoxDriver, or SafariDriver.
     All of these real-world browser integrations support this feature
     */
    private void takeScreenshot(String name) throws IOException {
        FileCopyUtils.copy(
                driver.getScreenshotAs(OutputType.FILE),
                new File("build/test-results/TEST-" + name + ".png"));
    }
}
