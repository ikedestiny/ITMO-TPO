import com.microsoft.playwright.Page;
import org.junit.jupiter.api.*;
import se.ifmo.pages.MainPage;
import se.ifmo.utils.BrowserUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest {
    private static BrowserUtils browserUtils;
    private MainPage mainPage;

    @BeforeAll
    static void setupClass() {
        browserUtils = new BrowserUtils();
        browserUtils.setupDriver();
    }

    @BeforeEach
    void setUp() {
        mainPage = new MainPage(browserUtils);
    }

    @Test
    void testTrainSearch() {
        mainPage.searchTrain("Москва", "Санкт-Петербург", LocalDate.now().plusDays(7));
        assertTrue(browserUtils.getPage().url().contains("train"));
    }

    @Test
    void testPlaneSearch() {
        mainPage.searchPlane("Москва", "Сочи", LocalDate.now().plusDays(14));
        assertTrue(browserUtils.getPage().url().contains("avia"));
    }

    @Test
    void testBusSearch() {
        mainPage.searchBus("Москва", "Тверь", LocalDate.now().plusDays(2));
        assertTrue(browserUtils.getPage().url().contains("bus"));
    }

    @Test
    void testHotelSearch() {
        mainPage.searchHotel("Москва", LocalDate.now().plusDays(5), LocalDate.now().plusDays(7), 2);
        assertTrue(browserUtils.getPage().url().contains("hotel"));
    }

    @AfterAll
    static void teardown() {
        if (browserUtils != null) browserUtils.quitDriver();
    }
}