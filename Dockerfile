FROM maven:3.8.1-openjdk-16 AS builder
COPY . /app
WORKDIR /app
RUN mvn verify

FROM openjdk:16
COPY --from=builder /app/target /target
RUN chmod +x /target/bin/worker
CMD ["sh", "/target/bin/worker"]