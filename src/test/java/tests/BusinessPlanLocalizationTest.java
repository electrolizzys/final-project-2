package tests;

import constants.UrlConstants;
import pages.BusinessPage;
import pages.BusinessSubscriptionsPage;
import pages.HomePage;
import pages.StartupPlanPage;
import steps.BusinessLocalizationSteps;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * One user journey (homepage -> switch language -> For Business -> Startup
 * Plan offer -> Startup Plan details), run for both locales with the same test
 * logic. Each run starts in the *other* locale and switches through the header's
 * language toggle, so the journey is verified to keep working after a locale
 * change; only the expected localized strings come from the data provider.
 */
public class BusinessPlanLocalizationTest extends BaseTest {

    @DataProvider(name = "locales")
    public Object[][] locales() {
        return new Object[][]{
                {
                        UrlConstants.BASE_URL + "/ka",
                        "en",
                        "Manage your company's finances remotely",
                        "Startup plan",
                        "Free internet and mobile banking"
                },
                {
                        UrlConstants.BASE_URL + "/en",
                        "ka",
                        "მართეთ კომპანიის ფინანსები დისტანციურად",
                        "სტარტაპ ნაკრები",
                        "უფასო ინტერნეტ და მობაილ ბანკი"
                }
        };
    }

    @Test(dataProvider = "locales", description = "KAN-T20 | View the Startup Plan offer in both Georgian and English")
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
