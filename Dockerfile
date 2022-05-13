FROM maven:3.8.5-openjdk-18 AS builder
COPY . /app
WORKDIR /app
RUN mvn verify

FROM openjdk:18
COPY --from=builder /app/target /target
RUN chmod +x /target/bin/worker
CMD ["sh", "/target/bin/worker"]