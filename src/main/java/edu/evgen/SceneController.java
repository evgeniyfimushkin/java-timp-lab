package edu.evgen;

import edu.evgen.habitat.Developer;
import edu.evgen.habitat.IBehaviour;
import edu.evgen.habitat.Manager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.stream.IntStream;

@Slf4j
public class SceneController {
    final Random random = new Random();
    @FXML
    public Button startButton;
    @FXML
    public Button mainButton;
    @FXML
    Label mainTitle;

    @FXML
    Pane habitat;

    final Manager manager = new Manager(3l);

    final Developer developer = new Developer(2l, 1.0);

    @FXML
    EventHandler<ActionEvent> buttonClickHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            log.info("OnAction {}", e);
        }
    };

    EventHandler<ActionEvent> lambdaClickHandler = eventone -> log.info("OnAction {}", eventone.getSource());


    public void doMoving(){// запуск в отдельном thread(асинхронное)
//        mainButton.setVisible(true);
        new Thread(this::moving).start();

    }
    @FXML
    private void initialize() {
        log.info("controller init");
        mainButton.setText("0");
        mainButton.setOnAction(lambdaClickHandler);//связали кнопку с обработчиком (inject)
        startButton.setOnAction(event -> doMoving());

    }

    void moving() {
        log.info("start moving");
        IntStream.range(1, 10000)
                .boxed()
                .peek(x -> sleep())//чисто чтоб поспать peek - void consumer То есть ничего не возвращает
                .peek(x -> log.info("Tick: {}", x))
                .forEach(x -> Platform.runLater( this::birthattempt));

        mainButton.setVisible(false);
        log.info("stop moving");
    }

    void birthattempt() {
//        mainButton.setText(i.toString());
//        double x = mainButton.getTranslateX();
//        double y = mainButton.getTranslateY();
//        mainButton.setTranslateX(x + moveStep());
//        mainButton.setTranslateY(y + moveStep());
//        manager.birthAttempt()
//                .map(IBehaviour::getImageView)
//                .ifPresent(habitat.getChildren()::add);

        developer.birthAttempt()
                .map(IBehaviour::getImageView)
                .ifPresent(habitat.getChildren()::add);
    }

    Integer moveStep(){
        return random.nextBoolean()?-20: 20;
    }

    @SneakyThrows
    static void sleep() {
        Thread.sleep(300);
    }
}