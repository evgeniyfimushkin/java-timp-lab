package edu.evgen;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.awt.*;

public class TerminalController {
    @FXML
    TextField commandField;
    @FXML
    TextArea textArea;

    private void output(String message){
        textArea.appendText(message + "\n");
    }
    private void processCommand(String command) {
        String[] tokens = command.split(" ");
        switch (tokens[0]) {
            case "help" -> {

            }
            case "hire" -> {

            }
            case "fire" -> {

            }
            case "exit" -> {

            }
            default -> {
                output("No such a command: " + command);
            }
        }
    }

    private void setKeyActions() {
        commandField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String command = commandField.getText().trim();
                textArea.appendText("> " + command + "\n");
                commandField.clear();
                processCommand(command);
            }
        });
    }

    @FXML
    public void initialize() {
        setKeyActions();
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setFocusTraversable(false);
        commandField.requestFocus();
    }
}
