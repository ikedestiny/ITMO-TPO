package se.ifmo.utils;

import org.openqa.selenium.WebDriver;

public class BrowserUtils {
    private static final String BASE_URL = "https://www.tutu.ru/";

    private WebDriver driver;

    public void setupDriver(String browserName) {
        driver = BrowserFactory.createDriver(browserName);
    }

    public void openBaseUrl() {
        driver.get(BASE_URL);
    }

    public void open(String path) {
        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
        driver.get(BASE_URL + normalizedPath);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
