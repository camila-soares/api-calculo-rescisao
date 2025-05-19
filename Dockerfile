FROM eclipse-temurin:17
COPY ./target/api-calculo-rescisao-0.0.1-SNAPSHOT.jar /app/api-calculo-rescisao-0.0.1-SNAPSHOT.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "api-calculo-rescisao-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080