package edu.evgen;

import edu.evgen.habitat.Habitat;
import edu.evgen.habitat.HabitatConfiguration;
import edu.evgen.habitat.HabitatImpl;
import edu.evgen.habitat.employee.IBehaviour;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class SceneController {

    final HabitatConfiguration configuration = HabitatConfiguration.builder()
            .managerRatio(1.0)
            .managerDelay(1L)
            .developerDelay(1L)
            .developerProbability(1.0)
            .paneSize(400L)
            .build();
    final Habitat habitat = new HabitatImpl(configuration);
    @FXML
    public Button startButton, stopButton, developersApplyButton, managersApplyButton;
    @FXML
    public MenuButton developersProbabilityMenu, managersRatioMenu;
    @FXML
    public RadioButton radioButtonHideTime, radioButtonShowTime;
    @FXML
    public CheckBox simulationInfoCheckBox;
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
    Label developerOptionLabel, managerOptionLAbel,
            simulationTime,
            developersCountLabel, managersCountLabel,
            managersDelayLabel, developersDelayLabel,
            developersProbabilityLabel, managersRatioLabel;


    @FXML
    Pane habitatPane;
    final Long processDelay = 100L;

    public boolean run = false;
    public boolean continueThread = false;
    Thread livingThread, informationThread;
    Long startSimulationTime = 0L,
            stopSimulationTime = 0L,
            startPauseTime = 0L,
            stopPauseTime = 0L,
            addedTime = 0L;

    public void doMoving() {// запуск в отдельном thread(асинхронное)
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
        startButton.setOnAction(event -> {
            addedTime = 0L;

            stopButton.setDisable(false);
            startButton.setDisable(true);
            doMoving();
        });
        menuStartItem.setOnAction(event -> startButton.fire());
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
        radioButtonShowTime.fire();
        radioButtonShowTime.setOnAction(event -> simulationTime.setVisible(true));
        radioButtonHideTime.setOnAction(event -> simulationTime.setVisible(false));

        simulationInfoCheckBox.setOnAction(event -> {
            if (simulationInfoCheckBox.isSelected())
                stopButton.setText("Info");
            else {
                stopButton.setText("Stop");
            }
        });
    }

    void helpMeItemAction() {
        final Stage errorStage = new Stage();
        log.info("helpMe");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/helpme.fxml"));
        Optional.ofNullable(loader)
                .map(this::loadSafe)
                .map(Scene::new)
                .ifPresent(errorStage::setScene);
        errorStage.show();
    }

    @SneakyThrows
    Parent loadSafe(FXMLLoader fxmlLoader) {
        return fxmlLoader.load();
    }

    void setdevelopersProbabilityMenuAction(MenuItem item) {
        log.info("setDevelopersProbabilityMenuAction");
        configuration.setDeveloperProbability(Double.parseDouble(item.getText()));
        developersProbabilityMenu.setText(item.getText());
        refreshStatistic();
    }

    void setManagersRatioMenuAction(MenuItem item) {
        log.info("setManagerRatioMenuAction");
        configuration.setManagerRatio(Double.parseDouble(item.getText()));
        managersRatioMenu.setText(item.getText());
        refreshStatistic();
    }

    void developersApplyButtonAction() {
        log.info("setDevelopersApplyButtonAction");
        if (!developersDelayTextField.getText().isEmpty()) {
            try {
                configuration.setDeveloperDelay(Long.parseLong(developersDelayTextField.getText()));
            } catch (NumberFormatException empty) {
                log.info("NumberFormatException ignored");
                errorSceneStart(empty);
            }
            refreshStatistic();
        }
        developersDelayTextField.clear();
    }

    void errorSceneStart(Throwable exception) {
        log.info(exception.getClass().toString());
        final Stage errorStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/error.fxml"));
        Optional.ofNullable(loader)
                .map(this::loadSafe)
                .map(Scene::new)
                .ifPresent(errorStage::setScene);
        errorStage.show();
        ErrorController controller = loader.getController();
        controller.initialize(exception);
        controller.closeErrorWindowButton.setOnAction(event -> errorStage.close());
    }

    void managersApplyButtonAction() {
        log.info("setManagersApplyButtonAction");
        if (!managersDelayTextField.getText().isEmpty()) {
            try {
                configuration.setManagerDelay(Long.parseLong(managersDelayTextField.getText()));
            } catch (NumberFormatException empty) {
                log.info("NumberFormatException");
                errorSceneStart(empty);
            }
            refreshStatistic();

        }
        managersDelayTextField.clear();
    }

    void living() {
        log.info("start living");
        run = true;
        if (!continueThread)
            startSimulationTime = System.currentTimeMillis();
        try {
            do {
                sleep();
                log.info("living:birthAttempt");
                Platform.runLater(this::birthAttempt);
            } while (run);
        } catch (Throwable ignore) {
        }
        log.info("stop living");
    }

    void birthAttempt() {
        if (run) {
            log.info("birthAttempt");
            habitat.birthAttempt()
                    .map(IBehaviour::getImageView)
                    .ifPresent(habitatPane.getChildren()::add);//метод референс, чтобы стало consumer
            refreshStatistic();
        }
    }

    //consumer ждёт аргумент и ничего не возвращает
    // runnable - void без аргументов

    void stopRun() {
        stopSimulationTime = System.currentTimeMillis();
        if (simulationInfoCheckBox.isSelected()){
            log.info("new window Stop simulation Info");

            final Stage stopStage = new Stage();
            FXMLLoader stopWindowLoader = new FXMLLoader(getClass().getResource("/stopSimulationInfo.fxml"));
            Optional.ofNullable(stopWindowLoader)
                    .map(this::loadSafe)
                    .map(Scene::new)
                    .ifPresent(stopStage::setScene);
            stopStage.show();
            stopSimulationInfoController controller = stopWindowLoader.getController();
            controller.setAllTheLabels(habitat);
            controller.simulationTime.setText("Simulation time: " + getSimulationTime());
            startPauseTime = System.currentTimeMillis();
            run=false;
            controller.stopButtonFromInfo.setOnAction(event -> {
                addedTime = addedTime +System.currentTimeMillis() - startPauseTime;
                continueThread = false;
                stopStage.close();
                kilingThread();
            });

            controller.continueButton.setOnAction(event -> {
                addedTime = addedTime +System.currentTimeMillis() - startPauseTime;
                stopStage.close();
                continueThread = true;
                doMoving();
            });

        }
        else {
            kilingThread();
        }
    }
    void kilingThread(){
        {
            startButton.setDisable(false);
            stopButton.setDisable(true);
//            if (run)
//                stopSimulationTime = System.currentTimeMillis();
            log.info("stopRun");
            run = false;
            livingThread.interrupt();

            log.info("clear");
            habitat.clear();
            Platform.runLater(habitatPane.getChildren()::clear);//метод референс -> runnable
            sleep();
            sleep();
            sleep();
            sleep();
            log.info("refresh");
            Platform.runLater(this::refreshStatistic);
            log.info("stopRun ->");
        }
    }
    void refreshStatistic() {

        log.info("RefreshStatistics");

        developersCountLabel.setText(habitat.getDeveloperCount().toString());
        managersCountLabel.setText(habitat.getManagerCount().toString());

        managersDelayLabel.setText("Managers Delay = " + configuration.getManagerDelay());
        developersDelayLabel.setText("Developers Delay = " + configuration.getDeveloperDelay());

        developersProbabilityLabel.setText("Probability = " + configuration.getDeveloperProbability());
        managersRatioLabel.setText("Ratio = " + configuration.getManagerRatio());

        simulationTime.setText("Simulation time: " + getSimulationTime());
    }

    @SneakyThrows
    void sleep() {
        Thread.sleep(processDelay);
    }

    Long getSimulationTime() {
        if (run)
            return startSimulationTime == 0 ?
                    0L :
                    (System.currentTimeMillis() - (addedTime) - startSimulationTime) / 1000;
        return (stopSimulationTime - (addedTime) - startSimulationTime) / 1000;
    }

    void setSimulationTimeVisible() {
        if (simulationTime.isVisible()) {
            radioButtonHideTime.fire();
        } else {
            radioButtonShowTime.fire();
        }
    }
}