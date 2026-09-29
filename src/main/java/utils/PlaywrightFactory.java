package utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * Owns one Playwright/Browser/BrowserContext/Page per thread so TestNG can run
 * test classes in parallel without tests sharing browser state.
 */
public final class PlaywrightFactory {

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    private PlaywrightFactory() {
    }

    public static Page createPage() {
        Playwright playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(ConfigReader.headless());

        Browser browser = switch (ConfigReader.browser().toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(launchOptions);
            case "webkit" -> playwright.webkit().launch(launchOptions);
            default -> playwright.chromium().launch(launchOptions);
        };

        BrowserContext context = browser.newContext();
        context.setDefaultTimeout(ConfigReader.defaultTimeoutMs());
        Page page = context.newPage();

        PLAYWRIGHT.set(playwright);
        BROWSER.set(browser);
        CONTEXT.set(context);
        PAGE.set(page);
        return page;
    }

    public static Page getPage() {
        return PAGE.get();
    }

    public static void closePage() {
        try {
            if (CONTEXT.get() != null) {
                CONTEXT.get().close();
            }
        } finally {
            if (BROWSER.get() != null) {
                BROWSER.get().close();
            }
            if (PLAYWRIGHT.get() != null) {
                PLAYWRIGHT.get().close();
            }
            PAGE.remove();
            CONTEXT.remove();
            BROWSER.remove();
            PLAYWRIGHT.remove();
        }
    }
}
