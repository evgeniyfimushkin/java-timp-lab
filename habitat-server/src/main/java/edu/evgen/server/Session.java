package edu.evgen.server;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Data
public class Session implements Closeable {
    private final Server server;
    public final Socket socket;
    public String id;
    Thread listener = new Thread(this::listen);


    public Session(Server server, Socket socket, String id) {
        this.server= server;
        this.socket = socket;
        this.id = id;
        listener.start();
    }
    @SneakyThrows
    public void getOut(List<String> lines, BufferedReader lineReader){
        log.info("Ricochet {}",id);
        PrintStream outputStreamStr = new PrintStream(socket.getOutputStream());
        outputStreamStr.println(lines.getLast());
        ObjectOutputStream outputStream;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        lines.add(lineReader.readLine());
        while (!lines.getLast().equals("@END@")) {
            log.info(lines.getLast());
            outputStream.writeObject(lines.getLast());
            lines.add(lineReader.readLine());
        }
        outputStreamStr.println();
        outputStreamStr.println(lines.getLast());

    }
    @SneakyThrows
    public void ricochet(List<String> lines, BufferedReader lineReader){
        String clientId = lineReader.readLine();
        log.info("Ricochet");
        for (Session session : server.sessions){
            if (session.id.equals(clientId))
                session.getOut(lines, lineReader);
        }
//        server.sessions.stream().filter(session -> session.id.equals(clientId))
//                .peek(session -> session.getOut(lines, lineReader));
    }
    @SneakyThrows
    public void listen(){
        while (true) {
            BufferedReader lineReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            List<String> lines = new ArrayList<>();
            lines.add(lineReader.readLine());
            while (!(lines.getLast().isEmpty())) {
                //log.info("switch <- {}", lines.getLast());
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
    public void close(){
        socket.close();
        server.sessions.remove(this);
        server.sessions.forEach(server::sendSessions);
    }
}