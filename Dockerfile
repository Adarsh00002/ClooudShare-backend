# ----------------------------------------------------------------------
# STAGE 1: BUILD STAGE
# ----------------------------------------------------------------------
FROM maven:3.9.5-eclipse-temurin-21 AS build

# 1. वर्किंग डायरेक्टरी सेट करें
WORKDIR /app


COPY pom.xml .


COPY src ./src

# 4. Maven पैकेजिंग कमांड चलाएँ
RUN mvn clean package -DskipTests

# ----------------------------------------------------------------------
# STAGE 2: RUNTIME STAGE (यह स्टेज ठीक लग रहा है, इसे न बदलें)
# ----------------------------------------------------------------------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/cloudshareapp-0.0.1-SNAPSHOT.jar /app/cloudshareapp-v1.0.jar


EXPOSE 9090
ENTRYPOINT ["java", "-jar", "cloudshareapp-v1.0.jar"]