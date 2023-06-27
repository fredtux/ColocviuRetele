FROM maven:3-eclipse-temurin-17

WORKDIR /app

COPY ./DayTimeClient/DayTimeClient/pom.xml .
RUN mvn dependency:go-offline

COPY ./DayTimeClient/DayTimeClient .



ENTRYPOINT ["mvn", "package"]