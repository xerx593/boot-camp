#### Stage 1: Build the application
FROM openjdk:8-jdk-alpine as build

# Set the current working directory inside the image
WORKDIR /boot-camp/maven

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Build all the dependencies in preparation to go offline.
# This is a separate step so the dependencies will be cached unless
# the pom.xml file has changed.
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src src

# Package the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#### Stage 2: A minimal docker image with command to run the app
FROM openjdk:8-jre-alpine

ARG DEPENDENCY=/boot-camp/maven/target/dependency

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /boot-camp/api/lib
COPY --from=build ${DEPENDENCY}/META-INF /boot-camp/api/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /boot-camp/api

RUN ls -lrt /boot-camp/api/lib/*

ENTRYPOINT ["java", "-cp", "/boot-camp/api:/boot-camp/api/lib/*", "com.tw.api.ApiApplication"]

HEALTHCHECK --interval=1m --timeout=3s CMD wget -q -T 3 -s http://localhost:8080/actuator/health/ || exit 1

ENV PORT 8080

EXPOSE 8080
EXPOSE 9221