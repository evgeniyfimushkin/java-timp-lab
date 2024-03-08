package edu.evgen;

import edu.evgen.habitat.Habitat;
import edu.evgen.habitat.HabitatConfiguration;
import edu.evgen.habitat.HabitatImpl;
import edu.evgen.habitat.employee.IBehaviour;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class SceneController {

    final HabitatConfiguration configuration = HabitatConfiguration.builder()
            .managerRatio(1.0)
            .managerDelay(2L)
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
    public MenuItem
            menuStartItem,
            menuStopItem,
            helpMeItem;
    @FXML
    public TextField developersDelayTextField, managersDelayTextField;
    @FXML
    Label             simulationTime,
            developersCountLabel, managersCountLabel,
            managersDelayLabel, developersDelayLabel,
            developersProbabilityLabel, managersRatioLabel;

    @FXML
    Pane habitatPane;
    final Long processDelay = 100L;

    public boolean run = false;
    Thread livingThread;
    Long startSimulationTime = System.currentTimeMillis(),
            startPauseTime = 0L,
            pausedTime = 0L;

    public void startSimulation(ActionEvent event) {
            pausedTime = 0L;
            startSimulationTime = System.currentTimeMillis();
            doSimulation(event);
    }

    public void doSimulation(ActionEvent event){
        stopButton.setDisable(false);
        startButton.setDisable(true);
        livingThread = new Thread(this::living);
        livingThread.start();
    }

    @FXML
    private void initialize() {
        stopButton.setDisable(true);
        log.info("controller init");
        refreshStatistic();
        simulationInfoCheckBox.setOnAction(this::toggleCheckBoxHandler);
        toggleCheckBoxHandler(null);
        startButton.setOnAction(this::startSimulation);
        menuStartItem.setOnAction(event -> startButton.fire());
        developersApplyButton.setOnAction(event -> developersApplyButtonAction());
        managersApplyButton.setOnAction(event -> managersApplyButtonAction());


        menuItemStream().forEach(developersProbabilityMenu.getItems()::add);
        menuItemStream().forEach(managersRatioMenu.getItems()::add);
        developersProbabilityMenu.getItems().forEach(menuItem -> menuItem.setOnAction(this::setupDevelopersProbability));
        managersRatioMenu.getItems().forEach(menuItem -> menuItem.setOnAction(this::setupManagersRatio));


        helpMeItem.setOnAction(this::helpMeItemAction);
        radioButtonShowTime.fire();
        radioButtonShowTime.setOnAction(event -> simulationTime.setVisible(true));
        radioButtonHideTime.setOnAction(event -> simulationTime.setVisible(false));
    }

    void toggleCheckBoxHandler(ActionEvent event) {
        if (simulationInfoCheckBox.isSelected()) {
            stopButton.setText("Pause");
            stopButton.setOnAction(this::showSimulationInfoForm);
            menuStopItem.setOnAction(this::showSimulationInfoForm);
        } else {
            stopButton.setText("Stop");
            stopButton.setOnAction(this::stopHandler);
            menuStopItem.setOnAction(this::stopHandler);
        }
    }

    private void setupManagersRatio(ActionEvent event) {
        MenuItem sourceMenuItem = (MenuItem) event.getSource();
        configuration.setManagerRatio(Double.parseDouble(sourceMenuItem.getText()));
        managersRatioMenu.setText(sourceMenuItem.getText());
        refreshStatistic();
    }

    private void setupDevelopersProbability(ActionEvent event){
        MenuItem sourceMenuItem = (MenuItem) event.getSource();
        configuration.setDeveloperProbability(Double.parseDouble(sourceMenuItem.getText()));
        developersProbabilityMenu.setText(sourceMenuItem.getText());
        refreshStatistic();
    }

    Stream<MenuItem> menuItemStream(){
        return IntStream.rangeClosed(1, 10)
                //сделать объект из примитива
                .boxed()
        //        Double divide10(Integer i) {
        //            return i / 10.0;
        //        }
                .map(i -> i / 10.0)
                .map(Object::toString)
                .map(MenuItem::new);
        //стрим не финализирован
    }
@SneakyThrows
    void helpMeItemAction(ActionEvent event) {
        final Stage errorStage = new Stage();
        log.info("helpMe");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/helpme.fxml"));
        errorStage.setScene(new Scene(loader.load()));
        errorStage.show();
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
    @SneakyThrows
    void errorSceneStart(Throwable exception) {
        log.info(exception.getClass().toString());
        final Stage errorStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/error.fxml"));
        errorStage.setScene(new Scene(loader.load()));
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

    void stopHandler(ActionEvent event) {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        log.info("stopRun");
        run = false;
        livingThread.interrupt();
        log.info("clear");
        habitat.clear();
        Platform.runLater(habitatPane.getChildren()::clear);
        sleep();
        sleep();
        sleep();
        sleep();
        log.info("refresh");
        Platform.runLater(this::refreshStatistic);
        log.info("stopRun ->");
    }

    @SneakyThrows
    void showSimulationInfoForm(ActionEvent rootEvent) {
        log.info("new window Stop simulation Info");

        final Stage formStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/stopSimulationInfo.fxml"));
        formStage.setScene(new Scene(loader.load()));
        stopSimulationInfoController controller = loader.getController();
        controller.setAllTheLabels(habitat);
        controller.simulationTime.setText("Simulation time: " + getSimulationTime());
        startPauseTime = System.currentTimeMillis();
        run = false;
        controller.stopButtonFromInfo.setOnAction(event -> {
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            formStage.close();
            stopHandler(rootEvent);
        });

        controller.continueButton.setOnAction(event -> {
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            formStage.close();
            doSimulation(rootEvent);
        });
        formStage.show();

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
        return  ( System.currentTimeMillis() - pausedTime - startSimulationTime) / 1000;
    }

    void setSimulationTimeVisible() {
        if (simulationTime.isVisible()) {
            radioButtonHideTime.fire();
        } else {
            radioButtonShowTime.fire();
        }
    }
}