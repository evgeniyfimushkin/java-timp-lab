package edu.evgen.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
@RequiredArgsConstructor
@Slf4j
public class Server implements Runnable {
    @Getter
    public final List<Session> sessions = new LinkedList<>();
    private final Set<String> ids = new HashSet<>();
    private final Integer port;
    private Boolean run;

    @SneakyThrows
    public void run() {
        run = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (run) {
                try {
                    log.info("Server is waiting for connections on port {}", port);
                    Socket socket = serverSocket.accept();
                    sessions.add(new Session(this,socket, getId()));
                    sendId(sessions.getLast());
                    sessions.forEach(this::sendSessions);
                    log.info("Client connected. Session {}. Clients count {}", sessions.getLast().getId(), sessions.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @SneakyThrows
    public void sendSessions(Session session){
        PrintStream outputStream = new PrintStream(session.getSocket().getOutputStream());
        outputStream.println("@SESSIONS@");
        ids.stream()
                .forEach(outputStream::println);
        outputStream.println("@END@");
    }
    @SneakyThrows
    private void sendId(Session session){
        PrintStream outputStream = new PrintStream(session.getSocket().getOutputStream());
        outputStream.println("@SETID@");
        outputStream.println(session.getId());
        outputStream.println("@END");
    }
    @SneakyThrows
    private void sendDevelopers(Session session){
        PrintStream outputStream = new PrintStream(session.getSocket().getOutputStream());
        outputStream.println("@SETID@");
        outputStream.println(session.getId());
        outputStream.println("@END");
    }
    private String getId() {
        String currentId = UUID.randomUUID().toString();
        if (!ids.contains(currentId)) {
            ids.add(currentId);
            return currentId;
        } else return getId();
    }

}