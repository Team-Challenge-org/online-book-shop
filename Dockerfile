# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS builder

# Enable BuildKit
ENV DOCKER_BUILDKIT=1

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project descriptor files for dependencies resolution
COPY pom.xml .

# Copy the entire project source
COPY src ./src

# Build the application with Maven
# Uncomment this row to use cached dependencies
RUN --mount=type=cache,target=/root/.m2 mvn -f pom.xml clean package -DskipTests

# Comment this row if you are using cached dependencies
#RUN mvn clean package -DskipTests


# Stage 2: Create a minimal runtime image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled application JAR file from the builder stage
COPY --from=builder /app/target/book-shop-0.0.1-SNAPSHOT.jar ./book-shop-0.0.1-SNAPSHOT.jar

# Expose the port that the Spring Boot application uses (default is 8080)
EXPOSE 8080

# Specify the command to run on container start
CMD ["java", "-jar", "book-shop-0.0.1-SNAPSHOT.jar"]