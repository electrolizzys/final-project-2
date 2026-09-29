package api.clients;

import utils.ConfigReader;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Client for the exchange-rate endpoints that tbcbank.ge's exchange rates page
 * calls. They are public (no API key); the site loads them for every visitor.
 */
public class ExchangeRateApiClient {

    private static final String EXCHANGE_RATE_PATH = "/api/v1/exchangeRates/getExchangeRate";
    private static final String COMMERCIAL_LIST_PATH = "/api/v1/exchangeRates/commercialList";

    public Response getCommercialRates() {
        return given()
                .baseUri(ConfigReader.apiBaseUrl())
                .accept("application/json")
                .queryParam("locale", "en-US")
                .when()
                .get(COMMERCIAL_LIST_PATH);
    }

    /**
     * targetCurrency may be null to omit the Iso2 parameter entirely.
     */
    public Response getExchangeRate(String sourceCurrency, String targetCurrency) {
        RequestSpecification request = given()
                .baseUri(ConfigReader.apiBaseUrl())
                .accept("application/json")
                .queryParam("Iso1", sourceCurrency);
        if (targetCurrency != null) {
            request = request.queryParam("Iso2", targetCurrency);
        }
        return request.when().get(EXCHANGE_RATE_PATH);
    }
}
