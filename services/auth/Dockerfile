FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/auth.jar /app/auth.jar
EXPOSE 8082
ENTRYPOINT [ "java","-jar","auth.jar" ]