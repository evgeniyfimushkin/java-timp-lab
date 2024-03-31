package edu.evgen.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/session")
public class SessionController {

    @Autowired
    Processor processor;

    @GetMapping
    List<Session> getSessions() {
        return processor.getSessions();
    }
}
