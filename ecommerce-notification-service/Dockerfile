# Usa una imagen base de Eclipse Temurin para Java 21
FROM eclipse-temurin:21-jdk-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR construido de tu aplicación al contenedor
COPY target/ecommerce-notification-service-1.0.0-SNAPSHOT.jar ecommerce-notification-service.jar

# Comando para ejecutar la aplicación Spring Boot cuando el contenedor se inicie
CMD ["java", "-jar", "ecommerce-notification-service.jar"]

