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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class Habitat {
    final Random random = new Random();
    @FXML
    public Button startButton;
    @FXML
    public Button stopButton;

    @FXML
    Label mgrCount, devCount;

    @FXML
    Label mainTitle;

    @FXML
    Pane habitat;
    final Long processDelay = 100L;

    boolean run = false;

    final Manager manager = new Manager(3L,400L);

    final Developer developer = new Developer(2L, 1.0, 300L);

    @FXML
    EventHandler<ActionEvent> buttonClickHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            log.info("OnAction {}", e);
        }
    };

//    EventHandler<ActionEvent> lambdaClickHandler = eventone -> log.info("OnAction {}", eventone.getSource());


    public void doMoving(){// запуск в отдельном thread(асинхронное)
//        mainButton.setVisible(true);
        new Thread(this::moving).start();

    }
    @FXML
    private void initialize() {
        log.info("controller init");
        stopButton.setText("Stop");
        stopButton.setOnAction(event -> stopRun());//связали кнопку с обработчиком (inject)
        startButton.setOnAction(event -> doMoving());

    }

    void moving() {
        log.info("start moving");
        run = true;
        do {
            sleep();
            Platform.runLater(this::birthAttempt);
        } while (run);
//        IntStream.range(1, 10000)
//                .boxed()
//                .peek(x -> sleep())//чисто чтоб поспать peek - void consumer То есть ничего не возвращает
//                .peek(x -> log.info("Tick: {}", x))
//                .forEach(x -> Platform.runLater( this::birthAttempt));
        stopButton.setVisible(false);
        log.info("stop moving");
    }

    void birthAttempt() {
        log.info("birthAttempt");
//        mainButton.setText(i.toString());
//        double x = Button.getTranslateX();
//        double y = mainButton.getTranslateY();
//        mainButton.setTranslateX(x + moveStep());
//        mainButton.setTranslateY(y + moveStep());
        manager.birthAttempt()
                .map(IBehaviour::getImageView)
                .ifPresent(habitat.getChildren()::add);
        developer.birthAttempt()
                .map(IBehaviour::getImageView)
                .ifPresent(habitat.getChildren()::add);

        devCount.setText("Eployees: " + habitat.getChildren().stream().count());
        mgrCount.setText("Managers: " + habitat.getChildren()
                .stream()
                .map(ImageView.class::cast)
                .map(ImageView::getImage)
                .map(Image::getUrl)
                .filter(url -> url.contains("manager"))
                .count());
    }

    Integer moveStep(){
        return random.nextBoolean()?-20: 20;
    }

    void stopRun(){
        log.info("stopRun");
        run = false;
    }

    @SneakyThrows
    void sleep() {
        Thread.sleep(processDelay);
    }
}