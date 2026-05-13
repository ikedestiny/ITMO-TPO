package se.ifmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import se.ifmo.utils.BrowserUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class BasePage {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(BrowserUtils browserUtils) {
        this(browserUtils.getDriver());
    }

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    protected WebElement visible(String xpath) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        } catch (TimeoutException exception) {
            failWithPageDiagnostics("Element is not visible by XPath: " + xpath, exception);
            throw exception;
        }
    }

    protected WebElement clickable(String xpath) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        } catch (TimeoutException exception) {
            failWithPageDiagnostics("Element is not clickable by XPath: " + xpath, exception);
            throw exception;
        }
    }

    protected List<WebElement> presentElements(String xpath) {
        return driver.findElements(By.xpath(xpath));
    }

    protected void click(String xpath) {
        clickable(xpath).click();
    }

    protected void clickIfPresent(String xpath) {
        List<WebElement> elements = presentElements(xpath);
        if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
            elements.get(0).click();
        }
    }

    protected void fill(String xpath, String text) {
        WebElement field = visible(xpath);
        field.click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(Keys.BACK_SPACE);
        field.sendKeys(text);
    }

    protected void fillFirstVisible(String text, String... xpaths) {
        WebElement field = firstVisible(xpaths);
        field.click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(Keys.BACK_SPACE);
        field.sendKeys(text);
    }

    protected void clickFirstVisible(String... xpaths) {
        firstVisible(xpaths).click();
    }

    protected WebElement firstVisible(String... xpaths) {
        RuntimeException lastException = null;
        for (String xpath : xpaths) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            } catch (RuntimeException exception) {
                lastException = exception;
            }
        }
        failWithPageDiagnostics("No visible element matched XPath candidates: " + String.join(" | ", xpaths), lastException);
        throw lastException == null ? new TimeoutException("No XPath candidates were provided") : lastException;
    }

    protected void fillAndChooseFirstSuggestion(String text, String... xpaths) {
        fillFirstVisible(text, xpaths);
        String suggestion = "(//*[self::li or self::div or self::button]"
                + "[contains(@class,'suggest') or contains(@class,'autocomplete') or @role='option' or contains(@data-ti,'suggest')]"
                + "[not(contains(@style,'display: none'))])[1]";
        clickIfPresent(suggestion);
    }

    protected void acceptCookiesAndClosePopups() {
        clickIfPresent("//button[contains(.,'Принять') or contains(.,'Согласен') or contains(.,'Понятно')]");
        clickIfPresent("//button[contains(.,'Соглашаюсь') or contains(.,'Хорошо')]");
        clickIfPresent("//*[self::button or self::a][@aria-label='Закрыть' or contains(.,'Закрыть')]");
    }

    protected void ensureTutuIsNotBlocked() {
        String bodyText = driver.findElement(By.tagName("body")).getText();
        if (bodyText.contains("Слишком много пересадок") || bodyText.toLowerCase().contains("выключите vpn")) {
            dumpPageDiagnostics("tutu-blocked");
            throw new IllegalStateException("tutu.ru returned its anti-bot page: \"Слишком много пересадок\". "
                    + "The expected form is not present in DOM; disable VPN/proxy or run from an allowed network.");
        }
    }

    protected boolean pageContainsText(String text) {
        try {
            visible("//*[contains(normalize-space(.),'" + text + "')]");
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
    }

    protected boolean elementExists(String xpath) {
        return !presentElements(xpath).isEmpty();
    }

    protected void moveMouseAway() {
        new Actions(driver).moveByOffset(5, 5).perform();
    }

    private void failWithPageDiagnostics(String message, RuntimeException cause) {
        dumpPageDiagnostics("selenium-timeout");
        ensureTutuIsNotBlocked();
        if (cause == null) {
            throw new TimeoutException(message);
        }
        throw new TimeoutException(message, cause);
    }

    private void dumpPageDiagnostics(String prefix) {
        try {
            Path debugDir = Path.of("target", "selenium-debug");
            Files.createDirectories(debugDir);
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS"));
            Files.writeString(debugDir.resolve(prefix + "-" + stamp + ".html"), driver.getPageSource());
            if (driver instanceof TakesScreenshot screenshotDriver) {
                byte[] screenshot = screenshotDriver.getScreenshotAs(OutputType.BYTES);
                Files.write(debugDir.resolve(prefix + "-" + stamp + ".png"), screenshot);
            }
        } catch (IOException ignored) {
            // Diagnostics should not hide the original Selenium failure.
        }
    }
}
