FROM openjdk:11-jre-slim

# Copy application
COPY ktor-exec.jar /var/application/

# Change to working directory
WORKDIR /var/application/

# Expose Ports
EXPOSE $PORT

# Run application
CMD java -Dlogback.configurationFile=logback-cloud.xml -jar /var/application/ktor-exec.jar