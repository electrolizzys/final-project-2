package tests;

import api.models.CommercialRate;
import constants.TestDataConstants;
import pages.ExchangeRatePage;
import steps.CommercialRatesApiSteps;
import steps.ExchangeRateSteps;
import utils.TestDataProviders;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * API -> UI consistency: the commercial rates API is the source of truth, and
 * each popular currency's card on the exchange rates page must show the same
 * name, buy rate, sell rate and official rate.
 */
public class ExchangeRateApiToUiTest extends BaseTest {

    @Test(dataProvider = TestDataProviders.POPULAR_CURRENCIES, dataProviderClass = TestDataProviders.class,
            description = "KAN-T24 | Verify the exchange rates page shows the same rates as the commercial rates API")
    public void rateCardMatchesCommercialRatesApi(String currencyCode) {
        CommercialRate expected = new CommercialRatesApiSteps().getCommercialRate(currencyCode);

        ExchangeRatePage exchangeRates = new ExchangeRateSteps(page).openExchangeRatesPage();

        Assert.assertEquals(exchangeRates.getRateCardTitle(currencyCode), "1 " + expected.getName(),
                currencyCode + " card title should match the API currency name");
        Assert.assertEquals(exchangeRates.getRateCardBuyRate(currencyCode), expected.getBuyRate(), TestDataConstants.RATE_TOLERANCE,
                currencyCode + " buy rate should match the API");
        Assert.assertEquals(exchangeRates.getRateCardSellRate(currencyCode), expected.getSellRate(), TestDataConstants.RATE_TOLERANCE,
                currencyCode + " sell rate should match the API");
        Assert.assertEquals(exchangeRates.getRateCardOfficialRate(currencyCode), expected.getOfficialCourse(), TestDataConstants.RATE_TOLERANCE,
                currencyCode + " official rate should match the API");
    }
}
