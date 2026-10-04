# Build stage: the service's own Maven Wrapper compiles the fat jar
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
# Separate layer: dependencies are re-downloaded only when pom.xml changes
RUN ./mvnw --batch-mode --quiet dependency:go-offline
COPY src/ src/
RUN ./mvnw --batch-mode --quiet package -DskipTests

# Runtime stage: JRE only, same base as the other services
FROM eclipse-temurin:25-jre
WORKDIR /app
# *.jar glob keeps the Dockerfile independent of the version in pom.xml
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
