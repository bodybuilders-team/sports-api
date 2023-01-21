FROM gradle:7.2.0-jdk17 AS build

WORKDIR /usr/app
COPY src ./src
COPY static-content ./static-content
COPY gradle ./gradle

RUN gradle build && \
    gradle jar

FROM openjdk:17

WORKDIR /usr/app
COPY --from=build /usr/app/ .

EXPOSE 8888
CMD ["java", "-jar", "./build/libs/app.jar"]
