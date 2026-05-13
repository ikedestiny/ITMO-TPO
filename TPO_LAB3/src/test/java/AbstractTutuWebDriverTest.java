import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import se.ifmo.pages.MainPage;
import se.ifmo.pages.MainPage.TestPassenger;
import se.ifmo.utils.BrowserUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Stream;

abstract class AbstractTutuWebDriverTest {
    protected static final String DEFAULT_FROM = "Москва";
    protected static final String DEFAULT_TO = "Санкт-Петербург";

    protected BrowserUtils browserUtils;
    protected MainPage mainPage;

    static Stream<String> browsers() {
        return Arrays.stream(System.getProperty("browsers", "edge").split(","))
                .map(String::trim)
                .filter(browser -> !browser.isBlank());
    }

    @BeforeEach
    void setUp() {
        browserUtils = new BrowserUtils();
    }

    @AfterEach
    void tearDown() {
        if (browserUtils != null) {
            browserUtils.quitDriver();
        }
    }

    protected void start(String browser) {
        browserUtils.setupDriver(browser);
        mainPage = new MainPage(browserUtils);
    }

    protected Credentials credentials() {
        String login = System.getenv().getOrDefault("TUTU_LOGIN", "autotest@example.com");
        String password = System.getenv().getOrDefault("TUTU_PASSWORD", "AutotestPassword123");
        String otp = System.getenv("TUTU_OTP");
        return new Credentials(login, password, otp);
    }

    protected TestPassenger defaultPassenger() {
        return new TestPassenger(
                "Иванов",
                "Иван",
                "Иванович",
                LocalDate.of(1990, 1, 15),
                "4510123456"
        );
    }

    protected record Credentials(String login, String password, String otp) {
    }
}
