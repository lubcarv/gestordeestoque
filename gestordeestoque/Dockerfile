# ---------- Stage 1: build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B package -DskipTests

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/target/gestordeestoque-0.0.1-SNAPSHOT.jar app.jar

# Variável de ambiente para a porta
ENV PORT=8080

# Configurações de JVM otimizadas para Render Free Tier (512MB RAM)
ENTRYPOINT ["java", \
    "-Xmx400m", \
    "-Xms200m", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Dspring.profiles.active=prod", \
    "-Dserver.port=${PORT}", \
    "-jar", "app.jar"]