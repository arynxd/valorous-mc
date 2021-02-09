FROM openjdk:15-alpine
WORKDIR /home/monkebot/
COPY build/libs/Valorous-all.jar Valorous.jar
ENTRYPOINT java -jar Valorous.jar
