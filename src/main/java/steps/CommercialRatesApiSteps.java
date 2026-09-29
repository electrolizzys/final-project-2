package steps;

import api.clients.ExchangeRateApiClient;
import api.models.CommercialRate;
import api.models.CommercialRatesResponse;

/**
 * Fetches the commercial rates list - the same data the exchange rates page
 * renders - and returns the entry for one currency, to compare against the UI.
 */
public class CommercialRatesApiSteps {

    private final ExchangeRateApiClient client = new ExchangeRateApiClient();

    public CommercialRate getCommercialRate(String currencyCode) {
        CommercialRatesResponse response = client.getCommercialRates()
                .then()
                .statusCode(200)
                .extract()
                .as(CommercialRatesResponse.class);
        return response.getRates().stream()
                .filter(rate -> currencyCode.equals(rate.getIso()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(currencyCode + " is missing from the commercial rates API response"));
    }
}
