package services;

import entities.Conversion;
import entities.Currency;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CurrencyService {
    private final ApiClient apiClient;
    private final Map<String, Currency> currencyMap;

    public CurrencyService(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.currencyMap = new HashMap<>();
        loadCurrencies();
    }

    // Carga el mapa de divisas disponibles en la API

    private void loadCurrencies() {
        String jsonResponse = apiClient.getCurrencies();
        JSONObject jsonObject = new JSONObject(jsonResponse);

        for (String key : jsonObject.keySet()) {
            String currency = jsonObject.getString(key);
            currencyMap.put(key, new Currency(key, currency));
        }
    }

    public void listCurrencyInfo() {
        Map<String, Currency> sortedCurrencyMap = new TreeMap<>(currencyMap);
        for (Map.Entry<String, Currency> entry : sortedCurrencyMap.entrySet()) {
            System.out.println(entry.getValue().formatted());
        }
    }

    public void findCurrencyInfo(String symbolsOrCountries) {
        String[] inputArray = symbolsOrCountries.split(",");
        for (String input : inputArray) {
            input = input.trim().toLowerCase();
            boolean found = false;

            // Buscar por símbolo
            if (currencyMap.containsKey(input.toUpperCase())) {
                double price = getPrice(input.toUpperCase());
                System.out.println(currencyMap.get(input.toUpperCase()).formatted() + " - Cotizacion actual (BASE USD): " + price);
                found = true;
            }

            // Buscar por moneda
            if (!found) {
                for (Map.Entry<String, Currency> entry : currencyMap.entrySet()) {
                    Currency currency = entry.getValue();
                    if (currency.currency().toLowerCase().contains(input)) {
                        double price = getPrice(entry.getKey());
                        System.out.println(currency.formatted()+" - Cotizacion actual (BASE USD): "+price);
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                System.out.println("Divisa o país no encontrado para: " + input);
            }
        }
    }

    public double getPrice(String symbol) {
        try {
            String response = apiClient.getExchangeRates(symbol);
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject rates = jsonResponse.getJSONObject("rates");

            return rates.getDouble(symbol);
        } catch (JSONException e) {
            System.err.println("Error procesando la respuesta de la API: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al obtener el precio de la divisa: " + e.getMessage());
        }
        return 0;
    }

    private final List<Conversion> conversionHistory = new ArrayList<>();

public double convertCurrency(String from, String to, double amount) throws CurrencyConversionException {
    try {
        double convertedAmount = performConversion(from, to, amount);
        if (convertedAmount == 0) {
            throw new CurrencyConversionException("Error al convertir divisas");
        }
        saveConversion(from, to, amount, convertedAmount);
        return convertedAmount;
    } catch (RuntimeException e) {
        System.out.println("Error al obtener la informacion para el criterio utilzado.");
    }
    return 0;

}

    private double performConversion(String from, String to, double amount) {
        try {
            String response = apiClient.getExchangeRates(from, to);
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject rates = jsonResponse.getJSONObject("rates");

            double fromRate = rates.getDouble(from);
            double toRate = rates.getDouble(to);

            return (amount / fromRate) * toRate;
        } catch (JSONException e) {
            System.err.println("Error procesando la respuesta de la API: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al convertir divisas: " + e.getMessage());
        }
        return 0;
    }


    private void saveConversion(String from, String to, double amount, double convertedAmount) {
        Currency fromCurrency = new Currency(from, getCurrencyForSymbol(from));
        Currency toCurrency = new Currency(to, getCurrencyForSymbol(to));
        Conversion conversion = new Conversion(fromCurrency, toCurrency, amount, convertedAmount);
        conversionHistory.add(conversion);
    }

    private String getCurrencyForSymbol(String symbol) {
        Currency currency = currencyMap.get(symbol);
        if (currency != null) {
            return currency.currency();
        } else {
            return "Símbolo no encontrado";
        }
    }

    public List<Conversion> getConversionHistory() {
        for (Conversion conversion : conversionHistory) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            System.out.println("Conversión realizada el " + conversion.getTimestamp().format(formatter));
            System.out.println("Cantidad: " + conversion.getAmount() + " " + conversion.getFromCurrency().symbol() +
                    " (" + conversion.getFromCurrency().currency() + ")");
            System.out.println("Convertido a: " + conversion.getConvertedAmount() + " " + conversion.getToCurrency().symbol() +
                    " (" + conversion.getToCurrency().currency() + ")");
            System.out.println("--------------------------------------------");
        }
        return conversionHistory;
    }

    public void saveHistoryToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Conversion conversion : conversionHistory) {
                writer.write(formatConversion(conversion));
                writer.newLine();
            }
            System.out.println("Historial de conversiones guardado en: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al guardar el historial de conversiones: " + e.getMessage());
        }
    }

    private String formatConversion(Conversion conversion) {
        return String.format("Conversión realizada el %s: %.2f %s (%s) a %.2f %s (%s)",
                conversion.getTimestamp(),
                conversion.getAmount(), conversion.getFromCurrency().symbol(), conversion.getFromCurrency().currency(),
                conversion.getConvertedAmount(), conversion.getToCurrency().symbol(), conversion.getToCurrency().currency());
    }


    public static class CurrencyConversionException extends Exception {
        public CurrencyConversionException(String message) {
            super(message);
        }
    }

    public static class CurrencyNotFoundException extends Exception {
        public CurrencyNotFoundException(String message) {
            super(message);
        }
    }
}
