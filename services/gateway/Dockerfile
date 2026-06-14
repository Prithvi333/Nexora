FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/gateway.jar /app/gateway.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","gateway.jar" ]