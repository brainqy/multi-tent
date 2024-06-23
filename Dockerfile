# Dockerfile for Spring Boot application

# Use a base image containing Java runtime environment
FROM openjdk:17-jdk-alpine

# Add a volume pointing to /tmp
VOLUME /tmp

# Copy the application JAR file
COPY target/springboot-crud-k8s.jar springboot-crud-k8s.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/springboot-crud-k8s.jar"]
