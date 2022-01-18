FROM openjdk:8-jre-alpine
ENV SERVICENAME="ws_broker"
COPY target/Broker-0.0.1-jar-with-dependencies.jar /Broker-0.0.1.jar
CMD /usr/bin/java -jar /Broker-0.0.1.jar $SERVICENAME