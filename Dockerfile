FROM openjdk:21

COPY target/*.jar order-service.jar
ENTRYPOINT ["java", "-jar", "order-service.jar"]