package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import constants.UrlConstants;

/**
 * The instant-transfers overview on /en/instant-transfers, and the two detail
 * pages reachable from it: the "from other bank" article (local transfers)
 * and the PAN-transfer page (sending money abroad by card number).
 */
public class TransfersPage extends BasePage {

    private final Locator seeMoreFromOtherBankLink;
    private final Locator panTransferDetailsLink;
    private final Locator breadcrumbInstantTransfersLink;

    public TransfersPage(Page page) {
        super(page);
        this.seeMoreFromOtherBankLink = page.locator("a[href='/en/articles/instant-transfers']").first();
        this.panTransferDetailsLink = page.locator("a[href='/en/instant-transfers/pan-transfers']").first();
        this.breadcrumbInstantTransfersLink = page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Instant Transfers"));
    }

    public TransfersPage open() {
        open(UrlConstants.INSTANT_TRANSFERS_EN);
        return this;
    }

    public void openLocalTransferDetails() {
        seeMoreFromOtherBankLink.click();
    }

    public void goBack() {
        page.goBack();
        breadcrumbInstantTransfersLink.first().waitFor();
    }

    public void openInternationalTransferDetails() {
        panTransferDetailsLink.click();
    }

    public String getUrl() {
        return page.url();
    }
}
