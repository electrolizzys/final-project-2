package tests;

import api.models.CommercialRate;
import pages.ExchangeRatePage;
import steps.CommercialRatesApiSteps;
import steps.ExchangeRateSteps;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * API -> UI consistency: the commercial rates API is the source of truth, and
 * each popular currency's card on the exchange rates page must show the same
 * name, buy rate, sell rate and official rate.
 */
public class ExchangeRateApiToUiTest extends BaseTest {

    // rates are shown with trailing zeros trimmed (API 3.530 -> UI "3.53"), so compare numerically
    private static final double RATE_TOLERANCE = 0.00001;

    @DataProvider(name = "popularCurrencies")
    public Object[][] popularCurrencies() {
        return new Object[][]{{"USD"}, {"EUR"}, {"GBP"}};
    }

    @Test(dataProvider = "popularCurrencies",
            description = "KAN-T24 | Verify the exchange rates page shows the same rates as the commercial rates API")
    public void rateCardMatchesCommercialRatesApi(String currencyCode) {
        CommercialRate expected = new CommercialRatesApiSteps().getCommercialRate(currencyCode);

        ExchangeRatePage exchangeRates = new ExchangeRateSteps(page).openExchangeRatesPage();

        Assert.assertEquals(exchangeRates.getRateCardTitle(currencyCode), "1 " + expected.getName(),
                currencyCode + " card title should match the API currency name");
        Assert.assertEquals(exchangeRates.getRateCardBuyRate(currencyCode), expected.getBuyRate(), RATE_TOLERANCE,
                currencyCode + " buy rate should match the API");
        Assert.assertEquals(exchangeRates.getRateCardSellRate(currencyCode), expected.getSellRate(), RATE_TOLERANCE,
                currencyCode + " sell rate should match the API");
        Assert.assertEquals(exchangeRates.getRateCardOfficialRate(currencyCode), expected.getOfficialCourse(), RATE_TOLERANCE,
                currencyCode + " official rate should match the API");
    }
}
