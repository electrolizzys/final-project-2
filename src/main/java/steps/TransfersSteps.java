package steps;

import com.microsoft.playwright.Page;
import pages.HomePage;
import pages.TransfersPage;

/**
 * Business flow for the instant-transfers scenario: homepage -> overview ->
 * local transfer details -> back to overview -> international transfer details.
 */
public class TransfersSteps {

    private final Page page;

    public TransfersSteps(Page page) {
        this.page = page;
    }

    public TransfersPage navigateToTransfersViaHomepage() {
        new HomePage(page).open().clickInstantTransfersCta();
        return new TransfersPage(page);
    }

    public void openLocalTransferDetails(TransfersPage transfers) {
        transfers.openLocalTransferDetails();
    }

    public void returnToOverview(TransfersPage transfers) {
        transfers.goBack();
    }

    public void openInternationalTransferDetails(TransfersPage transfers) {
        transfers.openInternationalTransferDetails();
    }
}
