# sockets

This is the base code for the first Practical of COMP30220.

It contains 2 Maven projects:

*  server: a simple java echo server.
*  client: a simple java client for use with the echo server.

# Compiling the example

To compile the server, goto the server folder and type:

`mvn compile`

To compile the client, goto the client folder and type:

`mvn compile`

# Running the example outside of docker

To run the server, goto the server folder and type:

`mvn exec:java`

To run the client, goto the client folder and type:

`mvn exec:java`

# Packaging and containerising the server

1.  goto the server folder
2.  type `mvn package`
3.  type `docker build -t server:latest .`

# Connecting to the server from outside the container (Docker Server / Maven Client)

To run the image you created above, simply type:

`docker run -p 7788:7788 server:latest`

Note the inclusion of the -p flag which is used to expose the internal port 7788 on the host network interface.  Without this, the server socket would be isolated and accessible only from within the container that it is running in.

To run the client, simply type:

`mvn exec:java`

**if you are running Docker toolkit this will not work because the docker engine is running in a virtual machine. The equivalent behaviour requires that you run the docker toolkit shell, type `docker-machine ip` to get the IP address of the virtual machine and then to type `mvn exec:java -D"exec.args=<vm ip address>"`.**

# Packaging and containerising the client

1.  goto the client folder
2.  type `mvn package`
3.  type `docker build -t client:latest .`

# Running the example

1.  Create a test network by typing: `docker network create my_network`
2.  Start the server by typing: `docker run --network-alias server --network my_network -it server:latest`
3.  Start the client by typing: `docker run --network my_network -it client:latest`

Note, if you include the port mapping when you run the server, then it is accessible from both the host machine and any docker container that is connected to the user defined network:

`docker run -p 7788:7788 --network-alias server --network my_network -it server:latest`