package edu.evgen;

import edu.evgen.habitat.Habitat;
import edu.evgen.habitat.HabitatImpl;
import edu.evgen.habitat.employee.IBehaviour;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SceneController {
    final Habitat habitat = new HabitatImpl(3L,3L,0.9,0.3,400L);
    @FXML
    public Button startButton, stopButton, developersApplyButton, managersApplyButton;
    @FXML
    public MenuButton developersProbabilityMenu, managersRatioMenu;
    @FXML
    public MenuItem developersRatioMenu1,
            developersRatioMenu2,
            developersRatioMenu3,
            developersRatioMenu4,
            developersRatioMenu5,
            developersRatioMenu6,
            developersRatioMenu7,
            developersRatioMenu8,
            developersRatioMenu9,
            developersRatioMenu10,
            managersRatioMenu1,
            managersRatioMenu2,
            managersRatioMenu3,
            managersRatioMenu4,
            managersRatioMenu5,
            managersRatioMenu6,
            managersRatioMenu7,
            managersRatioMenu8,
            managersRatioMenu9,
            managersRatioMenu10,
            menuStartItem,
            menuStopItem,
            helpMeItem;
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
    Thread livingThread, informationThread;
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
        menuStopItem.setOnAction(event -> stopRun());
        startButton.setOnAction(event -> doMoving());
        menuStartItem.setOnAction(event -> doMoving());
        switchButton.setOnAction(event -> setSimulationTimeVisible());
        developersApplyButton.setOnAction(event -> developersApplyButtonAction());
        managersApplyButton.setOnAction(event -> managersApplyButtonAction());



        developersRatioMenu1.setOnAction(event -> setdevelopersProbabilityMenuAction(developersRatioMenu1));
        developersRatioMenu2.setOnAction(event -> setdevelopersProbabilityMenuAction(developersRatioMenu1));
        developersRatioMenu3.setOnAction(event -> setdevelopersProbabilityMenuAction(developersRatioMenu2));
        developersRatioMenu4.setOnAction(event -> setdevelopersProbabilityMenuAction(developersRatioMenu3));
        developersRatioMenu5.setOnAction(event -> setdevelopersProbabilityMenuAction(developersRatioMenu4));
        developersRatioMenu6.setOnAction(event -> setdevelopersProbabilityMenuAction(developersRatioMenu5));
        developersRatioMenu7.setOnAction(event -> setdevelopersProbabilityMenuAction(developersRatioMenu6));
        developersRatioMenu8.setOnAction(event -> setdevelopersProbabilityMenuAction(developersRatioMenu7));
        developersRatioMenu9.setOnAction(event -> setdevelopersProbabilityMenuAction(developersRatioMenu8));
        developersRatioMenu10.setOnAction(event -> setdevelopersProbabilityMenuAction(developersRatioMenu10));
        managersRatioMenu1.setOnAction(event -> setManagersRatioMenuAction(managersRatioMenu1));
        managersRatioMenu2.setOnAction(event -> setManagersRatioMenuAction(managersRatioMenu2));
        managersRatioMenu3.setOnAction(event -> setManagersRatioMenuAction(managersRatioMenu3));
        managersRatioMenu4.setOnAction(event -> setManagersRatioMenuAction(managersRatioMenu4));
        managersRatioMenu5.setOnAction(event -> setManagersRatioMenuAction(managersRatioMenu5));
        managersRatioMenu6.setOnAction(event -> setManagersRatioMenuAction(managersRatioMenu6));
        managersRatioMenu7.setOnAction(event -> setManagersRatioMenuAction(managersRatioMenu7));
        managersRatioMenu8.setOnAction(event -> setManagersRatioMenuAction(managersRatioMenu8));
        managersRatioMenu9.setOnAction(event -> setManagersRatioMenuAction(managersRatioMenu9));
        managersRatioMenu10.setOnAction(event -> setManagersRatioMenuAction(managersRatioMenu10));

        helpMeItem.setOnAction(event -> helpMeItemAction());
    }
    void helpMeItemAction(){


    }
    void setdevelopersProbabilityMenuAction(MenuItem item){
        log.info("setDevelopersProbabilityMenuAction");
        habitat.setDeveloperProbability(Double.parseDouble(item.getText()));
        developersProbabilityMenu.setText(item.getText());
        refreshStatistic();
    }
    void setManagersRatioMenuAction(MenuItem item){
        log.info("setManagerRatioMenuAction");
        habitat.setManagerRatio(Double.parseDouble(item.getText()));
        managersRatioMenu.setText(item.getText());
        refreshStatistic();
    }
    void developersApplyButtonAction(){
        log.info("setDevelopersApplyButtonAction");
        if(!developersDelayTextField.getText().isEmpty()){
            try {
                habitat.setDeveloperDelay(Long.parseLong(developersDelayTextField.getText()));
            }catch (NumberFormatException empty){log.info("NumberFormatException ignored");}
            refreshStatistic();
        }
        developersDelayTextField.clear();
    }
    void managersApplyButtonAction(){
        log.info("setManagersApplyButtonAction");
        if(!managersDelayTextField.getText().isEmpty()){
            try{
                habitat.setManagerDelay(Long.parseLong(managersDelayTextField.getText()));
            } catch (NumberFormatException empty){log.info("NumberFormatException ignored");}
            refreshStatistic();

        }
        managersDelayTextField.clear();
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

        log.info("RefreshStatistics");

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