package edu.evgen.server;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.ServerSocket;
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

    @SneakyThrows
    public void getOut(List<String> lines, BufferedReader lineReader, String clientId,
                       List<Employee> objects ) {

        //.info(lines.getLast());

        log.info("Ricochet {}", id);
        PrintWriter outputWriter = new PrintWriter(socket.getOutputStream());
        //outputWriter.println(lines.getLast());
        lines.removeLast();

        log.info(lines.getLast());
        outputWriter.println(lines.getLast());
        outputWriter.flush();
        outputWriter.println(clientId);
        outputWriter.flush();

        ObjectOutputStream outputStream;
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        for (Employee object : objects) {
            outputStream.writeObject(object);
            log.info("SEND");
        }
        log.info("Object size {}",objects.size());
        outputStream.writeObject(null);
//        objects.forEach(outputStream::writeObject);
        log.info("output Object success {}", id);
    }

    @SneakyThrows
    @Synchronized
    public void ricochet(List<String> lines, BufferedReader lineReader) {

        List<Employee> objects = new ArrayList<>();
        lines.add(lineReader.readLine());
        String clientId = lines.getLast();
        log.info("Ricochet");

        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());


        Employee object;
        while ((object = (Employee) objectInputStream.readObject()) != null) {
            objects.add((Manager) object);
            log.info("OBJECT ADDED");
        }

        log.info("Object size {}",objects.size());
        log.info("end of reading of {}", clientId);
//        server.sessions.stream().filter(session -> clientId.equals(session.id))
//                .peek(session -> session.getOut(lines, lineReader));
        for (Session iter : server.sessions) {
            if (clientId.equals(iter.id))
                iter.getOut(lines, lineReader, id, objects);
        }
    }

    @SneakyThrows
    public void listen() {
        while (true) {
            BufferedReader lineReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            List<String> lines = new ArrayList<>();
            lines.add(lineReader.readLine());
            while (!(lines.getLast().isEmpty())) {
                log.info("switch <- {}", lines.getLast());
                switch (lines.getLast()) {
                    case "@EXCHANGEREQUEST@":
                        ricochet(lines, lineReader);
                        break;
                    case "@EXCHANGEREPLY@":
                        ricochet(lines, lineReader);
                        break;
                    default:
                        break;
                }
                lines.add(lineReader.readLine());
            }
            log.info("Buffer is empty");

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