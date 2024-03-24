package edu.evgen.client;

import edu.evgen.SceneController;
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
    private final SceneController controller;
    public String id;

    public Client(SceneController controller, Integer port) throws IOException {
        this.controller = controller;
        this.socket = new Socket("localhost", port);
        this.simulation = new Simulation(this::pullMessage, 100L, "client");
        simulation.startSimulation();
    }

    public static Optional<Client> getClient(SceneController controller, Integer port) {
        try {
            return Optional.of(new Client(controller, port));
        } catch (IOException e) {
            log.warn("Server not found");
            return Optional.empty();
        }
    }

    @SneakyThrows
    public void pullMessage() {
        BufferedReader lineReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
        List<String> lines = new ArrayList<>();
        lines.add(lineReader.readLine());
        while (!(lines.getLast().isEmpty())) {
            log.info("switch <- {}", lines.getLast());
            switch (lines.getLast()) {
                case "@SESSIONS@":
                    sessionsMessageHandler(lines, lineReader);
                    break;
                case "@SETID@":
                    getIdFromServer(lines, lineReader);
                    break;
                case "@MANAGERS@":
                    managersMessageHandler(lines, lineReader);
                    break;
                case "@DEVELOPERS@":
                    developersMessageHandler(lines, lineReader);
                    break;
                default:
                    break;
            }
            lines.add(lineReader.readLine());
        }
        log.info("Buffer is empty");

    }

    @SneakyThrows
    private void getIdFromServer(List<String> lines, BufferedReader lineReader) {
        lines.add(lineReader.readLine());
        id = lines.getLast();
        lines.add(lineReader.readLine());
        serverMessages.add(lines);
        log.info("getIdFromServer -> {}", id);
        controller.networkStatusLabel.setText("Status: Online");
        controller.clientIdLabel.setText("Id: " + id);
        controller.networkLabel.setText("Network: connected");
    }

    @SneakyThrows
    private void sessionsMessageHandler(List<String> lines, BufferedReader lineReader) {
        List<String> ids = new ArrayList<>();
        lines.add(lineReader.readLine());
        while (!"@END@".equals(lines.getLast())){
            ids.add(lines.getLast());
            lines.add(lineReader.readLine());
        }
        log.info("getMessage -> {}", lines);
        serverMessages.add(lines);
        ids.forEach(ServerSession::new);
        controller.refreshClientsTable(ServerSession.getSessions());
    }

    @SneakyThrows
    private void managersMessageHandler(List<String> lines, BufferedReader lineReader) {

    }

    @SneakyThrows
    private void developersMessageHandler(List<String> lines, BufferedReader lineReader) {

    }

    @Override
    @SneakyThrows
    public void close() {
        socket.close();
    }
}