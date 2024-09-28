package entities;

import java.time.LocalDateTime;

public class Conversion {
    private final Currency fromCurrency;
    private final Currency toCurrency;
    private final double amount;
    private final double convertedAmount;
    private final LocalDateTime timestamp;

    public Conversion(Currency fromCurrency, Currency toCurrency, double amount, double convertedAmount) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
        this.timestamp = LocalDateTime.now();
    }


    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Currency getFromCurrency() {
        return this.fromCurrency;
    }

    public Double getConvertedAmount() {
        return this.convertedAmount;
    }

    public Currency getToCurrency() {
        return this.toCurrency;
    }
}
