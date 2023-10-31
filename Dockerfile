FROM adoptopenjdk:17-jre-hotspot

WORKDIR /app

COPY ./target/security-0.0.1-SNAPSHOT.jar /app/security-0.0.1-SNAPSHOT.jar

EXPOSE 8000

CMD ["java", "-jar", "app.jar"]
