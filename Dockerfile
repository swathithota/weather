FROM openjdk:17-alpine
EXPOSE 8080

COPY target/weather*.jar weather.jar

ENTRYPOINT [ "java", "-jar", "/weather.jar" ]


