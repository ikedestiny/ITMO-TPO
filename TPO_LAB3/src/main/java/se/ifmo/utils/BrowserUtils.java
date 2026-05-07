package se.ifmo.utils;

import com.microsoft.playwright.Page;

public class BrowserUtils {
    private Page page;

    public void setupDriver() {
        page = BrowserFactory.createPage();
        page.navigate("https://www.tutu.ru/");
    }

    public Page getPage() {
        return page;
    }

    public void quitDriver() {
        BrowserFactory.close();
        page = null;
    }
}