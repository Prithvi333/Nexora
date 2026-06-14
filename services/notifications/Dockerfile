FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/notifications.jar /app/notifications.jar
EXPOSE 8087
ENTRYPOINT [ "java","-jar","notifications.jar" ]