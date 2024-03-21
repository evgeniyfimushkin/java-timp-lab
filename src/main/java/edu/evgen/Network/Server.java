package edu.evgen.Network;

import lombok.Data;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
public class Server {
    @Getter
    private static List<Session> sessions = new LinkedList<>();
    private final static TreeSet<Long> ids = new TreeSet();
    private static Random random = new Random();
    private static final Integer PORT = 19000;
    private static Boolean run;
    private static Thread listeningThread;

    public static void main(String[] args) throws IOException {
        while (run) {
            run = true;
            ServerSocket serverSocket = null;
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInputStream());
                sessions.add(new Session(socket, getId()));
                sessions.getLast().start();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                serverSocket.close();
            }
        }
    }

    private static Long getId(){
        Long currentId = random.nextLong();
        if (!ids.contains(currentId)){
            ids.add(currentId);
            return currentId;
        }
        else return getId();
    }
}
