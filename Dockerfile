FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/backend-0.0.1-SNAPSHOT.jar vigyan-mancha-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/vigyan-mancha-api.jar"]