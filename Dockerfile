# 1-qadam: Build bosqichi (Maven va Java 17 bilan)
FROM maven:3.8.5-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# 2-qadam: Run bosqichi (Yengilroq va barqaror Java 17 imiji)
FROM eclipse-temurin:17-jdk
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]