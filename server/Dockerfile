# --- Build stage ---
FROM gradle:8.9-jdk21 AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle --no-daemon fatJar

# --- Runtime stage ---
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/*-all.jar /app/server.jar

# Persisted data (SQLite db) and generated/provided TLS keystore.
RUN mkdir -p /app/data /app/certs
VOLUME ["/app/data", "/app/certs"]

ENV PORT=8080 \
    SSL_PORT=8443 \
    DB_PATH=/app/data/sync.db \
    KEYSTORE_PATH=/app/certs/keystore.jks \
    KEY_ALIAS=droidpasswords \
    KEYSTORE_PASSWORD=changeit \
    KEY_PASSWORD=changeit \
    CERT_DOMAINS=localhost

EXPOSE 8080 8443

ENTRYPOINT ["java", "-jar", "/app/server.jar"]
