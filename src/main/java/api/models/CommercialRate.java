package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * One currency in the commercialList response, e.g.
 * { "iso": "USD", "name": "U.S. Dollar", "buyRate": 2.553, "sellRate": 2.643, "officialCourse": 2.608 }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommercialRate {

    private String iso;
    private String name;
    private double buyRate;
    private double sellRate;
    private double officialCourse;

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getOfficialCourse() {
        return officialCourse;
    }

    public void setOfficialCourse(double officialCourse) {
        this.officialCourse = officialCourse;
    }
}
