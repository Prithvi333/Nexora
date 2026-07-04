FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/users.jar /app/users.jar
EXPOSE 8083
ENTRYPOINT [ "java","-jar","users.jar" ]