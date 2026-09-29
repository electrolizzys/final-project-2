package constants;

import utils.ConfigReader;

public final class UrlConstants {

    private UrlConstants() {
    }

    public static final String BASE_URL = ConfigReader.baseUrl();
    public static final String HOME_EN = BASE_URL + "/en";
    public static final String LOCATIONS_EN = BASE_URL + "/en/atms&branches";
    public static final String MORTGAGE_LOAN_EN = BASE_URL + "/en/loans/mortgage";
    public static final String EXCHANGE_RATE_EN = BASE_URL + "/en/valutis-kursi/USD-to-GEL?amount=100";
    public static final String INSTANT_TRANSFERS_EN = BASE_URL + "/en/instant-transfers";
}
