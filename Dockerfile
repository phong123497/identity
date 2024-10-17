# stage1: build

FROM maven:3.9.9-amazoncorretto-21 AS build

# Copy source code and pom.xml file to /app folder

WORKDIR /app
COPY pom.xml .
COPY src ./src

# build source code with maven
RUN mvn spotless:apply
RUN mvn package -DskipTests


#stage2 :create image
FROM amazoncorretto:21.0.4

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]