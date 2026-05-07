package se.ifmo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import se.ifmo.utils.BrowserUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainPage extends BasePage {

    private static final String TRAIN_TAB = "button:has-text('Поезда'), span:has-text('Поезда') >> ancestor::button";
    private static final String PLANE_TAB = "button:has-text('Самолёты'), span:has-text('Самолёты') >> ancestor::button";
    private static final String BUS_TAB = "button:has-text('Автобусы'), span:has-text('Автобусы') >> ancestor::button";
    private static final String HOTEL_TAB = "#__next > div:nth-child(3) > div:nth-child(2) > div > div > div:nth-child(2) > div:nth-child(2) > button:nth-child(1)";

    private static final String FROM_FIELD = "input#from";
    private static final String TO_FIELD = "input#to";
    private static final String DEPARTURE_DATE = "input#departDate";
    private static final String RETURN_DATE = "input#returnDate";

    private static final String HOTEL_CITY = "input#city";
    private static final String CHECKIN_DATE = "input#checkin";
    private static final String CHECKOUT_DATE = "input#checkout";
    private static final String GUESTS = "input#guests";

    private static final String SEARCH_BUTTON = "button:has-text('Найти')";

    public MainPage(BrowserUtils browserUtils) {
        super(browserUtils);
        page.locator(FROM_FIELD).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        acceptCookiesIfPresent();
    }

    public void selectTrainTab() { click(TRAIN_TAB); }
    public void selectPlaneTab() { click(PLANE_TAB); }
    public void selectBusTab() { click(BUS_TAB); }
    public void selectHotelTab() { click(HOTEL_TAB); }

    public void setFromCity(String city) {
        fillField(FROM_FIELD, city);
        selectFirstSuggestion();
    }

    public void setToCity(String city) {
        fillField(TO_FIELD, city);
        selectFirstSuggestion();
    }

    public void setDepartureDate(LocalDate date) {
        String formatted = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        fillField(DEPARTURE_DATE, formatted);
        page.locator(DEPARTURE_DATE).click(); // закрыть календарь
    }

    public void setReturnDate(LocalDate date) {
        fillField(RETURN_DATE, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    public void setHotelCity(String city) {
        fillField(HOTEL_CITY, city);
        selectFirstSuggestion();
    }

    public void setCheckInDate(LocalDate date) {
        fillField(CHECKIN_DATE, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    public void setCheckoutDate(LocalDate date) {
        fillField(CHECKOUT_DATE, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    public void setGuests(int guests) {
        fillField(GUESTS, String.valueOf(guests));
    }

    public void clickSearch() {
        click(SEARCH_BUTTON);
    }

    // Комбинированные методы
    public void searchTrain(String from, String to, LocalDate date) {
        selectTrainTab();
        setFromCity(from);
        setToCity(to);
        setDepartureDate(date);
        clickSearch();
    }

    public void searchPlane(String from, String to, LocalDate date) {
        selectPlaneTab();
        setFromCity(from);
        setToCity(to);
        setDepartureDate(date);
        clickSearch();
    }

    public void searchBus(String from, String to, LocalDate date) {
        selectBusTab();
        setFromCity(from);
        setToCity(to);
        setDepartureDate(date);
        clickSearch();
    }

    public void searchHotel(String city, LocalDate checkin, LocalDate checkout, int guests) {
        selectHotelTab();
        setHotelCity(city);
        setCheckInDate(checkin);
        setCheckoutDate(checkout);
        setGuests(guests);
        clickSearch();
    }
}