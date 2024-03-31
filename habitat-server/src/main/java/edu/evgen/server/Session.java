package edu.evgen.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.evgen.client.Message;
import edu.evgen.client.MessageMarkers;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static edu.evgen.client.MessageMarkers.SESSIONS;
import static edu.evgen.client.MessageMarkers.SETID;

@Slf4j
@Data
@ToString(of = {"id", "run"} )
public class Session implements Closeable {

    @JsonIgnore
    private final Processor server;

    @JsonIgnore
    public final Socket socket;

    @JsonProperty
    public String id;

    @JsonProperty
    Boolean run;

    @JsonIgnore
    Thread listener = new Thread(this::listen);


    public Session(Processor server, Socket socket, String id) {
        this.server = server;
        this.socket = socket;
        this.id = id;
        listener.start();
        run = true;
    }

    @SneakyThrows
    @Synchronized
    private void transport(Message message) {
        log.info("Transport with marker {}", message.getMarker());
        if (id.equals(message.getRecipient())) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
        } else {
            for (Session iter : server.sessions) {
                if (message.getRecipient().equals(iter.id))
                    iter.transport(message);
            }
        }
    }

    @SneakyThrows
    private void disconnect() {
        server.ids.remove(id);
        server.sessions.remove(this);
        server.sessions.forEach(server::sendSessions);
        run = false;
        listener.interrupt();
        socket.close();
    }
    @SneakyThrows
    public void sendId(){
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        List<String> list = new ArrayList<>();
        list.add(getId());
        Message message = new Message(SETID,id,id,list);
        objectOutputStream.writeObject(message);
        objectOutputStream.flush();
    }
    @SneakyThrows
    public void listen() {
        while (run) {

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) objectInputStream.readObject();

            if (message == null) {
                continue;
            }

            switch (message.getMarker()) {
                case EXCHANGEREQUEST:
                    transport(message);
                    break;
                case EXCHANGEREPLY:
                    transport(message);
                    break;
                case DISCONNECT:
                    disconnect();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    @SneakyThrows
    public void close() {
        socket.close();
        server.sessions.remove(this);
        server.sessions.forEach(server::sendSessions);
    }
}