FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/products.jar /app/products.jar
EXPOSE 8084
ENTRYPOINT [ "java","-jar","products.jar" ]