package edu.evgen;

import edu.evgen.habitat.Simulation;
import edu.evgen.habitat.HabitatConfiguration;
import edu.evgen.habitat.employee.*;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.WindowEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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
            .moveDelay(1L)
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
    Label simulationTime,
            developersCountLabel, managersCountLabel,
            managersDelayLabel, developersDelayLabel,
            developersProbabilityLabel, managersRatioLabel;

    @FXML
    Pane habitatPane;

    final Simulation moveSimulation = new Simulation(EmployeesRepository::moveAll, configuration.getMoveDelay(), "moving");
    final Simulation birthSimulation = new Simulation(this::birthAttempt, configuration.getMoveDelay(), "birthing");
    final Simulation killSimulation = new Simulation(this::kill, configuration.getMoveDelay(), "dying");
    Long startSimulationTime = System.currentTimeMillis(),
            startPauseTime = 0L,
            pausedTime = 0L;

    //Основные методы работы симуляции
    private Stream<Simulation> getSimulations() {
        return Stream.of(moveSimulation, birthSimulation, killSimulation);
    }

    public void startSimulation(ActionEvent event) {
        pausedTime = 0L;
        startSimulationTime = System.currentTimeMillis();
        doSimulation(event);
    }

    public void doSimulation(ActionEvent event) {
        fieldsSetDisable(true);
        getSimulations()
                .map(Simulation::getThread)
                .forEach(Thread::start);
    }

    public void continueSimulation(WindowEvent event) {
        getSimulations().forEach(Simulation::continueSimulation);
    }

    @SneakyThrows
    public void pauseSimulation() {
        getSimulations().forEach(Simulation::pauseSimulation);
    }


    void kill() {
        habitat.mustDie().forEach(EmployeesRepository::removeEmployee);
    }

    void birthAttempt() {
        habitat.birthAttempt()
                .ifPresent(employee -> Platform.runLater(this::refreshStatistic));
    }

    void stopHandler(ActionEvent event) {
        fieldsSetDisable(false);
        log.info("stopRun");

        log.info("clear");
        EmployeesRepository.clear();
        sleep();
        sleep();
        sleep();
        sleep();
        log.info("refresh");
        Platform.runLater(this::refreshStatistic);
        log.info("stopRun ->");
    }

    void fieldsSetDisable(boolean value) {
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

        objectsInfoButton.setDisable(true);
        stopButton.setDisable(true);
        log.info("controller init");
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

        EmployeesRepository.habitatPane = this.habitatPane;

        refreshConfiguration();
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
        refreshConfiguration();
    }

    private void setupDevelopersProbability(ActionEvent event) {
        MenuItem sourceMenuItem = (MenuItem) event.getSource();
        configuration.setDeveloperProbability(Double.parseDouble(sourceMenuItem.getText()));
        developersProbabilityMenu.setText(sourceMenuItem.getText());
        refreshConfiguration();
    }

    Stream<MenuItem> menuItemStream() {
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


    void developersDelayOnChange(ObservableValue<?> observable, String oldValue, String newValue) {
        try {
            configuration.setDeveloperDelay(Long.parseLong(newValue));
        } catch (NumberFormatException empty) {
            log.error("Bad developersDelay value: {}", newValue);

            if (!newValue.isEmpty())
                errorSceneStart(empty);
        }
    }

    void managersDelayOnChange(ObservableValue<?> observable, String oldValue, String newValue) {
        try {
            configuration.setManagerDelay(Long.parseLong(newValue));
        } catch (NumberFormatException empty) {
            log.error("Bad managersDelay value: {}", newValue);
            if (!newValue.isEmpty())
                errorSceneStart(empty);
        }
    }

    void managerLivingTimeOnChange(ObservableValue<?> observable, String oldValue, String newValue) {
        try {
            configuration.setManagerLivingTime(Long.parseLong(newValue));
        } catch (NumberFormatException empty) {
            log.error("Bad managerLivingTime value: {}", newValue);
            if (!newValue.isEmpty())
                errorSceneStart(empty);
        }
    }

    void developerLivingTimeOnChange(ObservableValue<?> observable, String oldValue, String newValue) {
        try {
            configuration.setDeveloperLivingTime(Long.parseLong(newValue));
        } catch (NumberFormatException empty) {
            log.error("Bad developerLivingTime value: {}", newValue);
            if (!newValue.isEmpty())
                errorSceneStart(empty);
        }
    }

    //Методы новых сцен
    @SneakyThrows
    void helpMeItemAction(ActionEvent event) {
        pauseSimulation();
        final Stage helpStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/helpme.fxml"));
        helpStage.setScene(new Scene(loader.load()));
        helpStage.setOnCloseRequest(this::continueSimulation);
        helpStage.show();
    }

    @SneakyThrows
    void errorSceneStart(Throwable exception) {
        pauseSimulation();
        final Stage errorStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/error.fxml"));
        errorStage.setScene(new Scene(loader.load()));
        ErrorController controller = loader.getController();
        controller.initialize(exception);
        controller.closeErrorWindowButton.setOnAction(event -> errorStage.close());
        errorStage.setOnCloseRequest(this::continueSimulation);

        errorStage.show();
    }

    @SneakyThrows
    void showSimulationInfoForm(ActionEvent rootEvent) {

        pauseSimulation();
        final Stage formStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/stopSimulationInfo.fxml"));
        formStage.setScene(new Scene(loader.load()));
        StopSimulationInfoController controller = loader.getController();
        controller.setAllTheLabels(habitat);
        controller.simulationTime.setText("Simulation time: " + getSimulationTime());
        startPauseTime = System.currentTimeMillis();
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
        formStage.setOnCloseRequest(event -> {
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            formStage.close();
            stopHandler(rootEvent);
        });
        formStage.setOnCloseRequest(this::continueSimulation);
        formStage.show();

    }

    @SneakyThrows
    void showObjectsInfoForm(ActionEvent rootEvent) {

        pauseSimulation();

        stopButton.setDisable(true);
        objectsInfoButton.setDisable(true);

        final Stage formStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/objectsInfo.fxml"));
        formStage.setScene(new Scene(loader.load()));
        ObjectsInfoController controller = loader.getController();

        startPauseTime = System.currentTimeMillis();
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
        formStage.setOnCloseRequest(event -> {
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            formStage.close();
            stopHandler(rootEvent);
        });
        formStage.setOnCloseRequest(this::continueSimulation);
        formStage.show();
    }

    //    Методы обновления
    @SneakyThrows
    void sleep() {
        Thread.sleep(habitat.getConfiguration().getProcessDelay());
    }

    void refreshStatistic() {

        log.info("RefreshStatistics");

        developersCountLabel.setText(String.valueOf(EmployeesRepository.getDevelopers().size()));
        managersCountLabel.setText(String.valueOf(EmployeesRepository.getManagers().size()));

        simulationTime.setText("Simulation time: " + getSimulationTime());
    }

    void refreshConfiguration() {
        managersDelayTextField.setText(configuration.getManagerDelay().toString());
        developersDelayTextField.setText(configuration.getDeveloperDelay().toString());

        managerLivingTime.setText(configuration.getManagerLivingTime().toString());
        developerLivingTime.setText(configuration.getDeveloperLivingTime().toString());

        developersProbabilityMenu.setText(configuration.getDeveloperProbability().toString());
        managersRatioMenu.setText(configuration.getManagerRatio().toString());

    }

    Long getSimulationTime() {
        return (System.currentTimeMillis() - pausedTime - startSimulationTime) / 1000;
    }

    void setSimulationTimeVisible() {
        if (simulationTime.isVisible()) {
            radioButtonHideTime.fire();
        } else {
            radioButtonShowTime.fire();
        }
    }
}