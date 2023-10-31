FROM openjdk:17-oracle

WORKDIR /app

COPY security-0.0.1-SNAPSHOT.jar /app/security-0.0.1-SNAPSHOT.jar

EXPOSE 8000

CMD ["java", "-jar", "security-0.0.1-SNAPSHOT.jar"]
