FROM maven:3.8.6-amazoncorretto-19 AS builder
COPY . /app
WORKDIR /app
RUN mvn verify

FROM openjdk:19
COPY --from=builder /app/target /target
RUN chmod +x /target/bin/worker
CMD ["sh", "/target/bin/worker"]