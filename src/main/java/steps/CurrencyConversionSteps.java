package steps;

import com.microsoft.playwright.Page;
import database.models.CurrencyConversionRecord;
import pages.ExchangeRatePage;

/**
 * Business flow for the database-driven conversion scenario: open the
 * converter pre-set to the currency pair and amount from one DB record.
 */
public class CurrencyConversionSteps {

    private final Page page;

    public CurrencyConversionSteps(Page page) {
        this.page = page;
    }

    public ExchangeRatePage openConversionPage(CurrencyConversionRecord record) {
        return new ExchangeRatePage(page)
                .open(record.getFromCurrency(), record.getToCurrency(), String.valueOf(record.getAmount()));
    }

    /**
     * Enters twice the record's amount and returns the recalculated converted value.
     */
    public String doubleSellAmount(ExchangeRatePage exchangeRate, CurrencyConversionRecord record) {
        return exchangeRate.enterSellAmountAndAwaitConvertedValue(String.valueOf(record.getAmount() * 2));
    }
}
