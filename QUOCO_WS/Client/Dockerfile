FROM openjdk:8-jre-alpine
ENV SERVER_HOST=localhost
COPY target/Client-0.0.1-jar-with-dependencies.jar /Client-0.0.1.jar
CMD /usr/bin/java -jar /Client-0.0.1.jar $SERVER_HOST