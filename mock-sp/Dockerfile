FROM openjdk:8-jdk-alpine
ARG JAR_FILE=${pwd}/target/*.jar
WORKDIR /auth-app

COPY ${JAR_FILE} app.jar

CMD ["java","-jar","/auth-app/app.jar"]