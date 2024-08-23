package com.asynchronous.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsynchronousFileClient {
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5000);
        Future<Void> connectFuture = clientChannel.connect(hostAddress);
        connectFuture.get(); // Blocking until connected
        System.out.println("Connected to the server..."); // Send file name to server
        String fileName = "sample.txt";
        ByteBuffer buffer = ByteBuffer.wrap(fileName.getBytes(StandardCharsets.UTF_8));
        clientChannel.write(buffer).get(); // Blocking until data is sent
        buffer.clear(); // Receive file content from server ByteBuffer
        ByteBuffer fileBuffer = ByteBuffer.allocate(1024);
        Future<Integer> readFuture = clientChannel.read(fileBuffer);
        int bytesRead = readFuture.get(); // Blocking until data is read
        if (bytesRead > 0) {
            fileBuffer.flip();
            String fileContent = new String(fileBuffer.array(), 0, bytesRead);
            System.out.println("Received file content: ");
            System.out.println(fileContent);
        }
        clientChannel.close();


    }


}
