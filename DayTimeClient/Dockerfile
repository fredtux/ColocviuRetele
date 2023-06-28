FROM maven:3-eclipse-temurin-17

WORKDIR /app

COPY ./DayTimeClient/DayTimeClient/pom.xml .
RUN mvn dependency:go-offline

COPY ./DayTimeClient/DayTimeClient .


RUN mvn package

# RUN mkdir -p /app/compiled_jars
ENTRYPOINT ["cp", "/app/target/DayTimeClient-1.0-SNAPSHOT-jar-with-dependencies.jar", "/app/compiled_jars/DayTimeClient.jar"]