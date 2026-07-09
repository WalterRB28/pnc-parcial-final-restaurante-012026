# Stage 1: Build
FROM gradle:8.4-jdk21 AS builder

WORKDIR /app

# Copiar archivos de build
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle/ ./gradle/

# Copiar código fuente
COPY src/ ./src/

# Compilar y empaquetar (skip tests para build rápido en Docker)
RUN ./gradlew build -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar JAR desde builder
COPY --from=builder /app/build/libs/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Configurar timezone (opcional)
ENV TZ=America/El_Salvador

# Comando para ejecutar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]