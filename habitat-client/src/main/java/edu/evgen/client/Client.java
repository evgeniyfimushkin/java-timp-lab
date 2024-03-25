package edu.evgen.client;

import edu.evgen.SceneController;
import edu.evgen.habitat.Simulation;
import edu.evgen.habitat.employee.Developer;
import edu.evgen.habitat.employee.EmployeesRepository;
import edu.evgen.habitat.employee.Manager;
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
    private void exchangeRequest(List<String> lines, BufferedReader lineReader) {

        log.info("switch <- {}", lines.getLast());
        lines.add(lineReader.readLine());
        String clientId = lines.getLast();
        log.info("exchangeRequest <- from {}", clientId);

        ObjectInputStream objectInputStream;
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        Object object;
        while ((object = (Manager) objectInputStream.readObject()) != null) {
//            log.info("ADDDDDEDD");
            new Manager((Manager) object);
        }
        log.info("SEND DEVS");
        sendDevelopers(clientId);
    }

    @SneakyThrows
    private void exchangeReply(List<String> lines, BufferedReader lineReader) {

        log.info("switch <- {}", lines.getLast());
        lines.add(lineReader.readLine());
        String clientId = lines.getLast();
        log.info("exchangeReply <- from {}", clientId);

        ObjectInputStream objectInputStream;
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        Object object;
        while ((object = (Developer) objectInputStream.readObject()) != null) {
            new Developer((Developer) object);
        }
        log.info("DEVS GETS");

    }

    @SneakyThrows
    public void sendDevelopers(String recipient) {
        PrintStream outputStreamStr = new PrintStream(socket.getOutputStream());
        outputStreamStr.println("@EXCHANGEREPLY@");
        outputStreamStr.println(recipient);

        ObjectOutputStream outputStream;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        for (int i = 0; i < EmployeesRepository.getDevelopers().size(); i++) {
            outputStream.writeObject(EmployeesRepository.getDevelopers().get(i));
        }
        outputStream.writeObject(null);
        EmployeesRepository.getDevelopers().forEach(EmployeesRepository::removeEmployee);
    }
    @SneakyThrows
    public void sendManagers(String recipient) {
        PrintStream outputStreamStr = new PrintStream(socket.getOutputStream());
        outputStreamStr.println("@EXCHANGEREQUEST@");
        outputStreamStr.println(recipient);

        ObjectOutputStream outputStream;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        for (int i = 0; i < EmployeesRepository.getManagers().size(); i++) {
            outputStream.writeObject(EmployeesRepository.getManagers().get(i));
        }
        outputStream.writeObject(null);
        EmployeesRepository.getManagers().forEach(EmployeesRepository::removeEmployee);
    }
        @SneakyThrows
        public void pullMessage () {
            BufferedReader lineReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            List<String> lines = new ArrayList<>();
            lines.add(lineReader.readLine());
            log.info("new message {}" , lines.getLast());
            while (true) {
                switch (lines.getLast()) {
                    case "@SESSIONS@":
                        sessionsMessageHandler(lines, lineReader);
                        break;
                    case "@EXCHANGEREQUEST@":
                        exchangeRequest(lines, lineReader);
                        break;
                    case "@EXCHANGEREPLY@":
                        exchangeReply(lines, lineReader);
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
//            log.info("Buffer is empty");

        }

        @SneakyThrows
        private void getIdFromServer (List < String > lines, BufferedReader lineReader){
            lines.add(lineReader.readLine());
            id = lines.getLast();
            lines.add(lineReader.readLine());
            serverMessages.add(lines);
            log.info("getIdFromServer -> {}", id);
            controller.printId(id);
        }

        @SneakyThrows
        private void sessionsMessageHandler (List < String > lines, BufferedReader lineReader){
            List<String> ids = new ArrayList<>();
            lines.add(lineReader.readLine());
            while (!"@END@".equals(lines.getLast())) {
                ids.remove(lines.getLast());
                ids.add(lines.getLast());
                lines.add(lineReader.readLine());
            }
            log.info("getMessage -> {}", ids);
            serverMessages.add(lines);
            ids.forEach(ServerSession::new);
            controller.refreshClientsTable(ServerSession.getSessions());
        }

        @SneakyThrows
        private void managersMessageHandler (List < String > lines, BufferedReader lineReader){

        }

        @SneakyThrows
        private void developersMessageHandler (List < String > lines, BufferedReader lineReader){

        }


        @Override
        @SneakyThrows
        public void close () {
            controller.clientsTextArea.setText("Connection Lost");
            controller.clientIdLabel.setText("id: null");
            controller.networkStatusLabel.setText("Status: Offline");
            socket.close();
        }
    }