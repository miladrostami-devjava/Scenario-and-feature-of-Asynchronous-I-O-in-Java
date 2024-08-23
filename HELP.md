# Asynchronous I/O in java

## Description 
In this scenario, we will implement an asynchronous file server that responds to requests
Asynchronously responds to I/O from clients to read and send the contents of files. use of
be processed (blocking) allows us to make requests without the need for blocking
This method is especially useful in applications that require high performance and parallel processing, such as
Web servers and responsive systems are very useful.
steps:
Server: Asynchronously, request clients to read a file from the system
receives the file.
Client: sends a request to read a file to the server.
Asynchronous (server I/O): after receiving the request, asynchronously (using
Reads the file and sends the content to the client.