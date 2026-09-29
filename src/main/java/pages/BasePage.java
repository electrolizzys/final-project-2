package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import components.CookieBannerComponent;
import components.HeaderComponent;
import components.LanguageSwitcherComponent;

/**
 * Common navigation/synchronization plumbing shared by every Page Object.
 * Selectors specific to a single page live in that page's own subclass;
 * anything shared across pages (header, cookie banner) is delegated to components.
 */
public abstract class BasePage {

    protected final Page page;
    protected final HeaderComponent header;
    protected final CookieBannerComponent cookieBanner;
    protected final LanguageSwitcherComponent languageSwitcher;

    protected BasePage(Page page) {
        this.page = page;
        this.header = new HeaderComponent(page);
        this.cookieBanner = new CookieBannerComponent(page);
        this.languageSwitcher = new LanguageSwitcherComponent(page);
    }

    public void switchLanguageTo(String locale) {
        languageSwitcher.switchTo(locale);
    }

    public String currentLocale() {
        return languageSwitcher.currentLocale();
    }

    protected void open(String url) {
        page.navigate(url);
        header.waitUntilLoaded();
        cookieBanner.acceptIfPresent();
    }

    /**
     * A locale-agnostic way to assert on translated content: the selector
     * mechanism (getByText) lives here, in the page/component layer; only
     * the expected string itself is supplied by the caller as test data.
     */
    public Locator headingWithText(String exactText) {
        return page.getByText(exactText, new Page.GetByTextOptions().setExact(true));
    }
}
