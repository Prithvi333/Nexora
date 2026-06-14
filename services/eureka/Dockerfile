FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/eureka.jar /app/eureka.jar
EXPOSE 8081
ENTRYPOINT [ "java","-jar","eureka.jar" ]