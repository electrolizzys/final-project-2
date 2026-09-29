package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * The business subscriptions/plans listing, reached from the Startup Plan
 * offer card on the business landing page. Shows several plan cards,
 * including the Startup plan whose heading text is locale-dependent.
 */
public class BusinessSubscriptionsPage extends BasePage {

    private final Locator startupPlanDetailsLink;

    public BusinessSubscriptionsPage(Page page) {
        super(page);
        this.startupPlanDetailsLink = page.locator("a[href$='/business-subscriptions/startup-business-plan']")
                .locator("visible=true").first();
    }

    public StartupPlanPage openStartupPlanDetails() {
        startupPlanDetailsLink.click();
        return new StartupPlanPage(page);
    }
}
