FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=target/churncheck-api-0.0.1.jar
COPY ${JAR_FILE} churncheck-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "churncheck-api.jar"]