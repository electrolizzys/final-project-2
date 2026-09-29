package steps;

import com.microsoft.playwright.Page;
import pages.BusinessPage;
import pages.BusinessSubscriptionsPage;
import pages.HomePage;
import pages.StartupPlanPage;

/**
 * Business flow for the Startup Plan offer scenario: homepage -> switch
 * language via the header toggle -> "For Business" header link -> Startup
 * Plan offer -> plan details. The same flow is reused for every locale; only
 * the start URL and target locale passed in change.
 */
public class BusinessLocalizationSteps {

    private final Page page;

    public BusinessLocalizationSteps(Page page) {
        this.page = page;
    }

    public HomePage openHomepageAndSwitchLanguage(String startHomeUrl, String targetLocale) {
        HomePage home = new HomePage(page).openAt(startHomeUrl);
        home.switchLanguageTo(targetLocale);
        return home;
    }

    public BusinessPage navigateToBusinessPage(HomePage home) {
        home.clickForBusinessLink();
        return new BusinessPage(page);
    }

    public BusinessSubscriptionsPage openStartupPlanOffer(BusinessPage business) {
        return business.openStartupPlanOffer();
    }

    public StartupPlanPage openStartupPlanDetails(BusinessSubscriptionsPage subscriptions) {
        return subscriptions.openStartupPlanDetails();
    }
}
