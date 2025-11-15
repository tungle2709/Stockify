FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/stock-trading-app-1.0.0.jar app.jar

EXPOSE 5000

CMD ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]
