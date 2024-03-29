package edu.evgen.server;

import edu.evgen.client.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static edu.evgen.client.MessageMarkers.SESSIONS;
import static edu.evgen.client.MessageMarkers.SETID;

@RequiredArgsConstructor
@Slf4j
public class Server implements Runnable {
    @Getter
    public final List<Session> sessions = new LinkedList<>();
    public final Set<String> ids = new HashSet<>();
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
                    sessions.forEach(this::sendSessions);
                    sessions.getLast().sendId();
                    log.info("Client connected. Session {}. Clients count {}", sessions.getLast().getId(), sessions.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @SneakyThrows
    public void sendSessions(Session session){
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(session.socket.getOutputStream());
        Message message = new Message(SESSIONS, session.id, session.id, new ArrayList<>(ids));
        objectOutputStream.writeObject(message);
    }
    private String getId() {
        String currentId = UUID.randomUUID().toString();
        if (!ids.contains(currentId)) {
            ids.add(currentId);
            return currentId;
        } else return getId();
    }

}
