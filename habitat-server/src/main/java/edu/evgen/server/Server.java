package edu.evgen.server;

import edu.evgen.client.Message;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static edu.evgen.client.MessageMarkers.SESSIONS;
import static edu.evgen.client.MessageMarkers.SETID;

@Service
//@RequiredArgsConstructor
@Slf4j
@NoArgsConstructor
public class Server {
    @Value("${app.port:1900}")
    private Integer port;
//    private Boolean run;

    @Autowired
    Processor processor;


    ServerSocket serverSocket;

    @PostConstruct
    @SneakyThrows
    public void init() {
       log.info("I'm created on port {}", port);
        serverSocket = new ServerSocket(port);
    }

    @SneakyThrows
    @Scheduled(fixedDelay = 1)
    public void acceptClient() {
        log.info("Server is waiting for connections on port {}", port);
        processor.processSocket(serverSocket.accept());
    }

}
