package edu.evgen;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Server {
    private static List<Thread> threads = new LinkedList<>();
    private static List<Socket> clients = new LinkedList<>();
    private static final Integer PORT = 19000;
    private static Boolean run;
    private static Thread listeningThread;

    public static void main(String[] args) throws IOException {
        run = true;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            listeningThread = new Thread(listeningThread(serverSocket));
            listeningThread.start();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }

    private static Runnable listeningThread(ServerSocket serverSocket) throws IOException {
        while (run){
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInputStream());
            threads.add(new Thread(socketInit(serverSocket, socket)));
            threads.getLast().start();
        }
        return null;
    }
    private static Runnable socketInit(ServerSocket serverSocket, Socket socket){
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {
            clients.add(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
