import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Поиск транспорта")
class TransportSearchTest extends AbstractTutuWebDriverTest {

    @ParameterizedTest(name = "Ж/д маршрут: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость ищет ж/д маршрут и просматривает результаты")
    void guestSearchesTrainRouteAndViewsResults(String browser) {
        start(browser);

        mainPage.openTrainPage();
        mainPage.searchRoute(DEFAULT_FROM, DEFAULT_TO, LocalDate.now().plusDays(14));

        assertTrue(mainPage.hasSearchResultsContent(),
                "После поиска поезда пользователь должен увидеть результаты");
    }

    @ParameterizedTest(name = "Авиарейс: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость ищет авиарейс и просматривает результаты")
    void guestSearchesFlightAndViewsResults(String browser) {
        start(browser);

        mainPage.openAviaPage();
        mainPage.searchRoute(DEFAULT_FROM, "Сочи", LocalDate.now().plusDays(30));

        assertTrue(mainPage.hasSearchResultsContent(),
                "После поиска авиарейса пользователь должен увидеть результаты");
    }

    @ParameterizedTest(name = "Автобус: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость ищет автобусный рейс и просматривает результаты")
    void guestSearchesBusAndViewsResults(String browser) {
        start(browser);

        mainPage.openBusPage();
        mainPage.searchRoute(DEFAULT_FROM, "Тверь", LocalDate.now().plusDays(7));

        assertTrue(mainPage.hasSearchResultsContent(),
                "После поиска автобуса пользователь должен увидеть результаты");
    }

    @ParameterizedTest(name = "Электричка: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость ищет электричку и просматривает расписание")
    void guestSearchesSuburbanTrainAndViewsSchedule(String browser) {
        start(browser);

        mainPage.openSuburbanTrainPage();
        mainPage.searchSuburbanTrain(DEFAULT_FROM, "Зеленоград", LocalDate.now().plusDays(3));

        assertTrue(mainPage.hasSuburbanContent(),
                "После поиска электрички пользователь должен увидеть расписание или билет");
    }
}
