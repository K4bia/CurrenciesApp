package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ApiClient {
    private static final String BASE_URL = "https://openexchangerates.org/api/latest.json";
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String ACCESS_KEY_PARAM = "?app_id=";

    public String fetchData(String endpoint) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }



    public String getCurrencies() {
        String urlStr = "https://openexchangerates.org/api/currencies.json";
        return fetchData(urlStr);
    }


    public String getExchangeRates(String... symbols) {
        String symbolsParam = String.join(",", symbols);

        String fullUrl = BASE_URL + ACCESS_KEY_PARAM + API_KEY + "&symbols=" + symbolsParam;
        return fetchData(fullUrl);
    }
}
