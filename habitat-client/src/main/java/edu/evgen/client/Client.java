package edu.evgen.client;

import edu.evgen.habitat.Simulation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class Client implements Closeable {

    private final Socket socket;

    private final Simulation simulation;

    @Getter
    private final List<List<String>> serverMessages = new ArrayList<>();

    public Client(Integer port) throws IOException {
        this.socket = new Socket("localhost", port);
        this.simulation = new Simulation(this::pullMessage, 100L, "client");
        simulation.startSimulation();
    }
    public static Optional<Client> getClient(Integer port){
        try{
            return Optional.of(new Client(port));
        } catch (IOException e){
            log.warn("Server not found");
            return Optional.empty();
        }
    }


    @SneakyThrows
    public void pullMessage() {
        BufferedReader lineReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
        List<String> lines = new ArrayList<>();

        do {
            lines.add(lineReader.readLine());
        } while (!"@END@".equals(lines.getLast()));

        log.info("getMessage -> {}", lines);
//        lines.removeFirst();
//        lines.removeLast();
        serverMessages.add(lines);
    }


    @Override
    @SneakyThrows
    public void close() {
        socket.close();
    }
}