package edu.evgen.client;

import edu.evgen.habitat.employee.Employee;
import lombok.Data;

import java.io.Serializable;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
@Data
public class Message implements Serializable {
    private List<?> list;
    //DTO
    private MessageMarkers marker;
    private String sender;
    private String recipient;

    public Message(MessageMarkers marker,String sender, String recipient, List<?> list) {
        this.marker = marker;
        this.sender = sender;
        this.recipient = recipient;
        this.list = list;
    }
}