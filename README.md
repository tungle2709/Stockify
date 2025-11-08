# Stockify

A robust stock trading application that provides real-time market data and trading capabilities.

## Tech Stack

### Backend
- Java Spring Boot
- Maven

### Database
- Amazon RDS (PostgreSQL)

### Infrastructure
- Amazon EC2 - Application hosting
- Amazon RDS - Database service

## Features
- Real-time stock data
- User authentication
- Portfolio management
- Trade execution
- Market analysis tools

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- AWS Account for EC2 and RDS services

### Installation
1. Clone the repository
```bash
git clone https://github.com/tungle2709/Stockify.git
```

2. Navigate to project directory
```bash
cd Stockify
```

3. Build the project
```bash
mvn clean install
```

4. Run the application
```bash
mvn spring-boot:run
```

## Configuration
- Configure your AWS credentials for EC2 and RDS access
- Update `application.properties` with your database credentials

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
