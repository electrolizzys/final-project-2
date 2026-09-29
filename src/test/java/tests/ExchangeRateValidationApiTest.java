package tests;

import api.models.ValidationErrorResponse;
import steps.ExchangeRateValidationSteps;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class ExchangeRateValidationApiTest {

    @Test(description = "Verify the exchange rate API rejects a request missing the target currency")
    public void exchangeRateWithoutTargetCurrencyReturnsValidationError() {
        ValidationErrorResponse error = new ExchangeRateValidationSteps().getExchangeRateWithoutTargetCurrency("USD");

        Assert.assertEquals(error.getStatus(), 400, "Error body should repeat the 400 status");
        Assert.assertNotNull(error.getTitle(), "Error should have a title");

        List<String> iso2Messages = error.getErrors().get("Iso2");
        Assert.assertNotNull(iso2Messages, "Validation errors should name the missing Iso2 parameter");
        Assert.assertFalse(iso2Messages.isEmpty(), "Iso2 should have at least one validation message");
        Assert.assertTrue(iso2Messages.get(0).contains("Iso2"),
                "Validation message should refer to Iso2, got: " + iso2Messages.get(0));
    }
}
