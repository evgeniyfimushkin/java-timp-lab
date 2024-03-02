package edu.evgen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SceneController {
    StringBuilder str = new StringBuilder();
    @FXML
    public Button mainButton;
    @FXML
    Label mainTitle;

    @FXML
    EventHandler<ActionEvent> buttonClickHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            log.info("OnAction {}", e);
        }
    };

    EventHandler<ActionEvent> lambdaClickHandler = eventone -> log.info("OnAction {}", eventone.getSource());



    @FXML
    private void initialize() {
        log.info("controller init");
        mainButton.setText("Click me");
        mainButton.setOnAction(eventone -> log.info("OnAction {}", eventone.getSource()));//связали кнопку с обработчиком (inject)
    }
}