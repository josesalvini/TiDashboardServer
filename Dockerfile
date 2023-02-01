#
# Build stage
#
#FROM maven:3.8.2-jdk-11 AS build
FROM maven:3-jdk-8-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
#FROM openjdk:11-jdk-slim
FROM openjdk:8-jdk-alpine
COPY --from=build /target/dashboard-0.0.1.jar dashboard.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","dashboard.jar"]