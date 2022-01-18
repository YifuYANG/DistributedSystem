FROM openjdk:8-jre-alpine
ENV SERVICENAME="ws_girlpower"
COPY target/GirlPower-0.0.1-jar-with-dependencies.jar /GirlPower-0.0.1.jar
CMD /usr/bin/java -jar /GirlPower-0.0.1.jar $SERVICENAME