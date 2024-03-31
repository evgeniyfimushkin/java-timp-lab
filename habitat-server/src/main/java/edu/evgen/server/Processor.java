package edu.evgen.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.evgen.client.Message;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static edu.evgen.client.MessageMarkers.SESSIONS;

@Service
@Slf4j
@NoArgsConstructor
public class Processor {
    @Getter
    public final List<Session> sessions = new LinkedList<>();
    public final Set<String> ids = new HashSet<>();

    @Autowired
    ObjectMapper objectMapper;

    @Async("processTaskExecutor")
    @SneakyThrows
    public void processSocket(Socket socket) {
        log.info("processSocket <-");
        Thread.sleep(5000L);
        Session newSession = new Session(this,socket, getId());
        log.info("Session Created: {}", newSession);
        log.info("Created Session JSON: {}", objectMapper.writeValueAsString(newSession));
        sessions.add(newSession);
        sessions.forEach(this::sendSessions);
        sessions.getLast().sendId();
        Thread.sleep(5000L);
        log.info("Client connected. Session {}. Clients count {}", sessions.getLast().getId(), sessions.size());
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
