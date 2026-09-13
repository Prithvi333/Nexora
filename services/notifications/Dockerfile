FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/notifications.jar /app/notifications.jar
EXPOSE 8087
ENTRYPOINT [ "java","-jar","notifications.jar" ]