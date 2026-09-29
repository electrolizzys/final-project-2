package tests;

import pages.ExchangeRatePage;
import steps.ExchangeRateSteps;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExchangeRateTest extends BaseTest {

    @Test(description = "KAN-T18 | Convert USD to GEL using the commercial exchange rate")
    public void changingSellAmountUpdatesConvertedValue() {
        ExchangeRateSteps steps = new ExchangeRateSteps(page);
        ExchangeRatePage exchangeRate = steps.navigateToExchangeRateViaHomepage();
        String convertedBefore = exchangeRate.getBuyAmount();

        String convertedAfter = steps.changeSellAmount(exchangeRate, "500");

        Assert.assertNotEquals(convertedAfter, convertedBefore,
                "Converted amount should update when the entered sell amount changes");
        double convertedValue = Double.parseDouble(convertedAfter);
        Assert.assertTrue(convertedValue > 0, "Converted amount should be a positive number");
    }
}
