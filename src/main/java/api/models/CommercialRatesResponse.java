package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * 200 response of commercialList: { "rates": [ { "iso", "name", "buyRate", ... }, ... ] }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommercialRatesResponse {

    private List<CommercialRate> rates;

    public List<CommercialRate> getRates() {
        return rates;
    }

    public void setRates(List<CommercialRate> rates) {
        this.rates = rates;
    }
}
