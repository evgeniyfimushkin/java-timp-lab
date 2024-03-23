package edu.evgen.network;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Client {
    private static final Integer PORT = 19000;
    private static final String HOST = "localhost";
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private Boolean isConnected;
    private List<Socket> connectedClientsIds;
    public Client(){
        this.connectedClientsIds = new LinkedList<>();
        this.isConnected = false;
    }

    public Boolean isConnected() {return isConnected;}
    public void connect(String address, int port) throws Exception{
        this.socket = new Socket(address, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        Object temp = in.readObject();
        System.out.println(temp.toString());
    }
    public void receiveClientsList(ObjectInputStream in){
    }
}