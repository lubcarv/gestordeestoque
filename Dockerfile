# ===================================
# Stage 1: Build com Maven
# ===================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /build

# Copiar pom.xml primeiro (para cache de dependências)
COPY gestordeestoque/gestordeestoque/pom. xml.

# Baixar dependências (cache)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY gestordeestoque/gestordeestoque/src ./src

# Build da aplicação
RUN mvn clean package -DskipTests -B

# ===================================
# Stage 2: Runtime
# ===================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar o JAR compilado
COPY --from=build /build/target/gestordeestoque-0.0.1-SNAPSHOT.jar app.jar

# Expor porta
EXPOSE 8080

# Configurações de JVM para produção
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando de execução
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]