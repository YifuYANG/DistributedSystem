FROM openjdk:8-jre-alpine
ENV SERVICENAME="ws_dodgydrivers"
COPY target/DodgyDrivers-0.0.1-jar-with-dependencies.jar /DodgyDrivers-0.0.1.jar
CMD /usr/bin/java -jar /DodgyDrivers-0.0.1.jar $SERVICENAME