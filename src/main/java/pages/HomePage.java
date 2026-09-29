package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import constants.UrlConstants;

/**
 * The tbcbank.ge homepage. Its "quick action" cards (exchange rate, loan
 * calculator, locations, instant transfers) render lazily once scrolled into
 * view, so revealing them polls a real DOM condition instead of sleeping.
 */
public class HomePage extends BasePage {

    private static final int MAX_SCROLL_ATTEMPTS = 25;

    public HomePage(Page page) {
        super(page);
    }

    public HomePage open() {
        open(UrlConstants.HOME_EN);
        return this;
    }

    /**
     * Opens the homepage for a given locale (e.g. BASE_URL + "/en" or "/ka"),
     * for journeys that must run identically across locales.
     */
    public HomePage openAt(String homeUrl) {
        open(homeUrl);
        return this;
    }

    public void clickForBusinessLink() {
        header.clickForBusiness();
    }

    public void clickExchangeRateCard() {
        clickCard("Find out the current exchange rate");
    }

    public void clickLoanCalculatorCard() {
        clickCard("Calculate the loan");
    }

    public void clickLocationsCard() {
        clickCard("Find branches, ATMs and CDMs");
    }

    public void clickInstantTransfersCta() {
        Locator cta = page.getByText("Instant Transfers", new Page.GetByTextOptions().setExact(true))
                .locator("visible=true").first();
        revealInViewport(cta);
        cta.click();
    }

    private void clickCard(String exactCardText) {
        Locator card = page.getByText(exactCardText, new Page.GetByTextOptions().setExact(true)).first();
        revealInViewport(card);
        card.click();
    }

    /**
     * The homepage's promo cards attach to the DOM only once scrolled near the
     * viewport, so we poll the real element count instead of sleeping for a
     * guessed duration; each check round-trips to the browser, which is what
     * gives the lazy section time to render between scroll steps.
     */
    private void revealInViewport(Locator target) {
        for (int attempt = 0; attempt < MAX_SCROLL_ATTEMPTS && target.count() == 0; attempt++) {
            page.mouse().wheel(0, 1200);
        }
        target.scrollIntoViewIfNeeded();
    }
}
