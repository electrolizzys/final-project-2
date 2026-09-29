package components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

/**
 * The cookie-consent banner rendered on top of every page on tbcbank.ge.
 * Reused by every Page Object so tests don't duplicate this dismissal logic.
 */
public class CookieBannerComponent {

    private final Locator acceptAllButton;

    public CookieBannerComponent(Page page) {
        this.acceptAllButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(Pattern.compile("accept all", Pattern.CASE_INSENSITIVE)));
    }

    /**
     * Dismisses the banner if it appeared for this session; a no-op otherwise
     * (it isn't shown again once already accepted in the same browser context).
     */
    public void acceptIfPresent() {
        try {
            acceptAllButton.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            acceptAllButton.click();
        } catch (Exception ignoredBannerNotShown) {
            // Banner already dismissed earlier in this context - nothing to do.
        }
    }
}
