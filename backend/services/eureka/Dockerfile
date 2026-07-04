FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/eureka.jar /app/eureka.jar
EXPOSE 8761
ENTRYPOINT [ "java","-jar","eureka.jar" ]