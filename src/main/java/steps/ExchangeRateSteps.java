package steps;

import com.microsoft.playwright.Page;
import pages.ExchangeRatePage;
import pages.HomePage;

/**
 * Business actions for the exchange rates page: reaching it (via the homepage's
 * quick-action card, or directly) and changing the converter's sell amount.
 */
public class ExchangeRateSteps {

    private final Page page;

    public ExchangeRateSteps(Page page) {
        this.page = page;
    }

    public ExchangeRatePage navigateToExchangeRateViaHomepage() {
        new HomePage(page).open().clickExchangeRateCard();
        return new ExchangeRatePage(page);
    }

    public ExchangeRatePage openExchangeRatesPage() {
        return new ExchangeRatePage(page).open();
    }

    public String changeSellAmount(ExchangeRatePage exchangeRate, String amount) {
        return exchangeRate.enterSellAmountAndAwaitConvertedValue(amount);
    }
}
