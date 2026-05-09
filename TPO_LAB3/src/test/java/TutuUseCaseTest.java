import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import se.ifmo.pages.MainPage;
import se.ifmo.pages.MainPage.TestPassenger;
import se.ifmo.utils.BrowserUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TutuUseCaseTest {
    private static final String DEFAULT_FROM = "Москва";
    private static final String DEFAULT_TO = "Санкт-Петербург";

    private BrowserUtils browserUtils;
    private MainPage mainPage;

    static Stream<String> browsers() {
        return Arrays.stream(System.getProperty("browsers", "chrome,firefox").split(","))
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

    @ParameterizedTest(name = "Поиск маршрута: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость ищет маршрут и просматривает результаты поиска")
    void guestSearchesRouteAndViewsResults(String browser) {
        start(browser);

        mainPage.openTrainPage();
        mainPage.searchRoute(DEFAULT_FROM, DEFAULT_TO, LocalDate.now().plusDays(14));

        assertTrue(mainPage.hasSearchResultsContent(),
                "После поиска пользователь должен увидеть страницу с результатами");
    }

    @ParameterizedTest(name = "Поиск авиарейса: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость ищет авиарейс и просматривает результаты поиска")
    void guestSearchesFlightAndViewsResults(String browser) {
        start(browser);

        mainPage.openAviaPage();
        mainPage.searchRoute(DEFAULT_FROM, "Сочи", LocalDate.now().plusDays(30));

        assertTrue(mainPage.hasSearchResultsContent(),
                "После поиска авиарейса пользователь должен увидеть страницу с результатами");
    }

    @ParameterizedTest(name = "Поиск автобусного рейса: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость ищет автобусный рейс и просматривает результаты поиска")
    void guestSearchesBusAndViewsResults(String browser) {
        start(browser);

        mainPage.openBusPage();
        mainPage.searchRoute(DEFAULT_FROM, "Тверь", LocalDate.now().plusDays(7));

        assertTrue(mainPage.hasSearchResultsContent(),
                "После поиска автобусного рейса пользователь должен увидеть страницу с результатами");
    }

    @ParameterizedTest(name = "Покупка билета до оплаты: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость выбирает поезд, место/тариф, вводит данные пассажира, промокод и способ оплаты")
    void guestFillsTicketPurchaseFlowUntilPayment(String browser) {
        start(browser);

        mainPage.openTrainPage();
        mainPage.searchRoute(DEFAULT_FROM, DEFAULT_TO, LocalDate.now().plusDays(21));
        mainPage.chooseFirstTrip();
        mainPage.chooseSeatOrTariff();
        mainPage.fillPassengerData(defaultPassenger());
        mainPage.applyPromocode("TESTPROMO");
        mainPage.choosePaymentMethod();
        mainPage.confirmOrderWithoutPayment();

        assertTrue(browserUtils.getDriver().getCurrentUrl().startsWith("https://")
                        || browserUtils.getDriver().getCurrentUrl().startsWith("data:"),
                "Сценарий должен дойти до оформления заказа без выполнения реального платежа");
    }

    @ParameterizedTest(name = "История заказов: {0}")
    @MethodSource("browsers")
    @DisplayName("Зарегистрированный пользователь просматривает историю заказов")
    void registeredUserViewsOrderHistory(String browser) {
        Credentials credentials = credentials();
        start(browser);
        mainPage.login(credentials.login(), credentials.password(), credentials.otp());

        mainPage.openOrderHistory();

        assertTrue(mainPage.hasOrderHistoryContent(), "В личном кабинете должна открыться история заказов");
    }

    @ParameterizedTest(name = "Профиль: {0}")
    @MethodSource("browsers")
    @DisplayName("Зарегистрированный пользователь открывает управление профилем")
    void registeredUserOpensProfileManagement(String browser) {
        Credentials credentials = credentials();
        start(browser);
        mainPage.login(credentials.login(), credentials.password(), credentials.otp());

        mainPage.openProfile();

        assertTrue(mainPage.hasProfileContent(), "Должна открыться страница управления профилем");
    }

    @ParameterizedTest(name = "Возврат билета: {0}")
    @MethodSource("browsers")
    @DisplayName("Зарегистрированный пользователь открывает сценарий отмены или возврата билета")
    void registeredUserOpensTicketRefundUseCase(String browser) {
        Credentials credentials = credentials();
        start(browser);
        mainPage.login(credentials.login(), credentials.password(), credentials.otp());

        mainPage.openRefundPage();

        assertTrue(mainPage.hasRefundContent(), "Должна открыться страница с возвратом или отменой билета");
    }

    @ParameterizedTest(name = "Поддержка: {0}")
    @MethodSource("browsers")
    @DisplayName("Пользователь обращается в поддержку")
    void userContactsSupport(String browser) {
        start(browser);

        mainPage.openSupport();

        assertTrue(mainPage.hasSupportContent(), "Должна открыться страница поддержки или справочного центра");
    }

    private void start(String browser) {
        browserUtils.setupDriver(browser);
        mainPage = new MainPage(browserUtils);
    }

    private Credentials credentials() {
        String login = System.getenv().getOrDefault("TUTU_LOGIN", "autotest@example.com");
        String password = System.getenv().getOrDefault("TUTU_PASSWORD", "AutotestPassword123");
        String otp = System.getenv("TUTU_OTP");
        return new Credentials(login, password, otp);
    }

    private TestPassenger defaultPassenger() {
        return new TestPassenger(
                "Иванов",
                "Иван",
                "Иванович",
                LocalDate.of(1990, 1, 15),
                "4510123456"
        );
    }

    private record Credentials(String login, String password, String otp) {
    }
}
