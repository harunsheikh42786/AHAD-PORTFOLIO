# Use OpenJDK 23 as the base image
FROM openjdk:23-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file
COPY target/AHAD-PORTFOLIO-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which your application runs
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
