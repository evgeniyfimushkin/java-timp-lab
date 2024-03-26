package edu.evgen.server;

import edu.evgen.client.Message;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import edu.evgen.habitat.employee.*;

@Slf4j
@Data
public class Session implements Closeable {
    private final Server server;
    public final Socket socket;
    public String id;
    Thread listener = new Thread(this::listen);


    public Session(Server server, Socket socket, String id) {
        this.server = server;
        this.socket = socket;
        this.id = id;
        listener.start();
    }

    @Synchronized
    @SneakyThrows
    public void getOut(List<String> lines, ObjectInputStream objectInputStream, String clientId,
                       List<Employee> objects) {

        //.info(lines.getLast());

        log.info("Ricochet {}", id);
        //PrintWriter outputWriter = new PrintWriter(socket.getOutputStream());
        //outputWriter.println(lines.getLast());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        lines.removeLast();

        log.info(lines.getLast());
        objectOutputStream.writeObject(lines.getLast());
        objectOutputStream.writeObject(clientId);
//        outputWriter.println(lines.getLast());
//        outputWriter.flush();
//        outputWriter.println(clientId);
//        outputWriter.flush();

        for (Employee object : objects) {
            objectOutputStream.writeObject(object);
            log.info("SEND");
        }
        log.info("Object size {}", objects.size());
        objectOutputStream.writeObject(null);
//        objects.forEach(outputStream::writeObject);
        log.info("output Object success {}", id);
    }

    @SneakyThrows
    @Synchronized
    public void reply(List<String> lines, ObjectInputStream objectInputStream) {

        List<Employee> objects = new ArrayList<>();
        lines.add((String) objectInputStream.readObject());
        String clientId = lines.getLast();
        log.info("Ricochet");

        Employee object;
        while ((object = (Developer) objectInputStream.readObject()) != null) {
            objects.add(object);
            log.info("OBJECT ADDED");
        }

        log.info("Object size {}", objects.size());
        log.info("end of reading of {}", clientId);
//        server.sessions.stream().filter(session -> clientId.equals(session.id))
//                .peek(session -> session.getOut(lines, lineReader));
        for (Session iter : server.sessions) {
            if (clientId.equals(iter.id))
                iter.getOut(lines, objectInputStream, id, objects);
        }
    }

    @SneakyThrows
    @Synchronized
    private void transport(Message message) {
        log.info("Transport with marker {}",message.getMarker());
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
    public void listen() {
        while (true) {

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