FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/orders.jar /app/orders.jar
EXPOSE 8085
ENTRYPOINT [ "java","-jar","orders.jar" ]