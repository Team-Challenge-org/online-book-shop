# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project descriptor files for dependencies resolution
COPY pom.xml .

# Copy the entire project source
COPY src ./src
RUN bash -c 'env | grep JAVA_HOME; java -version; mvn -version'
# Build the application with Maven
RUN mvn package -DskipTests

# Stage 2: Create a minimal runtime image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled application JAR file from the builder stage
COPY --from=builder /app/target/book-shop-0.0.1-SNAPSHOT.jar ./book-shop-0.0.1-SNAPSHOT.jar

#Uncomment this if you are building with .env file
#RUN source .env
#COPY .env .

# Set JAVA_HOME explicitly (for hosting)
ENV JAVA_HOME /usr/java/openjdk-17

# Expose the port that the Spring Boot application uses (default is 8080)
EXPOSE 8080

# Specify the command to run on container start
CMD ["java", "-jar", "book-shop-0.0.1-SNAPSHOT.jar"]