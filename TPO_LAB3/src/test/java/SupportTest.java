import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Поддержка")
class SupportTest extends AbstractTutuWebDriverTest {

    @ParameterizedTest(name = "Поддержка: {0}")
    @MethodSource("browsers")
    @DisplayName("Пользователь обращается в поддержку")
    void userContactsSupport(String browser) {
        start(browser);

        mainPage.openSupport();

        assertTrue(mainPage.hasSupportContent(),
                "Должна открыться страница поддержки или справочного центра");
    }
}
