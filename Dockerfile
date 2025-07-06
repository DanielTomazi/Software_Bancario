FROM openjdk:17-jdk-slim

LABEL maintainer="Sistema Bancario <dev@banco.com>"

WORKDIR /app

# Instalar dependências necessárias
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copiar arquivos Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Dar permissão de execução ao Maven Wrapper
RUN chmod +x mvnw

# Baixar dependências
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src src

# Compilar aplicação
RUN ./mvnw package -DskipTests

# Expor porta
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Executar aplicação
CMD ["java", "-jar", "target/sistema-bancario-1.0.0.jar"]
