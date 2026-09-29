package tests;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import pages.TransfersPage;
import steps.TransfersSteps;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

public class TransfersTest extends BaseTest {

    @Test(description = "KAN-T19 | View local and international transfer options")
    public void localAndInternationalTransferDetailsOpenFromOverview() {
        TransfersSteps steps = new TransfersSteps(page);
        TransfersPage transfers = steps.navigateToTransfersViaHomepage();

        steps.openLocalTransferDetails(transfers);
        PlaywrightAssertions.assertThat(page).hasURL(Pattern.compile(".*/en/articles/instant-transfers"));

        steps.returnToOverview(transfers);
        steps.openInternationalTransferDetails(transfers);
        PlaywrightAssertions.assertThat(page).hasURL(Pattern.compile(".*/en/instant-transfers/pan-transfers"));
    }
}
