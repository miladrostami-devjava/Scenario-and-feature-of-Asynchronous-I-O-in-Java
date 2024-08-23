package com.asynchronous.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsynchronousFileServer {
    public static void main(String[] args) throws IOException,
            InterruptedException, ExecutionException {
        AsynchronousServerSocketChannel serverChannel =
                AsynchronousServerSocketChannel.open();
        serverChannel.bind(new
                InetSocketAddress("localhost", 5000));
        System.out.println("Server is listening on port 5000...");
        while (true) {
            Future<AsynchronousSocketChannel> acceptFuture =
                    serverChannel.accept();
            AsynchronousSocketChannel clientChannel =
                    acceptFuture.get();
            // Blocking until a connection is established

            // Handle client connection in a new thread
            new Thread(() ->
                    handleClient(clientChannel)).start();
        }
    }

    private static void
    handleClient(AsynchronousSocketChannel clientChannel) {
        try {
            //Read filename from client

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Future<Integer> readFuture =
                    clientChannel.read(buffer);
            int bytesRead = readFuture.get(); //    Blocking until data is read
            if (bytesRead == -1) {
                clientChannel.close();
                return;
            }
            buffer.flip();
            String fileName =
                    new String(buffer.array(), 0, buffer.limit()).trim();
            System.out.println("Received request to read file: " + fileName); //Read the requested file asynchronously

            Path path = Paths.get(fileName);
            AsynchronousFileChannel fileChannel =
                    AsynchronousFileChannel.open(path, StandardOpenOption.READ);
            ByteBuffer fileBuffer = ByteBuffer.allocate((int)
                    fileChannel.size());
            Future<Integer> fileReadFuture =
                    fileChannel.read(fileBuffer, 0);
            fileReadFuture.get(); // Blocking until file is read
            fileBuffer.flip(); // Send file content back to client
            clientChannel.write(fileBuffer).get(); // Blocking until data is sent
            System.out.println("File content sent to client.");
            clientChannel.close();
        } catch (IOException | InterruptedException |
                 ExecutionException e) {
            e.printStackTrace();
        }
    }
}
