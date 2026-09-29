package components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * The persistent top navigation bar rendered on every tbcbank.ge page, in
 * every locale. The logo and "For Business" link are targeted by href
 * suffix rather than full path, since the path prefix (/en, /ka) is the
 * only part that changes between locales - letting every Page Object and
 * Steps class reuse this same component regardless of which locale it navigated to.
 */
public class HeaderComponent {

    private final Locator logoLink;
    private final Locator forBusinessLink;

    public HeaderComponent(Page page) {
        this.logoLink = page.locator("a[href='/en'], a[href='/ka']").first();
        this.forBusinessLink = page.locator("a[href$='/business']").first();
    }

    public void waitUntilLoaded() {
        logoLink.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void clickForBusiness() {
        forBusinessLink.click();
    }
}
