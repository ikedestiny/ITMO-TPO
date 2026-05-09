package se.ifmo.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Locale;

public final class BrowserFactory {
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(45);
    private static final Duration IMPLICIT_TIMEOUT = Duration.ofSeconds(2);

    private BrowserFactory() {
    }

    public static WebDriver createDriver(String browserName) {
        String normalizedName = browserName == null ? "chrome" : browserName.toLowerCase(Locale.ROOT).trim();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        WebDriver driver = switch (normalizedName) {
            case "firefox" -> createFirefoxDriver(headless);
            case "edge", "msedge" -> createEdgeDriver(headless);
            case "chrome" -> createChromeDriver(headless);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        };

        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);
        driver.manage().timeouts().implicitlyWait(IMPLICIT_TIMEOUT);
        driver.manage().window().setSize(new Dimension(1366, 900));
        return driver;
    }

    private static WebDriver createChromeDriver(boolean headless) {
        setupDriverManagerIfEnabled("chrome");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--lang=ru-RU");
        String binary = System.getProperty("chrome.binary");
        if (binary != null && !binary.isBlank()) {
            options.setBinary(binary);
        }
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1366,900");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        setupDriverManagerIfEnabled("edge");
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--lang=ru-RU");
        String binary = System.getProperty("edge.binary");
        if (binary != null && !binary.isBlank()) {
            options.setBinary(binary);
        }
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1366,900");
        }
        return new EdgeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        setupDriverManagerIfEnabled("firefox");
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("intl.accept_languages", "ru-RU,ru");
        String binary = System.getProperty("firefox.binary");
        if (binary != null && !binary.isBlank()) {
            options.setBinary(binary);
        }
        if (headless) {
            options.addArguments("-headless");
            options.addArguments("--width=1366");
            options.addArguments("--height=900");
        }
        return new FirefoxDriver(options);
    }

    private static void setupDriverManagerIfEnabled(String browserName) {
        boolean useWebDriverManager = Boolean.parseBoolean(System.getProperty("webdriver.manager", "true"));
        if (!useWebDriverManager) {
            return;
        }

        switch (browserName) {
            case "chrome" -> WebDriverManager.chromedriver().setup();
            case "edge" -> WebDriverManager.edgedriver().setup();
            case "firefox" -> WebDriverManager.firefoxdriver().setup();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
    }
}
