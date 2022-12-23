FROM gradle:7.6.0-jdk17-alpine as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/check-app-1.0.jar check-app-1.0.jar

ENTRYPOINT ["java", "-jar","check-app-1.0.jar"]