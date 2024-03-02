package edu.evgen;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.stream.IntStream;

@Slf4j
public class SceneController {
    final Random random = new Random();
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


    public void doMoving(){// запуск в отдельном thread(асинхронное)
        new Thread(this::moving).start();
    }
    @FXML
    private void initialize() {
        log.info("controller init");
        mainButton.setText("Click me");
        mainButton.setOnAction(eventone -> log.info("OnAction {}", eventone.getSource()));//связали кнопку с обработчиком (inject)

    }

    void moving() {
        log.info("start moving");
        IntStream.range(1, 100)
                .boxed()
                .peek(x -> sleep())//чисто чтоб поспать peek - void consumer То есть ничего не возвращает
                .peek(x -> log.info("Tick: {}", x))
                .forEach((x) -> Platform.runLater(() -> moveTo(x)));
        log.info("stop moving");
    }

    void moveTo(Integer i) {
        mainButton.setText(i.toString());
        double x = mainButton.getTranslateX();
        double y = mainButton.getTranslateY();
        mainButton.setTranslateX(x + moveStep());
        mainButton.setTranslateY(y + moveStep());
    }

    Integer moveStep(){
        return random.nextBoolean()?-20: 20;
    }

    @SneakyThrows
    static void sleep() {
        Thread.sleep(300);
    }
}