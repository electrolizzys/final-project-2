package tests;

import pages.BusinessPage;
import pages.BusinessSubscriptionsPage;
import pages.HomePage;
import pages.StartupPlanPage;
import steps.BusinessLocalizationSteps;
import utils.TestDataProviders;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * One user journey (homepage -> switch language -> For Business -> Startup
 * Plan offer -> Startup Plan details), run for both locales with the same test
 * logic; the rows come from TestDataProviders.locales. Each run starts in the
 * *other* locale and switches through the header's language toggle, so the
 * journey is verified to keep working after a locale change.
 */
public class BusinessPlanLocalizationTest extends BaseTest {

    @Test(dataProvider = TestDataProviders.LOCALES, dataProviderClass = TestDataProviders.class,
            description = "KAN-T20 | View the Startup Plan offer in both Georgian and English")
    public void startupPlanOfferIsShownInBothLocales(String startHomeUrl,
                                                      String targetLocale,
                                                      String heroHeadingText,
                                                      String startupPlanCardHeading,
                                                      String benefitsHeadingText) {
        BusinessLocalizationSteps steps = new BusinessLocalizationSteps(page);
        HomePage home = steps.openHomepageAndSwitchLanguage(startHomeUrl, targetLocale);
        Assert.assertEquals(home.currentLocale(), targetLocale, "Language toggle should switch the site locale");

        BusinessPage business = steps.navigateToBusinessPage(home);
        assertVisible(business.headingWithText(heroHeadingText));

        BusinessSubscriptionsPage subscriptions = steps.openStartupPlanOffer(business);
        assertVisible(subscriptions.headingWithText(startupPlanCardHeading));

        StartupPlanPage startupPlan = steps.openStartupPlanDetails(subscriptions);
        assertVisible(startupPlan.headingWithText(benefitsHeadingText));
        Assert.assertEquals(startupPlan.currentLocale(), targetLocale, "Journey should stay in the chosen locale");
    }
}
