FROM maven:3-eclipse-temurin-17

WORKDIR /app

COPY ./TimeClient/TimeClient/pom.xml .
RUN mvn dependency:go-offline

COPY ./TimeClient/TimeClient .



ENTRYPOINT ["mvn", "package"]