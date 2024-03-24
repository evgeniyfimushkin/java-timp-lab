package edu.evgen.client;

import edu.evgen.SceneController;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ServerSession {
    @Getter
    public String id;
    @Getter
    public Button button;
    @Getter
    public static List<String> sessions = new ArrayList<>();

    public ServerSession(String id) {
        this.id = id;
        if (!sessions.contains(this.id))
            sessions.add(this.id);
    }
}
