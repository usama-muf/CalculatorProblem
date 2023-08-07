#FROM eclipse-temurin:17-alpine
# FROM openjdk:17-jdk-slim
	
# LABEL mentainer="Mohammed Usama"

# WORKDIR /app

# COPY target/calculator-problem-v2-0.0.1-SNAPSHOT.jar /app/calculator-problem-v2-0.0.1-SNAPSHOT.jar

# ENTRYPOINT ["java", "-jar", "calculator-problem-v2-0.0.1-SNAPSHOT.jar"]



# FROM maven:3.6.3-jdk-11-slim AS MAVEN_BUILD
FROM maven:3.8.4-openjdk-17 AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn clean install  -DskipTests
#RUN mvn package

FROM openjdk:17-alpine

WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/*.jar /app/app.jar
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
# ENTRYPOINT ["java", "-jar", "app.war"]



# Use a base image with JDK and Maven to build the application
# FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory inside the container
#WORKDIR /app

# Copy the pom.xml file to install dependencies
#COPY pom.xml .

# Install the project dependencies
#RUN mvn dependency:go-offline

# Copy the source code
#COPY src ./src

# Build the application
#RUN mvn clean package -DskipTests

# Use a slim JRE image as the base for the final image
#FROM openjdk:17-jre-slim


# Set the working directory inside the container
## Expose the port that the application listens on
#EXPOSE 8080

# Set environment variables for the MySQL connection
#ENV MYSQL_HOST=localhost \
#   MYSQL_PORT=3306 \
#    MYSQL_DATABASE=calculator_problem_v2 \
#   MYSQL_USER=root \
#   MYSQL_PASSWORD=root

# Install MySQL client
#RUN apt-get update && apt-get install -y default-mysql-client

# Run the application
#ENTRYPOINT ["java", "-jar", "calculator-problem-v2-0.0.1-SNAPSHOT.jar"]
