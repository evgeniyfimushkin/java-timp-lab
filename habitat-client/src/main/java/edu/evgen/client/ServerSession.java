package edu.evgen.client;

import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ServerSession{
    @Getter
    public String id;
    @Getter
    public Button button;
    @Getter
    public static List<ServerSession> sessions = new ArrayList<>();
    public ServerSession(String id) {
        this.id = id;
        sessions.add(this);
    }
}
