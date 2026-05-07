package se.ifmo.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BrowserFactory {
    private static Playwright playwright;
    private static Browser browser;
    private static Page page;

    public static Page createPage() {
        String browserName = System.getProperty("browser", "chromium").toLowerCase();
        playwright = Playwright.create();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false);
        switch (browserName) {
            case "firefox":
                browser = playwright.firefox().launch(options);
                break;
            case "webkit":
                browser = playwright.webkit().launch(options);
                break;
            default:
                browser = playwright.chromium().launch(options);
        }

        page = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1024, 768)
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")).newPage();

        return page;
    }

    public static void close() {
        if (page != null) page.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}