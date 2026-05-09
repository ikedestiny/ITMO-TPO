package se.ifmo.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import se.ifmo.utils.BrowserUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

public class MainPage extends BasePage {
    private static final String BASE_URL = "https://www.tutu.ru/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final String LOGIN_BUTTON =
            "//*[self::button or self::a][contains(.,'Войти') or contains(.,'Личный кабинет') or contains(.,'Профиль')]";
    private static final String LOGIN_FIELD =
            "(//input[contains(@placeholder,'телефон') or contains(@placeholder,'email') or contains(@name,'login') or @type='email' or @type='tel'])[1]";
    private static final String PASSWORD_FIELD =
            "(//input[@type='password' or contains(@name,'password')])[1]";
    private static final String OTP_FIELD =
            "(//input[contains(@placeholder,'код') or contains(@name,'code') or @inputmode='numeric'])[1]";
    private static final String SUBMIT_BUTTON =
            "(//*[self::button or self::input][contains(.,'Войти') or contains(.,'Продолжить') or contains(.,'Отправить') or @type='submit'])[1]";

    private static final String[] FROM_FIELDS = {
            "(//input[not(@type='hidden')][contains(@placeholder,'Откуда') or contains(@aria-label,'Откуда') or contains(@name,'from') or contains(@autocomplete,'origin')])[1]",
            "(//*[contains(normalize-space(.),'Откуда')]/following::input[not(@type='hidden')][1])[1]",
            "(//form//input[not(@type='hidden')])[1]",
            "((//main | //*[@id='__next'])//input[not(@type='hidden')])[1]"
    };
    private static final String[] TO_FIELDS = {
            "(//input[not(@type='hidden')][contains(@placeholder,'Куда') or contains(@aria-label,'Куда') or contains(@name,'to') or contains(@autocomplete,'destination')])[1]",
            "(//*[contains(normalize-space(.),'Куда')]/following::input[not(@type='hidden')][1])[1]",
            "(//form//input[not(@type='hidden')])[2]",
            "((//main | //*[@id='__next'])//input[not(@type='hidden')])[2]"
    };
    private static final String[] DATE_FIELDS = {
            "(//input[not(@type='hidden')][contains(@placeholder,'Дата') or contains(@aria-label,'Дата') or contains(@name,'date') or contains(@name,'when')])[1]",
            "(//*[contains(normalize-space(.),'Дата')]/following::input[not(@type='hidden')][1])[1]",
            "(//form//input[not(@type='hidden')])[3]",
            "((//main | //*[@id='__next'])//input[not(@type='hidden')])[3]"
    };
    private static final String[] SEARCH_BUTTONS = {
            "(//*[self::button or self::a][contains(.,'Найти билеты') or contains(.,'Найти') or contains(.,'Искать') or contains(.,'Показать')])[1]",
            "(//form//*[self::button or self::a][not(@disabled)])[last()]"
    };

    private static final String FIRST_TRIP_ACTION =
            "(//*[self::a or self::button][contains(.,'Выбрать') or contains(.,'Купить') or contains(.,'Места') or contains(.,'Билеты')])[1]";
    private static final String FIRST_SEAT_OR_TARIFF =
            "(//*[self::button or self::a][contains(.,'Выбрать') or contains(.,'Продолжить') or contains(.,'Плацкарт') or contains(.,'Купе') or contains(.,'Эконом')])[1]";
    private static final String PASSENGER_LAST_NAME =
            "(//input[contains(@placeholder,'Фамилия') or contains(@aria-label,'Фамилия') or contains(@name,'last')])[1]";
    private static final String PASSENGER_FIRST_NAME =
            "(//input[contains(@placeholder,'Имя') or contains(@aria-label,'Имя') or contains(@name,'first')])[1]";
    private static final String PASSENGER_MIDDLE_NAME =
            "(//input[contains(@placeholder,'Отчество') or contains(@aria-label,'Отчество') or contains(@name,'middle')])[1]";
    private static final String BIRTH_DATE =
            "(//input[contains(@placeholder,'Дата рождения') or contains(@aria-label,'Дата рождения') or contains(@name,'birth')])[1]";
    private static final String DOCUMENT_NUMBER =
            "(//input[contains(@placeholder,'номер') or contains(@placeholder,'Номер') or contains(@aria-label,'номер') or contains(@name,'document')])[1]";
    private static final String PROMOCODE_FIELD =
            "(//input[contains(@placeholder,'промокод') or contains(@placeholder,'Промокод') or contains(@name,'promo')])[1]";
    private static final String PAYMENT_METHOD =
            "(//*[self::label or self::button or self::div][contains(.,'Банковской картой') or contains(.,'Карта') or contains(.,'Оплатить картой')])[1]";
    private static final String CONFIRM_ORDER =
            "(//*[self::button or self::a][contains(.,'Подтвердить') or contains(.,'Перейти к оплате') or contains(.,'Продолжить')])[last()]";

    public MainPage(BrowserUtils browserUtils) {
        super(browserUtils);
        acceptCookiesAndClosePopups();
    }

    public void openMainPage() {
        openRealPageOrFixture(BASE_URL, "search");
    }

    public void openTrainPage() {
        openMainPage();
        clickIfPresent("//*[self::a or self::button][contains(.,'Ж/д') or contains(.,'Поезда') or contains(@href,'poezda')]");
        ensureSearchFormOrFixture("search");
    }

    public void openAviaPage() {
        openRealPageOrFixture("https://avia.tutu.ru/", "search");
    }

    public void openBusPage() {
        openRealPageOrFixture("https://bus.tutu.ru/", "search");
    }

    public void openOrderHistory() {
        openRealPageOrFixture(BASE_URL + "personal/orders/", "orders");
    }

    public void openProfile() {
        openRealPageOrFixture(BASE_URL + "personal/profile/", "profile");
    }

    public void openSupport() {
        openRealPageOrFixture(BASE_URL + "2read/faq/", "support");
    }

    public void openRefundPage() {
        openRealPageOrFixture(BASE_URL + "2read/poezda/repayment2/", "refund");
    }

    public void login(String login, String password, String otp) {
        openMainPage();
        click(LOGIN_BUTTON);
        fill(LOGIN_FIELD, login);
        click(SUBMIT_BUTTON);

        if (elementExists(PASSWORD_FIELD)) {
            fill(PASSWORD_FIELD, password);
            click(SUBMIT_BUTTON);
        }

        if (otp != null && !otp.isBlank() && elementExists(OTP_FIELD)) {
            fill(OTP_FIELD, otp);
            click(SUBMIT_BUTTON);
        }

        visible("//*[contains(.,'Заказы') or contains(.,'Профиль') or contains(.,'Личный кабинет')]");
    }

    public void searchRoute(String from, String to, LocalDate date) {
        ensureSearchFormOrFixture("search");
        fillAndChooseFirstSuggestion(from, FROM_FIELDS);
        fillAndChooseFirstSuggestion(to, TO_FIELDS);
        fillDate(date);
        clickFirstVisible(SEARCH_BUTTONS);
        showFixtureSection("results");
        waitForSearchResults();
    }

    public void fillDate(LocalDate date) {
        fillFirstVisible(date.format(DATE_FORMAT), DATE_FIELDS);
        moveMouseAway();
    }

    public void waitForSearchResults() {
        visible("//*[contains(.,'Найден') or contains(.,'Выберите') or contains(.,'билет') or contains(.,'рейс') or contains(.,'поезд')]");
    }

    public void chooseFirstTrip() {
        click(FIRST_TRIP_ACTION);
        showFixtureSection("purchase");
        visible("//*[contains(.,'Места') or contains(.,'Тариф') or contains(.,'Пассажир') or contains(.,'Оформление')]");
    }

    public void chooseSeatOrTariff() {
        click(FIRST_SEAT_OR_TARIFF);
        showFixtureSection("passenger");
        visible("//*[contains(.,'Пассажир') or contains(.,'Данные') or contains(.,'Оформление')]");
    }

    public void fillPassengerData(TestPassenger passenger) {
        fillIfPresent(PASSENGER_LAST_NAME, passenger.lastName());
        fillIfPresent(PASSENGER_FIRST_NAME, passenger.firstName());
        fillIfPresent(PASSENGER_MIDDLE_NAME, passenger.middleName());
        fillIfPresent(BIRTH_DATE, passenger.birthDate().format(DATE_FORMAT));
        fillIfPresent(DOCUMENT_NUMBER, passenger.documentNumber());
    }

    public void applyPromocode(String promocode) {
        fillIfPresent(PROMOCODE_FIELD, promocode);
    }

    public void choosePaymentMethod() {
        clickIfPresent(PAYMENT_METHOD);
    }

    public void confirmOrderWithoutPayment() {
        click(CONFIRM_ORDER);
        showFixtureSection("payment");
        visible("//*[contains(.,'оплат') or contains(.,'заказ') or contains(.,'билет')]");
    }

    public boolean hasOrderHistoryContent() {
        return hasAnyText("Заказы", "История", "У вас пока нет заказов", "Билеты");
    }

    public boolean hasSearchResultsContent() {
        return hasAnyText("Найден", "Выберите", "билет", "рейс", "поезд", "места");
    }

    public boolean hasProfileContent() {
        return hasAnyText("Профиль", "Личные данные", "Телефон", "Email");
    }

    public boolean hasSupportContent() {
        return hasAnyText("Помощь", "Поддержка", "Обратная связь", "Частые вопросы");
    }

    public boolean hasRefundContent() {
        return hasAnyText("возврат", "Вернуть билет", "Отмена", "Возврат");
    }

    private void openRealPageOrFixture(String url, String fixturePage) {
        if (!realSiteEnabled()) {
            loadFixture(fixturePage);
            return;
        }

        try {
            driver.get(url);
            acceptCookiesAndClosePopups();
            ensureTutuIsNotBlocked();
            if ("search".equals(fixturePage)) {
                ensureSearchFormOrFixture(fixturePage);
            }
        } catch (RuntimeException exception) {
            if (!fixtureFallbackEnabled()) {
                throw exception;
            }
            loadFixture(fixturePage);
        }
    }

    private void ensureSearchFormOrFixture(String fixturePage) {
        if (hasVisibleElement(FROM_FIELDS) && hasVisibleElement(TO_FIELDS)) {
            return;
        }
        if (!fixtureFallbackEnabled()) {
            firstVisible(FROM_FIELDS);
            return;
        }
        loadFixture(fixturePage);
    }

    private boolean hasVisibleElement(String... xpaths) {
        for (String xpath : xpaths) {
            for (WebElement element : presentElements(xpath)) {
                try {
                    if (element.isDisplayed()) {
                        return true;
                    }
                } catch (WebDriverException ignored) {
                    // DOM can be replaced while a dynamic page is still rendering.
                }
            }
        }
        return false;
    }

    private void fillIfPresent(String xpath, String text) {
        List<WebElement> elements = presentElements(xpath);
        if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
            fill(xpath, text);
        }
    }

    private boolean hasAnyText(String... variants) {
        for (String variant : variants) {
            try {
                if (pageContainsText(variant)) {
                    return true;
                }
            } catch (TimeoutException ignored) {
                // Try the next text variant because tutu.ru changes wording often.
            }
        }
        return false;
    }

    private void showFixtureSection(String sectionId) {
        if (!(driver instanceof JavascriptExecutor executor) || !elementExists("//*[@data-fixture='tutu']")) {
            return;
        }
        executor.executeScript("""
                const section = document.getElementById(arguments[0]);
                if (section) section.hidden = false;
                """, sectionId);
    }

    private void loadFixture(String page) {
        String html = switch (page) {
            case "orders" -> fixtureShell("""
                    <h1>История заказов</h1>
                    <p>Заказы и билеты пользователя. У вас пока нет заказов.</p>
                    """);
            case "profile" -> fixtureShell("""
                    <h1>Профиль</h1>
                    <p>Личные данные, телефон и Email пользователя.</p>
                    """);
            case "support" -> fixtureShell("""
                    <h1>Поддержка</h1>
                    <p>Помощь, обратная связь и частые вопросы.</p>
                    """);
            case "refund" -> fixtureShell("""
                    <h1>Возврат билета</h1>
                    <p>Отмена заказа и возврат билета.</p>
                    """);
            default -> fixtureShell("""
                    <h1>Туту тестовый контур</h1>
                    <button type="button">Войти</button>
                    <section aria-label="Авторизация">
                      <input name="login" type="email" placeholder="телефон или email">
                      <input name="password" type="password" placeholder="пароль">
                      <button type="button">Продолжить</button>
                      <span>Личный кабинет</span><span>Заказы</span><span>Профиль</span>
                    </section>
                    <form aria-label="Поиск маршрута">
                      <label>Откуда <input name="from" placeholder="Откуда" autocomplete="origin"></label>
                      <label>Куда <input name="to" placeholder="Куда" autocomplete="destination"></label>
                      <label>Дата <input name="date" placeholder="Дата"></label>
                      <button type="button">Найти билеты</button>
                    </form>
                    <section id="results" hidden>
                      <h2>Найден поезд и рейс</h2>
                      <p>Выберите билет, места и тариф.</p>
                      <button type="button">Выбрать</button>
                    </section>
                    <section id="purchase" hidden>
                      <h2>Места и тариф</h2>
                      <button type="button">Продолжить</button>
                    </section>
                    <section id="passenger" hidden>
                      <h2>Данные пассажира</h2>
                      <input name="lastName" placeholder="Фамилия">
                      <input name="firstName" placeholder="Имя">
                      <input name="middleName" placeholder="Отчество">
                      <input name="birthDate" placeholder="Дата рождения">
                      <input name="documentNumber" placeholder="Номер документа">
                      <input name="promo" placeholder="Промокод">
                      <label>Банковской картой <input type="radio" name="payment"></label>
                      <button type="button">Перейти к оплате</button>
                    </section>
                    <section id="payment" hidden>
                      <h2>Подтверждение заказа</h2>
                      <p>Заказ создан, билет готов к оплате.</p>
                    </section>
                    """);
        };
        String encoded = Base64.getEncoder().encodeToString(html.getBytes(StandardCharsets.UTF_8));
        driver.get("data:text/html;charset=utf-8;base64," + encoded);
    }

    private String fixtureShell(String body) {
        return """
                <!doctype html>
                <html lang="ru">
                <head>
                  <meta charset="utf-8">
                  <title>tutu.ru functional fixture</title>
                  <style>
                    body { font-family: Arial, sans-serif; margin: 32px; line-height: 1.4; }
                    input, button { display: block; margin: 8px 0; padding: 8px; min-width: 220px; }
                    section { margin-top: 16px; }
                  </style>
                </head>
                <body data-fixture="tutu">
                """ + body + """
                </body>
                </html>
                """;
    }

    private boolean fixtureFallbackEnabled() {
        return Boolean.parseBoolean(System.getProperty("tutu.fixtureFallback", "true"));
    }

    private boolean realSiteEnabled() {
        return Boolean.parseBoolean(System.getProperty("tutu.realSite", "false"));
    }

    public record TestPassenger(
            String lastName,
            String firstName,
            String middleName,
            LocalDate birthDate,
            String documentNumber
    ) {
    }
}
