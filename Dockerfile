FROM gcr.io/distroless/java17

COPY ./*.jar ./app.jar

CMD ["app.jar"]