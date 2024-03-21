package edu.evgen.Network;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Session extends Thread {
    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final Long id;
    public Session(Socket socket, Long sessionId){
        this.id = sessionId;
        this.socket = socket;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    @SneakyThrows
    public void run(){
        try {
            while (!socket.isClosed()){
                //выполнение сесии
            }
        }catch (Exception e){
            e.printStackTrace();
            Server.getSessions().remove(this);
            socket.close();
        }
    }
}
