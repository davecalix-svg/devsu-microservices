# Usa una imagen base de Java
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Copia el JAR generado por Maven
COPY target/cuenta-service.jar app.jar

# Expone el puerto
EXPOSE 8082

# Comando de ejecución
ENTRYPOINT ["java","-jar","app.jar"]
