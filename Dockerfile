# Stage 1: prende servlet-api.jar da Tomcat
FROM tomcat:9.0-jdk11-openjdk-slim AS tomcat_base

# Stage 2: compila i sorgenti Java
FROM eclipse-temurin:11-jdk AS builder
WORKDIR /build

COPY --from=tomcat_base /usr/local/tomcat/lib/servlet-api.jar /build/lib/
COPY WebContent/WEB-INF/lib/mysql-connector-java-5.1.47-bin.jar /build/lib/
COPY src /build/src

RUN find /build/src -name "*.java" > /build/sources.txt && \
    javac -encoding UTF-8 \
          -cp "/build/lib/servlet-api.jar:/build/lib/mysql-connector-java-5.1.47-bin.jar" \
          -d /build/classes \
          @/build/sources.txt

# Stage 3: Tomcat con l'app deployata
FROM tomcat:9.0-jdk11-openjdk-slim

RUN rm -rf /usr/local/tomcat/webapps/*

COPY WebContent                /usr/local/tomcat/webapps/BachecaUnisa
COPY --from=builder /build/classes /usr/local/tomcat/webapps/BachecaUnisa/WEB-INF/classes

EXPOSE 8080
