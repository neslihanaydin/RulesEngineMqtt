FROM openjdk:17-jdk-slim

LABEL maintainer="naydin43" \
      description="RulesEngine project with MQTT and Drools integration."

WORKDIR /app

COPY target/WSRulesEngine-1.0-SNAPSHOT-jar-with-dependencies.jar /app/RulesEngine.jar

EXPOSE 1883
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "RulesEngine.jar"]