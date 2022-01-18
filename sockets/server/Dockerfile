FROM openjdk:8-jre-alpine
COPY target/server-1.0.jar /server-1.0.jar
CMD ["/usr/bin/java", "-cp", "/server-1.0.jar", "EchoServer"]