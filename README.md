# CurrencyApp
This is a Java-based application that provides real-time currency conversion and allows users to view detailed information about various currencies. It interacts with a currency exchange rate API to retrieve current exchange rates and supports features such as conversion history management and data persistence.

## Features

- **Currency Conversion**: Convert any amount from one currency to another using real-time exchange rates.
- **Currency Information**: Search and display information about currencies based on their symbol or country name.
- **Conversion History**: View a detailed history of all conversions performed, including timestamps and converted amounts.
- **Save History to File**: Users can save their conversion history to a text file for later reference.
- **API Integration**: Fetches up-to-date exchange rates from a currency API, ensuring accurate conversions.
  
## Technologies Used

- **Java**: Core programming language.
- **JSON**: Used for parsing API responses.
- **API Integration**: Real-time exchange rates are retrieved from a currency API using HTTP requests.
- **File I/O**: Saves conversion history to a local text file.

## Libraries

The project uses two main libraries for handling JSON objects and API data:

- **Gson**: Used for converting Java objects to JSON and vice versa.
- **org.json**: A library for handling JSON objects and parsing the API responses.


## How to Use

1. Clone the repository:
   git clone https://github.com/K4bia/CurrencyApp/.git
2. Import the project into your favorite Java IDE (e.g., IntelliJ, Eclipse).

3. Add the following libraries to your project:
   - Gson: `com.google.code.gson:gson:2.8.9`
   - JSON: `org.json:json:20210307`
4. Configure your API key and base URL in the `ApiClient` class.

## Usage

1. Run the project.
2. Use the menu to:
   - List available currencies
   - Convert between currencies
   - View conversion history
   - Save the conversion history to a file

Future Improvements
    Implement database storage for the conversion history.
    Add support for multiple base currencies.
    Internationalization to support different languages for currency information display.
    
