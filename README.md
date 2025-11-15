# Stockify - Stock Trading Application

A Spring Boot web application for stock trading and portfolio management.

## Project Structure

```
├── src/                    # Source code
├── deployment/            # Deployment scripts
├── docker/               # Docker configuration
├── scripts/              # AWS management scripts
├── docs/                 # Documentation
├── build/                # Build artifacts
├── target/               # Maven build output
└── pom.xml              # Maven configuration
```

## Quick Start

### Local Development
```bash
mvn spring-boot:run
```

### Docker Deployment
```bash
cd docker
docker-compose up -d
```

### AWS Deployment
```bash
./deployment/deploy-final.sh
```

## Access
- Local: http://localhost:5000
- Docker: http://localhost:5000
- AWS: http://your-ec2-ip:5000

## Features
- Stock price tracking
- Portfolio management
- Trading simulation
- Profit/Loss analysis
- Transaction history
