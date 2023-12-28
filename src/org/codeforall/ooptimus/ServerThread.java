package org.codeforall.ooptimus;

import java.io.*;
import java.lang.reflect.Executable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThread extends Thread {
    private Socket socket;
    private List<Socket> connectedSockets;


    public ServerThread(Socket socket, List<Socket> connectedSockets) {
        this.socket = socket;
        this.connectedSockets = connectedSockets;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String message;
            do {
                message = reader.readLine();
                for (Socket connect : connectedSockets) {
                    if (connectedSockets != socket) {
                        PrintWriter connectedOut = new PrintWriter(connect.getOutputStream(), true);
                        connectedOut.println(socket.getPort() + " : " + message);
                    }
                }
                if (message.equals("/list")) {
                    for (Socket list : connectedSockets) {
                        PrintWriter printList = new PrintWriter(list.getOutputStream(), true);
                        printList.println(Arrays.toString(new int[]{list.getPort()}));
                    }
                }
            } while (!message.equals("/quit"));


            socket.close();
            connectedSockets.remove(socket);
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
