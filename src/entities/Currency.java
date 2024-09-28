package entities;


public record Currency(String symbol, String currency) {
    public String formatted() {
        return String.format("Símbolo: %s, Moneda: %s", symbol, currency);
    }
}

