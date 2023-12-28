package org.codeforall.ooptimus;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client implements Runnable {
    private Socket socket;
    private List<Socket> connectSockets;
    private static int clientCount = 0;

    public Client(Socket socket, List<Socket> connectSockets) {
        this.socket = socket;
        this.connectSockets = connectSockets;
    }


    public static void main(String[] args) {
        try {
            Socket socketClient = new Socket(Server.HOST, Server.PORTNUMB);
            PrintWriter output = new PrintWriter(socketClient.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            Scanner text = new Scanner(System.in);
            String message;
            do {
                message = text.nextLine();
                output.println(message);
                String response = input.readLine();
                output.println(response);
                System.out.println(response);
            }while (!message.equals("/quit"));
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            clientCount++;
            Thread.currentThread().setName("Client " + clientCount);
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String message;
            do {
                message = reader.readLine();
                for (Socket connect : connectSockets) {
                    if (connect != socket) {
                        PrintWriter connectedOut = new PrintWriter(connect.getOutputStream(), true);
                        connectedOut.println(Thread.currentThread().getName() + " : " + message);
                    }
                }
                if (message.equals("/list")) {
                    for (Socket connect : connectSockets) {
                        PrintWriter printList = new PrintWriter(connect.getOutputStream(), true);
                        printList.println("Clients \n" + Thread.currentThread().getName());
                    }
                }
            } while (!message.equals("/quit"));
            socket.close();
            connectSockets.remove(socket);
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}