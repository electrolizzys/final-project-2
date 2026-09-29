package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 200 response of getExchangeRate, e.g.
 * { "iso1": "USD", "iso2": "GEL", "buyRate": 2.553, "sellRate": 2.643, "updateDate": "..." }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse {

    private String iso1;
    private String iso2;
    private double buyRate;
    private double sellRate;
    private String updateDate;

    public String getIso1() {
        return iso1;
    }

    public void setIso1(String iso1) {
        this.iso1 = iso1;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public double getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(double buyRate) {
        this.buyRate = buyRate;
    }

    public double getSellRate() {
        return sellRate;
    }

    public void setSellRate(double sellRate) {
        this.sellRate = sellRate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
