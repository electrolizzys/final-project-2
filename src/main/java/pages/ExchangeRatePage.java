package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import constants.UrlConstants;
import utils.ConfigReader;

/**
 * The exchange rates page on /en/valutis-kursi: the currency converter (whose
 * two amount fields have stable, dedicated ids) and the rate cards above it
 * showing each popular currency's buy, sell and official rate.
 */
public class ExchangeRatePage extends BasePage {

    private final Locator sellAmountInput;
    private final Locator buyAmountInput;

    public ExchangeRatePage(Page page) {
        super(page);
        this.sellAmountInput = page.locator("#sell-amount");
        this.buyAmountInput = page.locator("#buy-amount");
    }

    public ExchangeRatePage open() {
        open(UrlConstants.EXCHANGE_RATE_EN);
        return this;
    }

    /**
     * Opens the converter pre-set to a given currency pair and amount, e.g.
     * open("EUR", "GEL", "250") -> /en/valutis-kursi/EUR-to-GEL?amount=250.
     * Used by the database-driven scenario, which varies the pair per record.
     */
    public ExchangeRatePage open(String fromCurrency, String toCurrency, String amount) {
        open(UrlConstants.BASE_URL + "/en/valutis-kursi/" + fromCurrency + "-to-" + toCurrency + "?amount=" + amount);
        // the converted figure populates asynchronously after navigation; wait
        // for the real value rather than reading the field while still empty.
        PlaywrightAssertions.assertThat(buyAmountInput).not().hasValue("",
                new LocatorAssertions.HasValueOptions().setTimeout(ConfigReader.defaultTimeoutMs()));
        return this;
    }

    public String getBuyAmount() {
        return buyAmountInput.inputValue();
    }

    public String getSellAmount() {
        return sellAmountInput.inputValue();
    }

    /**
     * Fills the sell amount and waits - via a web-first assertion, not a sleep -
     * for the converted amount to actually change before handing control back.
     */
    public String enterSellAmountAndAwaitConvertedValue(String amount) {
        String buyValueBefore = buyAmountInput.inputValue();
        sellAmountInput.fill(amount);
        PlaywrightAssertions.assertThat(buyAmountInput).not().hasValue(buyValueBefore,
                new LocatorAssertions.HasValueOptions().setTimeout(ConfigReader.defaultTimeoutMs()));
        return buyAmountInput.inputValue();
    }

    public String getRateCardTitle(String currencyCode) {
        return rateCard(currencyCode).locator(".tbcx-pw-popular-currencies__title").innerText().trim();
    }

    public double getRateCardOfficialRate(String currencyCode) {
        return Double.parseDouble(rateCard(currencyCode)
                .locator(".tbcx-pw-popular-currencies__caption strong").innerText().trim());
    }

    public double getRateCardBuyRate(String currencyCode) {
        return rateCardColumnValue(currencyCode, "Buy");
    }

    public double getRateCardSellRate(String currencyCode) {
        return rateCardColumnValue(currencyCode, "Sell");
    }

    /**
     * One card in the "popular currencies" list (USD, EUR, GBP), found by the
     * currency code in its badge - scoping to the card keeps its Buy/Sell cells
     * apart from the converter's own Buy/Sell labels elsewhere on the page.
     */
    private Locator rateCard(String currencyCode) {
        return page.locator("tbcx-pw-popular-currency-item").filter(new Locator.FilterOptions()
                .setHas(page.locator(".tbcx-pw-currency-badge", new Page.LocatorOptions().setHasText(currencyCode))));
    }

    private double rateCardColumnValue(String currencyCode, String caption) {
        String value = rateCard(currencyCode)
                .locator(".tbcx-pw-popular-currencies__col")
                .filter(new Locator.FilterOptions().setHasText(caption))
                .locator(".tbcx-pw-popular-currencies__body")
                .innerText();
        return Double.parseDouble(value.trim());
    }
}
