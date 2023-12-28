package org.codeforall.ooptimus;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public final static int PORTNUMB = 9999;
    public final static String HOST = "localHost";
    private static List<Socket> connectSokets = new ArrayList<>();

    public Server() {}

    public static void main(String[] args) {
        ExecutorService executorService= Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORTNUMB)) {
            System.out.println("Binding to port " + PORTNUMB);
            System.out.println("Server started: " + serverSocket);
            System.out.println("Server is listening on port " + PORTNUMB);
            System.out.println("Waiting for a client connection");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected " + clientSocket.getInetAddress());
                connectSokets.add(clientSocket);
                executorService.submit(new Client(clientSocket, connectSokets));
                //new ServerThread(clientSocket, connectSokets).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}