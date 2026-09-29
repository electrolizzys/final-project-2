package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * The /business (For Business) landing page. Reached via the header's
 * locale-agnostic link; only the hero heading text differs between locales,
 * which is supplied by the caller rather than hardcoded here.
 */
public class BusinessPage extends BasePage {

    private final Locator startupPlanOfferLink;

    public BusinessPage(Page page) {
        super(page);
        // the mega-menu hides several more links to the same href, so only the visible (real) one is targeted
        this.startupPlanOfferLink = page.locator("a[href$='/business/daily-banking/business-subscriptions']")
                .locator("visible=true").first();
    }

    public BusinessSubscriptionsPage openStartupPlanOffer() {
        startupPlanOfferLink.click();
        return new BusinessSubscriptionsPage(page);
    }
}
