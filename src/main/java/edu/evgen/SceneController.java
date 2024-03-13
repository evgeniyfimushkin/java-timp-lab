package edu.evgen;

import edu.evgen.habitat.HabitatConfiguration;
import edu.evgen.habitat.employee.Developer;
import edu.evgen.habitat.employee.Employee;
import edu.evgen.habitat.employee.IBehaviour;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static edu.evgen.habitat.HabitatImpl.habitat;

@Slf4j
public class SceneController {

    final HabitatConfiguration configuration = HabitatConfiguration.builder()
            .processDelay(100L)
            .managerRatio(1.0)
            .managerDelay(2L)
            .developerDelay(1L)
            .developerProbability(1.0)
            .paneSize(400L)
            .managerLivingTime(4L)
            .developerLivingTime(2L)
            .build();
    @FXML
    public Button startButton, stopButton, objectsInfoButton;
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
    public TextField developersDelayTextField, managersDelayTextField,
                managerLivingTime, developerLivingTime;
    @FXML
    Label             simulationTime,
            developersCountLabel, managersCountLabel,
            managersDelayLabel, developersDelayLabel,
            developersProbabilityLabel, managersRatioLabel;

    @FXML
    Pane habitatPane;

    public boolean run = false;
    Thread livingThread;
    Long startSimulationTime = System.currentTimeMillis(),
            startPauseTime = 0L,
            pausedTime = 0L;
    //Основные методы работы симуляции
    public void startSimulation(ActionEvent event) {
            pausedTime = 0L;
            startSimulationTime = System.currentTimeMillis();
            doSimulation(event);
    }

    public void doSimulation(ActionEvent event){
        fieldsSetDisable(true);
        livingThread = new Thread(this::living);
        livingThread.start();
    }
    void living() {
        log.info("start living");
        run = true;
        try {
            do {
                sleep();
                log.info("living:birthAttempt");
                Platform.runLater(this::birthAttempt);
                habitat.mustDie().forEach(this::kill);

            } while (run);
        } catch (Throwable ignore) {
        }
        log.info("stop living");
    }
    void kill(IBehaviour employee){
        Platform.runLater(() -> habitatPane.getChildren().remove(employee.getImageView()));
        habitat.getDevelopers().remove(employee);
        habitat.getManagers().remove(employee);
        Employee.getAllID().remove(employee.getId());
        Employee.getAllBirthTimes().remove(employee.getBirthTime());
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
    void stopHandler(ActionEvent event) {
        fieldsSetDisable(false);
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
    void fieldsSetDisable(boolean value){
        developersDelayTextField.setDisable(value);
        managersDelayTextField.setDisable(value);
        developerLivingTime.setDisable(value);
        managerLivingTime.setDisable(value);
        stopButton.setDisable(!value);
        startButton.setDisable(value);
        objectsInfoButton.setDisable(!value);
    }
    @FXML
    private void initialize() {
        habitat.setConfiguration(configuration);
        developersDelayTextField.textProperty().addListener(this::developersDelayOnChange);
        managersDelayTextField.textProperty().addListener(this::managersDelayOnChange);
        developerLivingTime.textProperty().addListener((this::developerLivingTimeOnChange));
        managerLivingTime.textProperty().addListener(this::managerLivingTimeOnChange);
        refreshStatistic();

        objectsInfoButton.setDisable(true);
        stopButton.setDisable(true);
        log.info("controller init");
        refreshStatistic();
        simulationInfoCheckBox.setOnAction(this::toggleCheckBoxHandler);
        toggleCheckBoxHandler(null);
        startButton.setOnAction(this::startSimulation);
        menuStartItem.setOnAction(event -> startButton.fire());
        objectsInfoButton.setOnAction(this::showObjectsInfoForm);


        menuItemStream().forEach(developersProbabilityMenu.getItems()::add);
        menuItemStream().forEach(managersRatioMenu.getItems()::add);
        developersProbabilityMenu.getItems().forEach(menuItem -> menuItem.setOnAction(this::setupDevelopersProbability));
        managersRatioMenu.getItems().forEach(menuItem -> menuItem.setOnAction(this::setupManagersRatio));


        helpMeItem.setOnAction(this::helpMeItemAction);
        radioButtonShowTime.fire();
        radioButtonShowTime.setOnAction(event -> simulationTime.setVisible(true));
        radioButtonHideTime.setOnAction(event -> simulationTime.setVisible(false));
    }

    //Методы реализующие объекты интерфейса
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

    void developersDelayOnChange(ObservableValue<?> observable, String oldValue, String newValue){
        try {
            configuration.setDeveloperDelay(Long.parseLong(newValue));
        } catch (NumberFormatException empty) {
            log.error("Bad developersDelay value: {}", newValue);

            if (!newValue.isEmpty())
                errorSceneStart(empty);
        }
    }

    void managersDelayOnChange(ObservableValue<?> observable, String oldValue, String newValue){
        try{
            configuration.setManagerDelay(Long.parseLong(newValue));
        }catch (NumberFormatException empty){
            log.error("Bad managersDelay value: {}", newValue);
            if (!newValue.isEmpty())
                errorSceneStart(empty);
        }
    }
    void managerLivingTimeOnChange(ObservableValue<?> observable, String oldValue, String newValue){
        try{
            configuration.setManagerLivingTime(Long.parseLong(newValue));
        }catch (NumberFormatException empty){
            log.error("Bad managerLivingTime value: {}", newValue);
            if(!newValue.isEmpty())
                errorSceneStart(empty);
        }
    }

    void developerLivingTimeOnChange(ObservableValue<?> observable, String oldValue, String newValue){
        try{
            configuration.setDeveloperLivingTime(Long.parseLong(newValue));
        } catch (NumberFormatException empty){
            log.error("Bad developerLivingTime value: {}", newValue);
            if(!newValue.isEmpty())
                errorSceneStart(empty);
        }
    }
    //Методы новых сцен
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
        formStage.setOnCloseRequest(event->{
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            formStage.close();
            stopHandler(rootEvent);
        });
        formStage.show();

    }
    @SneakyThrows
    void showObjectsInfoForm(ActionEvent rootEvent){

        stopButton.setDisable(true);
        objectsInfoButton.setDisable(true);

        final Stage formStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/objectsInfo.fxml"));
        formStage.setScene(new Scene(loader.load()));
        ObjectsInfoController controller = loader.getController();
        controller.setObservableList(habitat.getDevelopers(),habitat.getManagers());

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
        formStage.setOnCloseRequest(event->{
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            formStage.close();
            stopHandler(rootEvent);
        });
        formStage.show();
    }
//    Методы обновления
    @SneakyThrows
    void sleep() {
        Thread.sleep(habitat.getConfiguration().getProcessDelay());
    }
    void refreshStatistic() {

        log.info("RefreshStatistics");

        developersCountLabel.setText(habitat.getDeveloperCount().toString());
        managersCountLabel.setText(habitat.getManagerCount().toString());

        managersDelayTextField.setText(configuration.getManagerDelay().toString());
        developersDelayTextField.setText(configuration.getDeveloperDelay().toString());

        managerLivingTime.setText(configuration.getManagerLivingTime().toString());
        developerLivingTime.setText(configuration.getDeveloperLivingTime().toString());

        developersProbabilityMenu.setText(configuration.getDeveloperProbability().toString());
        managersRatioMenu.setText(configuration.getManagerRatio().toString());

        simulationTime.setText("Simulation time: " + getSimulationTime());
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