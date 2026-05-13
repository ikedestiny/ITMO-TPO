import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Личный кабинет")
@Epic("tutu.ru")
@Feature("Личный кабинет")
class AccountManagementTest extends AbstractTutuWebDriverTest {

    @ParameterizedTest(name = "История заказов: {0}")
    @MethodSource("browsers")
    @DisplayName("Зарегистрированный пользователь просматривает историю заказов")
    void registeredUserViewsOrderHistory(String browser) {
        Credentials credentials = credentials();
        start(browser);
        mainPage.login(credentials.login(), credentials.password(), credentials.otp());

        mainPage.openOrderHistory();

        assertTrue(mainPage.hasOrderHistoryContent(),
                "В личном кабинете должна открыться история заказов");
    }

    @ParameterizedTest(name = "Профиль: {0}")
    @MethodSource("browsers")
    @DisplayName("Зарегистрированный пользователь открывает управление профилем")
    void registeredUserOpensProfileManagement(String browser) {
        Credentials credentials = credentials();
        start(browser);
        mainPage.login(credentials.login(), credentials.password(), credentials.otp());

        mainPage.openProfile();

        assertTrue(mainPage.hasProfileContent(),
                "Должна открыться страница управления профилем");
    }

    @ParameterizedTest(name = "Возврат билета: {0}")
    @MethodSource("browsers")
    @DisplayName("Зарегистрированный пользователь открывает сценарий отмены или возврата билета")
    void registeredUserOpensTicketRefundUseCase(String browser) {
        Credentials credentials = credentials();
        start(browser);
        mainPage.login(credentials.login(), credentials.password(), credentials.otp());

        mainPage.openRefundPage();

        assertTrue(mainPage.hasRefundContent(),
                "Должна открыться страница с возвратом или отменой билета");
    }
}
