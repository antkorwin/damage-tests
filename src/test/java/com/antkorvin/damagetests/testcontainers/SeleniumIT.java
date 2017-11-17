package com.antkorvin.damagetests.testcontainers;

import com.antkorvin.damagetests.utils.BaseSystemIT;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

/**
 * Created by Korovin A. on 17.11.2017.
 *
 * Test search in browser by Selenium driver run in docker testcontainer
 *
 * @author Korovin Anatoliy
 * @version 1.0
 */
public class SeleniumIT extends BaseSystemIT {

    @Rule
    public BrowserWebDriverContainer chrome =
            new BrowserWebDriverContainer()
                    .withDesiredCapabilities(DesiredCapabilities.chrome());

    @Test
    public void testSearchInGoogle() throws Exception {
        // Arrange
        RemoteWebDriver driver = chrome.getWebDriver();

        // Act
        driver.get("https://www.google.ru/");
        driver.findElement(By.name("q")).sendKeys("Хабаровск");
        driver.findElement(By.name("q")).submit();

        // Assert
        Assertions.assertThat(driver.findElement(By.linkText("Портал администрации города Хабаровска")))
                  .isNotNull();
    }
}
