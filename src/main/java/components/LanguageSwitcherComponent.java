package components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.net.URI;

/**
 * The header's language toggle (tbcx-lang-switcher), present on every page.
 * It is a single toggle rather than a list: it shows the *other* language's
 * label (ქარ on English pages, EN on Georgian ones) and clicking it reloads the
 * current page under the other locale prefix (/en/... <-> /ka/...).
 */
public class LanguageSwitcherComponent {

    private final Page page;
    private final Locator toggle;

    public LanguageSwitcherComponent(Page page) {
        this.page = page;
        // rendered twice (desktop + tablet header rows); both behave identically
        this.toggle = page.locator("tbcx-lang-switcher .tbcx-language-select__field")
                .locator("visible=true").first();
    }

    public String currentLocale() {
        String path = URI.create(page.url()).getPath();
        return path.length() >= 3 ? path.substring(1, 3) : "";
    }

    /**
     * Switches to the given locale ("ka" or "en") if the page isn't already in it,
     * then waits for the URL to carry the new prefix and for the header to be
     * re-rendered for that locale, so the next action hits the translated page.
     */
    public void switchTo(String locale) {
        if (locale.equals(currentLocale())) {
            return;
        }
        toggle.click();
        page.waitForURL(url -> URI.create(url).getPath().startsWith("/" + locale));
        page.locator("a[href='/" + locale + "']").first()
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }
}
