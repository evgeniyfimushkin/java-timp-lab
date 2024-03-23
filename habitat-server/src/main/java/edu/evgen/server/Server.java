package edu.evgen.server;

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
    private final List<Session> sessions = new LinkedList<>();
    private final Set<String> ids = new HashSet<>();
    private final Random random = new Random();
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
                    sessions.add(new Session(socket, getId()));
                    sessions.forEach(this::sendSessions);
                    log.info("Client connected. Session {}. Clients count {}", sessions.getLast().id(), sessions.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @SneakyThrows
    private void sendSessions(Session session){
        PrintStream outputStream = new PrintStream(session.socket().getOutputStream());
        outputStream.println("@SESSIONS@");
        ids.stream()
                .forEach(outputStream::println);
        outputStream.println("@END@");
    }
    private String getId() {
        String currentId = UUID.randomUUID().toString();
        if (!ids.contains(currentId)) {
            ids.add(currentId);
            return currentId;
        } else return getId();
    }
}
