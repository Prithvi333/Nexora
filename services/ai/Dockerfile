FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/ai.jar /app/ai.jar
EXPOSE 8089
ENTRYPOINT [ "java","-jar","products.jar" ]