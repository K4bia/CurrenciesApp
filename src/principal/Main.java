package principal;
import services.*;

public class Main {
    public static void main(String[] args) {
        ApiClient apiClient = new ApiClient();
        CurrencyService currencyService = new CurrencyService(apiClient);
        Menu menu = new Menu(currencyService);
        menu.showMenu();

    }
}