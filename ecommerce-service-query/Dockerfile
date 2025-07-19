FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/ecommerce-service-query-1.0.0-SNAPSHOT.jar ecommerce-service-query.jar

EXPOSE 8080

CMD ["java", "-jar", "ecommerce-service-query.jar"]