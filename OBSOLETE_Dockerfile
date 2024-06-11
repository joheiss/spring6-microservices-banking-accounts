FROM openjdk:21-slim

WORKDIR /app

COPY target/accounts-0.0.1-SNAPSHOT.jar ./accounts-0.0.1-SNAPSHOT.jar

EXPOSE 8091

ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]
