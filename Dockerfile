FROM maven:3.9-amazoncorretto-20 AS builder
COPY . /app
WORKDIR /app
RUN mvn verify

FROM openjdk:20
COPY --from=builder /app/target /target
RUN chmod +x /target/bin/worker
CMD ["sh", "/target/bin/worker"]