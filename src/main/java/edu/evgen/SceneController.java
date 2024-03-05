package edu.evgen;

import edu.evgen.habitat.Habitat;
import edu.evgen.habitat.HabitatImpl;
import edu.evgen.habitat.employee.IBehaviour;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;

@Slf4j
public class SceneController {
    final Habitat habitat = new HabitatImpl(3L,3L,0.9,0.3,400L);
    @FXML
    public Button startButton, stopButton, developersApplyButton, managersApplyButton;
    @FXML
    public MenuButton developersProbabilityMenu, managersRatioMenu;
    @FXML
    public TextField developersDelayTextField, managersDelayTextField;
    @FXML
    public ToggleButton switchButton;
    @FXML
    Label developerOptionLabel,managerOptionLAbel,
            simulationTime,
            developersCountLabel,managersCountLabel,
            managersDelayLabel, developersDelayLabel,
            developersProbabilityLabel, managersRatioLabel;


    @FXML
    Pane habitatPane;
    final Long processDelay = 100L;

    boolean run = false;
    Thread livingThread;
    Long startSimulationTime = 0L, stopSimulationTime = 0L;

    public void doMoving(){// запуск в отдельном thread(асинхронное)
        stopButton.setDisable(false);
        startButton.setDisable(true);
        if (!run) {
            livingThread = new Thread(this::living);
            livingThread.start();
        }
    }
    @FXML
    private void initialize() {
        stopButton.setDisable(true);
        log.info("controller init");
        refreshStatistic();
        stopButton.setText("Stop");
        stopButton.setOnAction(event -> stopRun());//связали кнопку с обработчиком (inject)
        startButton.setOnAction(event -> doMoving());
        switchButton.setOnAction(event -> setSimulationTimeVisible());
        developersApplyButton.setOnAction(event -> developersApplyButtonAction());
        managersApplyButton.setOnAction(event -> managersApplyButtonAction());
    }
    void developersApplyButtonAction(){
        log.info("setDevelopersApplyButtonAction");
        habitat.setDeveloperDelay(Long.parseLong(developersDelayTextField.getText()));
    }
    void managersApplyButtonAction(){
        log.info("setManagersApplyButtonAction");
        habitat.setManagerDelay(Long.parseLong(managersDelayTextField.getText()));
    }
    void living() {
        log.info("start living");
        run = true;
        startSimulationTime = System.currentTimeMillis();
        try {
            do {
                sleep();
                Platform.runLater(this::birthAttempt);
            } while (run);
        } catch (Throwable ignore){}
        log.info("stop living");
    }

    void birthAttempt() {
        log.info("birthAttempt");
        habitat.birthAttempt()
                .map(IBehaviour::getImageView)
                .ifPresent(habitatPane.getChildren()::add);//метод референс, чтобы стало consumer
        refreshStatistic();
    }
    //consumer ждёт аргумент и ничего не возвращает
    // runnable - void без аргументов
    void stopRun(){
        startButton.setDisable(false);
        stopButton.setDisable(true);
        if (run)
            stopSimulationTime = System.currentTimeMillis();
        log.info("stopRun");
        run = false;
        livingThread.interrupt();
        sleep();
        sleep();
        sleep();
        log.info("clear");
        habitat.clear();
        Platform.runLater(habitatPane.getChildren()::clear);//метод референс -> runnable
        sleep();
        log.info("refresh");
        Platform.runLater(this::refreshStatistic);
        log.info("stopRun ->");
    }
    void refreshStatistic(){
        developersCountLabel.setText(habitat.getDeveloperCount().toString());
        managersCountLabel.setText(habitat.getManagerCount().toString());

        managersDelayLabel.setText("Managers Delay = " + habitat.getManagerDelay());
        developersDelayLabel.setText("Developers Delay = " + habitat.getDeveloperDelay());

        developersProbabilityLabel.setText("Probability = " + habitat.getDeveloperProbability());
        managersRatioLabel.setText("Ratio = " + habitat.getManagerRatio());

        simulationTime.setText("Simulation time: " + getSimulationTime());
    }
    @SneakyThrows
    void sleep() {
            Thread.sleep(processDelay);
    }
    Long getSimulationTime(){
        if (run)
        return startSimulationTime == 0 ?
                0L :
                (System.currentTimeMillis()-startSimulationTime)/1000;
        return (stopSimulationTime - startSimulationTime)/1000;
    }
    void setSimulationTimeVisible(){
        if (simulationTime.isVisible()) {
            switchButton.setText("Show Time");
            simulationTime.setVisible(false);
        }
        else{
            switchButton.setText("Hide Time");
            simulationTime.setVisible(true);
        }
    }
}