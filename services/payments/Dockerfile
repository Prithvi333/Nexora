FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/payments.jar /app/payments.jar
EXPOSE 8086
ENTRYPOINT [ "java","-jar","payments.jar" ]