package steps;

import api.clients.ExchangeRateApiClient;
import api.models.ExchangeRateResponse;

/**
 * Happy-path flow: request the rate for a valid currency pair, check the
 * request succeeded, and hand back the deserialized body for assertions.
 */
public class ExchangeRateApiSteps {

    private final ExchangeRateApiClient client = new ExchangeRateApiClient();

    public ExchangeRateResponse getExchangeRate(String sourceCurrency, String targetCurrency) {
        return client.getExchangeRate(sourceCurrency, targetCurrency)
                .then()
                .statusCode(200)
                .extract()
                .as(ExchangeRateResponse.class);
    }
}
