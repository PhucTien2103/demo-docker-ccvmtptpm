# syntax=docker/dockerfile:1

# Build WAR with Maven + JDK 21
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY src ./src
RUN mvn -B -DskipTests clean package

# Run WAR on Tomcat 10.1 (Jakarta Servlet 6)
FROM tomcat:10.1-jdk21-temurin

# Clean default apps and deploy this project as ROOT for easy routing on cloud platforms
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/XinNghiPhep.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
