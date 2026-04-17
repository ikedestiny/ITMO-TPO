package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MainPage extends BasePage {

    private By fromCityInput = By.xpath("//input[contains(@class, 'input') and contains(@placeholder, 'Откуда')] | //input[@name='from']");
    private By toCityInput = By.xpath("//input[contains(@class, 'input') and contains(@placeholder, 'Куда')] | //input[@name='to']");
    private By dateInput = By.xpath("//input[contains(@class, 'input') and contains(@placeholder, 'Дата')] | //input[@name='date']");
    private By searchButton = By.xpath("//button[contains(text(), 'Найти')] | //button[@type='submit']");
    private By firstSuggestion = By.xpath("(//li[contains(@class, 'suggestion')])[1] | (//div[contains(@class, 'autocomplete-item')])[1]");
    
    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("https://www.tutu.ru");
        // Ждём загрузки хотя бы одного поля
        waitForVisibility(fromCityInput);
    }

    public void searchTrain(String from, String to, String date) {
        WebElement fromField = waitForVisibility(fromCityInput);
        fromField.clear();
        fromField.sendKeys(from);
        waitForVisibility(firstSuggestion).click();

        WebElement toField = waitForVisibility(toCityInput);
        toField.clear();
        toField.sendKeys(to);
        waitForVisibility(firstSuggestion).click();

        WebElement dateField = waitForVisibility(dateInput);
        dateField.clear();
        dateField.sendKeys(date);
        dateField.sendKeys(Keys.ENTER);

        waitForClickable(searchButton).click();
        wait.until(ExpectedConditions.urlContains("search"));
    }
}