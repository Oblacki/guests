FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/guests-2.5.0-SNAPSHOT.jar /app

EXPOSE 8082

CMD ["java", "-jar", "guests-2.5.0-SNAPSHOT.jar"]