package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverSetup {

    public static WebDriver createDriver() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();

        return switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                yield new FirefoxDriver(firefoxOptions);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                yield new EdgeDriver(edgeOptions);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                yield new ChromeDriver(chromeOptions);
            }
        };
    }
}