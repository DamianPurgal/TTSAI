FROM maven:3.8-amazoncorretto-19 AS build
COPY src ./src
COPY pom.xml ./
RUN mvn clean package
CMD ["mvn", "spring-boot:run"]