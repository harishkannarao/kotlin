FROM openjdk:11-jre-slim

# Copy application
COPY wiremock-runner-exec.jar /var/application/

# Change to working directory
WORKDIR /var/application/

# Expose Ports
EXPOSE $PORT

# Run application
CMD java -jar /var/application/wiremock-runner-exec.jar