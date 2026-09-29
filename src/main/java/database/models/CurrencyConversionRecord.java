package database.models;

/**
 * One row of the currency_conversions table: a currency pair and an amount
 * to drive the converter with. Plain getters/setters, as MyBatis maps into it.
 */
public class CurrencyConversionRecord {

    private int id;
    private String fromCurrency;
    private String toCurrency;
    private int amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return fromCurrency + "->" + toCurrency + " (" + amount + ")";
    }
}
