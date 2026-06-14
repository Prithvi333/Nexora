FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/products.jar /app/products.jar
EXPOSE 8084
ENTRYPOINT [ "java","-jar","products.jar" ]