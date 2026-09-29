package utils;

import constants.UrlConstants;
import database.CurrencyConversionRepository;
import database.models.CurrencyConversionRecord;
import org.testng.annotations.DataProvider;

import java.util.List;

/**
 * All TestNG data providers, referenced from tests via
 * {@code dataProviderClass = TestDataProviders.class}, so test classes hold
 * only test methods.
 */
public final class TestDataProviders {

    public static final String LOCALES = "locales";
    public static final String CURRENCY_CONVERSIONS = "currencyConversions";
    public static final String POPULAR_CURRENCIES = "popularCurrencies";

    private TestDataProviders() {
    }

    /**
     * Localization journey: each row starts in the *other* locale and switches to
     * the target one, followed by the localized texts expected along the journey.
     * Adding a locale means adding a row here.
     */
    @DataProvider(name = LOCALES)
    public static Object[][] locales() {
        return new Object[][]{
                {
                        UrlConstants.BASE_URL + "/ka",
                        "en",
                        "Manage your company's finances remotely",
                        "Startup plan",
                        "Free internet and mobile banking"
                },
                {
                        UrlConstants.BASE_URL + "/en",
                        "ka",
                        "მართეთ კომპანიის ფინანსები დისტანციურად",
                        "სტარტაპ ნაკრები",
                        "უფასო ინტერნეტ და მობაილ ბანკი"
                }
        };
    }

    /**
     * One row per record in the local H2 database, read through MyBatis:
     * a new INSERT in testdata/data.sql becomes a new test run.
     */
    @DataProvider(name = CURRENCY_CONVERSIONS)
    public static Object[][] currencyConversions() {
        List<CurrencyConversionRecord> records = CurrencyConversionRepository.findAll();
        Object[][] data = new Object[records.size()][1];
        for (int i = 0; i < records.size(); i++) {
            data[i][0] = records.get(i);
        }
        return data;
    }

    /** Currencies shown as cards on the exchange rates page, checked against the API. */
    @DataProvider(name = POPULAR_CURRENCIES)
    public static Object[][] popularCurrencies() {
        return new Object[][]{{"USD"}, {"EUR"}, {"GBP"}};
    }
}
