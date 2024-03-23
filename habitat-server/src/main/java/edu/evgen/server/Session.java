package edu.evgen.server;

import java.net.Socket;


public record Session(Socket socket, String id) {}
