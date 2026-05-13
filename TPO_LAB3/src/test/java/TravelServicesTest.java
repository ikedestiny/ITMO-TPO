import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Дополнительные сервисы путешествий")
class TravelServicesTest extends AbstractTutuWebDriverTest {

    @ParameterizedTest(name = "Аренда авто: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость ищет автомобиль для аренды")
    void guestSearchesCarRentalOptions(String browser) {
        start(browser);

        mainPage.openCarRentalPage();
        mainPage.searchCarRental("Сочи", LocalDate.now().plusDays(10), LocalDate.now().plusDays(14));

        assertTrue(mainPage.hasCarRentalContent(),
                "После поиска аренды авто пользователь должен увидеть варианты машин");
    }

    @ParameterizedTest(name = "Бронирование жилья: {0}")
    @MethodSource("browsers")
    @DisplayName("Гость ищет жилье для бронирования")
    void guestSearchesLodgingOptions(String browser) {
        start(browser);

        mainPage.openHotelsPage();
        mainPage.searchHotel("Казань", LocalDate.now().plusDays(20), LocalDate.now().plusDays(23));

        assertTrue(mainPage.hasHotelContent(),
                "После поиска жилья пользователь должен увидеть отели или варианты бронирования");
    }
}
