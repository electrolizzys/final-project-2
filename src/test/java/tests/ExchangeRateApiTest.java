package tests;

import api.models.ExchangeRateResponse;
import steps.ExchangeRateApiSteps;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExchangeRateApiTest {

    @Test(description = "KAN-T22 | Verify the exchange rate API returns valid rates for USD to GEL")
    public void exchangeRateForValidPairReturnsRequestedPairWithValidRates() {
        ExchangeRateResponse rate = new ExchangeRateApiSteps().getExchangeRate("USD", "GEL");

        Assert.assertEquals(rate.getIso1(), "USD", "Response should echo the requested source currency");
        Assert.assertEquals(rate.getIso2(), "GEL", "Response should echo the requested target currency");
        Assert.assertTrue(rate.getBuyRate() > 0, "Buy rate should be positive");
        Assert.assertTrue(rate.getSellRate() > rate.getBuyRate(), "Sell rate should exceed the buy rate");
        Assert.assertNotNull(rate.getUpdateDate(), "Response should say when the rate was last updated");
    }
}
