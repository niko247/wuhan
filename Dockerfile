FROM maven:3.6.3-openjdk-15 AS builder
COPY . /app
WORKDIR /app
RUN mvn verify

FROM openjdk:15
COPY --from=builder /app/target /target
RUN chmod +x /target/bin/worker
CMD ["sh", "/target/bin/worker"]