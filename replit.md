# Stock Trading Application

## Overview
A simple stock trading web application built with Spring Boot, featuring Thymeleaf templates for the frontend and RESTful APIs for backend operations. The application allows users to view stocks, buy/sell stocks, track their portfolio, view transaction history, and monitor profit/loss.

## Recent Changes (November 7, 2025)
- Initial project creation with Spring Boot 3.2.0
- Implemented complete stock trading system with 5 pages
- Added RESTful API endpoints for programmatic access
- Integrated Alpha Vantage API for stock price data (with mock fallback)
- Set up H2 in-memory database for data persistence
- Configured initial data with 10 sample stocks and $100,000 starting balance

## Project Architecture

### Technology Stack
- **Backend Framework**: Spring Boot 3.2.0
- **Frontend**: Thymeleaf + Bootstrap 5
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Java Version**: 17

### Application Structure
```
src/main/java/com/stocktrading/
├── StockTradingApplication.java       # Main application entry point
├── model/                              # Domain entities
│   ├── Stock.java                      # Stock entity
│   ├── Portfolio.java                  # Portfolio holdings
│   ├── Transaction.java                # Transaction records
│   ├── Account.java                    # User account
│   └── TransactionType.java            # Buy/Sell enum
├── repository/                         # JPA repositories
│   ├── StockRepository.java
│   ├── PortfolioRepository.java
│   ├── TransactionRepository.java
│   └── AccountRepository.java
├── service/                            # Business logic
│   ├── StockService.java               # Stock operations
│   ├── AlphaVantageService.java        # External API integration
│   ├── TradingService.java             # Trading operations
│   └── PortfolioService.java           # Portfolio calculations
├── controller/                         # Web controllers (Thymeleaf)
│   ├── HomeController.java
│   ├── TradingController.java
│   ├── PortfolioController.java
│   ├── TransactionController.java
│   └── ProfitLossController.java
├── controller/api/                     # REST API controllers
│   ├── StockApiController.java
│   ├── TradingApiController.java
│   ├── PortfolioApiController.java
│   └── TransactionApiController.java
└── config/
    └── DataInitializer.java            # Database initialization

src/main/resources/
├── application.properties              # Configuration
└── templates/                          # Thymeleaf HTML templates
    ├── stocks.html
    ├── trading.html
    ├── portfolio.html
    ├── transactions.html
    └── profit-loss.html
```

## Features

### Web Pages (Thymeleaf UI)
1. **Stocks Page** (`/stocks`) - View all available stocks with current prices and changes
2. **Trading Page** (`/trading`) - Buy and sell stocks with account balance display
3. **Portfolio Page** (`/portfolio`) - View holdings, current value, and gains/losses
4. **Transaction History** (`/transactions`) - Complete history of all trades
5. **Profit/Loss Dashboard** (`/profit-loss`) - Revenue metrics and performance analytics

### RESTful API Endpoints
- `GET /api/stocks` - Get all stocks
- `GET /api/stocks/{symbol}` - Get stock by symbol
- `POST /api/stocks/refresh` - Refresh all stock prices
- `POST /api/trading/buy` - Buy stock (JSON body: {symbol, quantity})
- `POST /api/trading/sell` - Sell stock (JSON body: {symbol, quantity})
- `GET /api/portfolio` - Get portfolio holdings
- `GET /api/portfolio/account` - Get account summary
- `GET /api/transactions` - Get all transactions
- `GET /api/transactions/{symbol}` - Get transactions for specific stock

## Configuration

### Application Properties
- **Server Port**: 5000
- **Database**: H2 in-memory (jdbc:h2:mem:stockdb)
- **Initial Cash Balance**: $100,000
- **H2 Console**: Enabled at `/h2-console`

### Stock Data
The application uses Alpha Vantage API for real-time stock prices. If the API is unavailable or rate-limited, it falls back to mock price generation based on stock symbols.

API Key configuration: `alpha.vantage.api.key` in application.properties (currently set to "demo")

### Sample Stocks
The application initializes with 10 stocks:
- AAPL (Apple Inc.)
- GOOGL (Alphabet Inc.)
- MSFT (Microsoft Corporation)
- AMZN (Amazon.com Inc.)
- TSLA (Tesla Inc.)
- META (Meta Platforms Inc.)
- NVDA (NVIDIA Corporation)
- JPM (JPMorgan Chase & Co.)
- V (Visa Inc.)
- WMT (Walmart Inc.)

## Running Locally

### Prerequisites
- Java 17 or higher
- Maven

### Build and Run
```bash
# Clean and build
mvn clean package

# Run the application
mvn spring-boot:run

# Or run the JAR
java -jar target/stock-trading-app-1.0.0.jar
```

### Access the Application
- **Web UI**: http://localhost:5000
- **H2 Console**: http://localhost:5000/h2-console
- **API Base URL**: http://localhost:5000/api

## Database Schema

### Tables
- **account** - User account with cash balance and portfolio metrics
- **stocks** - Available stocks with prices and metadata
- **portfolio** - User's stock holdings
- **transactions** - Complete trade history

## Future Enhancements
- User authentication and multi-user support
- Real-time price updates with WebSocket
- Advanced charting and technical analysis
- Watchlist functionality
- Export functionality for tax reporting
- Integration with real trading APIs
