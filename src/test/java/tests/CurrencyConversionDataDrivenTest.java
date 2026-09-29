package tests;

import database.MyBatisSessionFactory;
import database.mappers.CurrencyConversionMapper;
import database.models.CurrencyConversionRecord;
import pages.ExchangeRatePage;
import steps.CurrencyConversionSteps;
import org.apache.ibatis.session.SqlSession;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Runs the same conversion check against every currency pair stored in the
 * local H2 database (via MyBatis) - a new row means a new pair covered,
 * with no new test method required.
 */
public class CurrencyConversionDataDrivenTest extends BaseTest {

    @DataProvider(name = "currencyConversions")
    public Object[][] currencyConversions() {
        try (SqlSession session = MyBatisSessionFactory.openSession()) {
            List<CurrencyConversionRecord> records = session.getMapper(CurrencyConversionMapper.class).selectAll();
            Object[][] data = new Object[records.size()][1];
            for (int i = 0; i < records.size(); i++) {
                data[i][0] = records.get(i);
            }
            return data;
        }
    }

    @Test(dataProvider = "currencyConversions",
            description = "KAN-T21 | Verify currency conversion math for each database-driven currency pair")
    public void conversionScalesWithAmount(CurrencyConversionRecord record) {
        CurrencyConversionSteps steps = new CurrencyConversionSteps(page);
        ExchangeRatePage exchangeRate = steps.openConversionPage(record);

        Assert.assertEquals(Double.parseDouble(exchangeRate.getSellAmount()), record.getAmount(), 0.0001,
                "Sell amount field should be pre-filled with the database amount for " + record);

        double convertedForBaseAmount = Double.parseDouble(exchangeRate.getBuyAmount());
        Assert.assertTrue(convertedForBaseAmount > 0,
                "Converted amount should be positive for " + record);

        double convertedForDoubledAmount = Double.parseDouble(steps.doubleSellAmount(exchangeRate, record));

        double expectedDoubled = convertedForBaseAmount * 2;
        double tolerance = expectedDoubled * 0.01;
        Assert.assertTrue(Math.abs(convertedForDoubledAmount - expectedDoubled) <= tolerance,
                String.format("Doubling the sell amount should roughly double the converted value for %s (expected ~%.2f, got %.2f)",
                        record, expectedDoubled, convertedForDoubledAmount));
    }
}
