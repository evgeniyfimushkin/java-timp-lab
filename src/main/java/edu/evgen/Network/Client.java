package edu.evgen.Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static final Integer PORT = 19000;
    public static final String HOST = "localhost";

    public static void main(String[] args) throws IOException {
        Socket socket = null;

        try {
            socket = new Socket(HOST, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            socket.close();
        }
    }
}
