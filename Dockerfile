FROM eclipse-temurin:21-jdk

# purpose : we need to copy jar file into the docker image that we want to build
# eg: we want to create inside the container we want to create the folder and want to call of the jar file for running the container
# copying the jar file into the running container
COPY target/reward-system-0.0.1-SNAPSHOT.jar /app/reward-system.jar

WORKDIR /app

# cmd: command to execude the jar file
CMD ["java", "-jar", "reward-system.jar"]

EXPOSE 8085