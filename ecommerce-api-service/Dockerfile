FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/ecommerce-api-service-1.0.0-SNAPSHOT.jar ecommerce-api-service.jar

EXPOSE 8080

CMD ["java", "-jar", "ecommerce-api-service.jar"]