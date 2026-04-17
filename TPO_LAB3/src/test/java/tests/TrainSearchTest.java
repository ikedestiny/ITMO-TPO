package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;

import java.time.Duration;
import java.util.List;

public class TrainSearchTest extends BaseTest {

    @Test
    public void testTrainSearchFromMoscowToSpb() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.searchTrain("Москва", "Санкт-Петербург", "20.12.2026");

        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("tutu.ru"),
                "URL не соответствует ожидаемому после поиска");
    }

    // Вспомогательный тест для отладки загрузки страницы
    @Test
    public void testWaitForPageLoad() {
        driver.get("https://www.tutu.ru");
        // Ждём появления body
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        System.out.println("Страница загружена. Ищем поле ввода...");

        // Находим все input и выводим их placeholder'ы
        List<WebElement> inputs = driver.findElements(By.xpath("//input"));
        System.out.println("Найдено input элементов: " + inputs.size());
        inputs.forEach(input -> System.out.println("placeholder: " + input.getAttribute("placeholder")));
    }
}