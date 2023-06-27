FROM maven:3-eclipse-temurin-17

WORKDIR /app

COPY ./DayTimeServer/DayTimeServer/pom.xml .
RUN mvn dependency:go-offline

COPY ./DayTimeServer/DayTimeServer .

RUN mvn package

ENTRYPOINT ["java", "-jar", "target/DayTimeServer-1.0-SNAPSHOT-jar-with-dependencies.jar"]