FROM eclipse-temurin:11

COPY . /app
ENTRYPOINT ["/app/run.bash"]
