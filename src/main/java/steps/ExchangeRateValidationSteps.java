package steps;

import api.clients.ExchangeRateApiClient;
import api.models.ValidationErrorResponse;

/**
 * Negative flow: request a rate with the target currency (Iso2) left out,
 * check the API rejects it with 400, and hand back the validation error body.
 */
public class ExchangeRateValidationSteps {

    private final ExchangeRateApiClient client = new ExchangeRateApiClient();

    public ValidationErrorResponse getExchangeRateWithoutTargetCurrency(String sourceCurrency) {
        return client.getExchangeRate(sourceCurrency, null)
                .then()
                .statusCode(400)
                .extract()
                .as(ValidationErrorResponse.class);
    }
}
