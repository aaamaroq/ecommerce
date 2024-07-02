# Usa una imagen base de OpenJDK para Java 17
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR construido de tu aplicación al contenedor
COPY target/ApiECommerce-0.0.1-SNAPSHOT.jar ApiECommerce.jar

# Expone el puerto en el que tu aplicación Spring Boot escucha (ajústalo según sea necesario)
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot cuando el contenedor se inicie
CMD ["java", "-jar", "ApiECommerce.jar"]