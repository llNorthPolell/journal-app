FROM openjdk:17-jdk-alpine

RUN addgroup -S app && adduser -S app -G app

USER app

COPY target/*.jar app.jar
EXPOSE 10000
ENTRYPOINT ["java","-jar","/app.jar"]