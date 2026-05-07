package se.ifmo.pages;

import com.microsoft.playwright.Page;
import se.ifmo.utils.BrowserUtils;

public abstract class BasePage {
    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public BasePage(BrowserUtils browserUtils) {
        this(browserUtils.getPage());
    }

    protected void click(String selector) {
        page.locator(selector).click();
    }

    protected void fillField(String selector, String text) {
        page.locator(selector).clear();
        page.locator(selector).fill(text);
    }

    protected void selectFirstSuggestion() {
        String suggestionSelector = "div[class*='suggest-item'], li[class*='suggest-item'], div[role='option']";
        page.locator(suggestionSelector).first().click();
    }

    protected void acceptCookiesIfPresent() {
        String cookieSelector = "button:has-text('Согласен'), button:has-text('Принять')";
        if (page.locator(cookieSelector).count() > 0) {
            page.locator(cookieSelector).click();
        }
    }
}