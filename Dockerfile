FROM openjdk:8-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY pom.xml /app
COPY src /app/src

# Install the required dependencies
RUN apk update && \
    apk add maven

# Build the Maven project
RUN mvn clean install

# Set the environment variables for Jenkins
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

# Start the Jenkins server
CMD ["/usr/local/bin/jenkins.sh"]
