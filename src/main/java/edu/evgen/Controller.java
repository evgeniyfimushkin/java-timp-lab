package edu.evgen;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import java.awt.*;

public class Controller{
    StringBuilder str = new StringBuilder();
    @FXML
    public Button mainButtons;
    @FXML
    Button button;
    @FXML
    Label mainTitle;
    @FXML
    private void buttonClicked() {
        button = mainButtons;
        button.setText("AGAIN" + str.toString());
        str.append(" AND AGAIN");
        mainTitle.setText("HUY"+ str.toString());
        System.out.println("Button clicked!");
    }
}
