package edu.evgen;

import edu.evgen.habitat.Habitat;
import edu.evgen.habitat.HabitatImpl;
import edu.evgen.habitat.employee.Developer;
import edu.evgen.habitat.employee.IBehaviour;
import edu.evgen.habitat.employee.Manager;
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
public class SceneController {
    final Habitat habitat = new HabitatImpl(3L,3L,0.9,0.3,400L);
    @FXML
    public Button startButton;
    @FXML
    public Button stopButton;

    @FXML
    Label mgrCount, devCount;


    @FXML
    Pane habitatPane;
    final Long processDelay = 100L;

    boolean run = false;

    public void doMoving(){// запуск в отдельном thread(асинхронное)
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
        log.info("stop moving");
    }

    void birthAttempt() {
        log.info("birthAttempt");
        habitat.birthAttempt()
                .map(IBehaviour::getImageView)
                .ifPresent(habitatPane.getChildren()::add);
        devCount.setText("Developers: " + habitat.getDeveloperCount());
        mgrCount.setText("Managers: " + habitat.getManagerCount());
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