FROM maven:3.8.2-openjdk-17 AS builder
COPY . /app
WORKDIR /app
RUN mvn verify

FROM openjdk:17
COPY --from=builder /app/target /target
RUN chmod +x /target/bin/worker
CMD ["sh", "/target/bin/worker"]