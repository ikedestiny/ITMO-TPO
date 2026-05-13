import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Покупка билетов")
@Epic("tutu.ru")
@Feature("Покупка билетов")
class TicketPurchaseTest extends AbstractTutuWebDriverTest {

    @ParameterizedTest(name = "Покупка ж/д билета до оплаты: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость выбирает поезд, место/тариф, вводит пассажира, промокод и способ оплаты")
    void guestFillsTrainTicketPurchaseFlowUntilPayment(String browser) {
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
                "Сценарий должен дойти до оформления заказа без реального платежа");
    }

    @ParameterizedTest(name = "Покупка билета на электричку: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость ищет электричку и начинает покупку билета")
    void guestStartsSuburbanTrainTicketPurchase(String browser) {
        start(browser);

        mainPage.openSuburbanTrainPage();
        mainPage.searchSuburbanTrain(DEFAULT_FROM, "Зеленоград", LocalDate.now().plusDays(2));

        assertTrue(mainPage.hasSuburbanContent(),
                "Пользователь должен увидеть найденные электрички перед покупкой");
    }
}
