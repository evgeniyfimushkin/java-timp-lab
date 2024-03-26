package edu.evgen.client;

import edu.evgen.SceneController;
import edu.evgen.habitat.Simulation;
import edu.evgen.habitat.employee.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.*;

import static edu.evgen.client.MessageMarkers.*;

@RequiredArgsConstructor
@Slf4j
public class Client implements Closeable {

    private final Socket socket;

    private Simulation simulation;

    @Getter
    private final List<List<String>> serverMessages = new ArrayList<>();
    private final SceneController controller;
    public String id;
    private Boolean run=true;

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
    private void exchange(Message message) {
        if (message.getMarker() == EXCHANGEREQUEST) {
            for (Object iter : message.getList()) {
                new Manager((Manager) iter);
            }
            List<IBehaviour> list = new ArrayList<>();
            EmployeesRepository.getDevelopers().forEach(list::add);
            Message messageReply = new Message(EXCHANGEREPLY, id, message.getSender(), list);
            ObjectOutputStream outputStream;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(messageReply);
            EmployeesRepository.getDevelopers().forEach(EmployeesRepository::removeEmployee);
            log.info("REPLY");
        } else if (message.getMarker() == EXCHANGEREPLY) {
            for (Object iter : message.getList()) {
                new Developer((Developer) iter);
            }
        }
    }

    @SneakyThrows
    public void sendManagers(String recipient) {
        log.info("REQUEST");
        List<IBehaviour> list = new ArrayList<>();
        EmployeesRepository.getManagers().forEach(list::add);
        Message message = new Message(EXCHANGEREQUEST, id, recipient, list);
        ObjectOutputStream outputStream;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(message);
        EmployeesRepository.getManagers().forEach(EmployeesRepository::removeEmployee);
    }

    @Synchronized
    @SneakyThrows
    public void pullMessage() {
        while (run) {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) objectInputStream.readObject();

            if (message == null) {
                continue;
            }

            switch (message.getMarker()) {
                case SESSIONS:
                    sessionsMessageHandler(message);
                    break;
                case EXCHANGEREQUEST:
                    exchange(message);
                    break;
                case EXCHANGEREPLY:
                    exchange(message);
                    break;
                case SETID:
                    getIdFromServer(message);
                    break;
                default:
                    break;
            }
        }
    }
    @SneakyThrows
    public void disconnectClient(){
        log.info("DISCONNECT");
        run = false;
        Thread.sleep(1000L);
        simulation.stopSimulation();
        simulation = null;
        Message message = new Message(DISCONNECT, id, id, null);
        ObjectOutputStream outputStream;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(message);
        close();
    }
    @SneakyThrows
    private void getIdFromServer(Message message) {
        id = (String) message.getList().getLast();
        log.info("getIdFromServer -> {}", id);
        controller.printId(id);
    }
    @SneakyThrows
    private void sessionsMessageHandler(Message message) {
        controller.refreshClientsTable((List<String>) message.getList());
    }
    @Override
    @SneakyThrows
    public void close() {
        controller.clientsTextArea.setText("Connection Lost");
        controller.clientIdLabel.setText("id: null");
        controller.networkStatusLabel.setText("Status: Offline");
    }
}