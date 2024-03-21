package edu.evgen.Network;

import edu.evgen.habitat.HabitatImpl;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static edu.evgen.habitat.HabitatImpl.habitat;

public class Client {
    private static final Integer PORT = 19000;
    private static final String HOST = "localhost";
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private Boolean isConnected;
    private List<Long> connectedClientsIds;
}