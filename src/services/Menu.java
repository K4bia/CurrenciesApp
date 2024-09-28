package services;

import services.CurrencyService.CurrencyConversionException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private final CurrencyService currencyService;
    private final Scanner sc = new Scanner(System.in);

    public Menu(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public void showMenu() {
        boolean continuar = false;
        while (!continuar) {
            try {
                showOptions();
                int opc = getUserOption();
                processOption(opc);
                if (opc == 0) continuar = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, intente nuevamente.");
                sc.next();
            } catch (CurrencyConversionException e) {
                System.out.println("Error al convertir la divisa: " + e.getMessage());
            } catch (CurrencyService.CurrencyNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showOptions() {
        System.out.println("BIENVENIDO AL CONVERSOR DE DIVISAS:");
        System.out.println("Ingrese alguna de las opciones:");
        System.out.println("1. Lista de divisas/símbolos disponibles para consultar.");
        System.out.println("2. Buscar por símbolo o moneda.");
        System.out.println("3. Convertir divisas.");
        System.out.println("4. Obtener lista de conversiones.");
        System.out.println("0. Salir");
    }

    private int getUserOption() {
        while (!sc.hasNextInt()) {
            System.out.println("Opción no válida. Por favor ingrese un número.");
            sc.next();
        }
        return sc.nextInt();
    }

    private void processOption(int opc) throws CurrencyConversionException, CurrencyService.CurrencyNotFoundException {
        switch (opc) {
            case 1:
                currencyService.listCurrencyInfo();
                break;
            case 2:
                processSearch();
                break;
            case 3:
                processConversion();
                break;
            case 4:
                processHistory();
                break;
            case 0:
                System.out.println("Muchas gracias por usar nuestro servicio. Hasta luego");
                break;
            default:
                System.out.println("Opción no válida. Intente nuevamente");
        }
    }

    private void processSearch() throws CurrencyService.CurrencyNotFoundException {
        System.out.println("Ingrese un símbolo o país para la búsqueda:");
        String busqueda = sc.next();
        currencyService.findCurrencyInfo(busqueda);
    }

    private void processConversion() throws CurrencyConversionException {
        System.out.println("Ingrese el símbolo de la divisa base. (ARS/EUR/BRL/USD)");
        String base = sc.next().toUpperCase();
        System.out.println("Ingrese el símbolo de la divisa a la que desea convertir. (ARS/EUR/BRL/USD)");
        String result = sc.next().toUpperCase();

        System.out.println("Ingrese el monto");
        double amount = sc.nextDouble();
        double convertedAmount = currencyService.convertCurrency(base, result, amount);
        System.out.println(amount + " " + base + " equivalen a: " +
                String.format("%.2f", convertedAmount) + " " + result);
    }

    private void processHistory() {
        System.out.println(currencyService.getConversionHistory());
        System.out.println("Desea guardar una copia del historial (S/N)");
        String confirm = sc.next().trim().toUpperCase();
        if (confirm.equals("S")) {
            currencyService.saveHistoryToFile("historial_conversiones.txt");
        } else if (!confirm.equals("N")) {
            System.out.println("Opción no válida. Intente nuevamente.");
        }
    }


}
