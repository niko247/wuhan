FROM openjdk:15
COPY target /target
RUN chmod +x /target/bin/worker
CMD ["sh", "/target/bin/worker"]