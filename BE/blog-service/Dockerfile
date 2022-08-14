FROM ubuntu:lastest

RUN apt-get update && \
    apt-get install -y --no-install-recommends locales && \
    locale-gen en_US.UTF-8 && \
    apt-get dist-upgrade -y && \
    apt-get install iputils-ping

FROM eclipse-temurin:11

ARG JAR_FILE=../target/blog-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]