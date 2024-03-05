package edu.evgen;

import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

@Slf4j
public class ErrorController {
    public Label errorMessage;
    public Button closeErrorWindowButton;


    @FXML
    public void initialize(Throwable e) {
        if (e instanceof NumberFormatException) {
            StringBuilder str = new StringBuilder(e.toString());
            str.replace(0, 50, "You can't print ");
            str.append(", stupid!");
            str.append("\nUse numbers, ashole!");
            errorMessage.setText(str.toString());
        }
        else
            errorMessage.setText("i don't know what you did if you see this message. Get you ass out of here!");
    }

}
