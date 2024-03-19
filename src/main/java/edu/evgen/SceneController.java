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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static edu.evgen.habitat.HabitatImpl.habitat;

@Slf4j
public class SceneController {

    final HabitatConfiguration configuration = HabitatConfiguration.builder()
            .processDelay(100L)
            .managerRatio(0.5)
            .managerDelay(2L)
            .developerDelay(2L)
            .developerProbability(0.7)
            .paneSize(400L)
            .managerLivingTime(5L)
            .developerLivingTime(4L)
            .moveDelay(1L)
            .build();
    @FXML
    public AnchorPane root;
    @FXML
    public Button
            startButton,
            stopButton,
            objectsInfoButton,
            devThreadStopButton,
            devThreadStartButton,
            mgrThreadStopButton,
            mgrThreadStartButton;
    @FXML
    public MenuButton
            developersProbabilityMenu,
            managersRatioMenu,
            devPriorityMenuButton,
            mgrPriorityMenuButton;
    @FXML
    public RadioButton radioButtonHideTime, radioButtonShowTime;
    @FXML
    public CheckBox simulationInfoCheckBox;

    @FXML
    public MenuItem
            menuStartItem,
            menuStopItem,
            helpMeItem,
            terminalMenuItem;
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

    Simulation moveDevelopers;
    Simulation moveManagers;
    Simulation developerBirthSimulation;
    Simulation managerBirthSimulation;
    Simulation killSimulation;
    Long startSimulationTime = System.currentTimeMillis(),
            startPauseTime = 0L,
            pausedTime = 0L;

    //Основные методы работы симуляции
    private Stream<Simulation> getSimulations() {
        return Stream.of(
                moveDevelopers,
                moveManagers,
                developerBirthSimulation,
                managerBirthSimulation,
                killSimulation);
    }

    public void startSimulation(ActionEvent event) {

        moveDevelopers = new Simulation(EmployeesRepository::moveDevelopers, configuration.getMoveDelay(), "moveDev");

        moveManagers = new Simulation(EmployeesRepository::moveManagers, configuration.getMoveDelay(), "moveMgr");

        developerBirthSimulation = new Simulation(() -> birthAttempt(habitat::developerBirthAttempt), configuration.getDeveloperDelay() * 1000, "birthDev");

        managerBirthSimulation = new Simulation(() -> birthAttempt(habitat::managerBirthAttempt), configuration.getManagerDelay() * 1000, "birthMgr");

        killSimulation = new Simulation(this::kill, configuration.getMoveDelay(), "dying");

        devPriorityMenuButton.setText(String.valueOf(developerBirthSimulation.getPriority()));

        mgrPriorityMenuButton.setText(String.valueOf(managerBirthSimulation.getPriority()));

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

    public void continueSimulation() {
        stopButton.setDisable(false);
        objectsInfoButton.setDisable(false);
        getSimulations().forEach(Simulation::continueSimulation);
    }

    @SneakyThrows
    public void pauseSimulation() {
        getSimulations().forEach(Simulation::pauseSimulation);
    }

    void kill() {
        habitat.mustDie().forEach(EmployeesRepository::removeEmployee);
    }

    void birthAttempt(Supplier<Optional<? extends IBehaviour>> employeeProducer) {
        employeeProducer.get()
                .ifPresent(employee -> Platform.runLater(this::refreshStatistic));
    }

    void stopHandler(ActionEvent event) {
        getSimulations().forEach(Simulation::stopSimulation);
        fieldsSetDisable(false);
        log.info("stopRun");
        log.info("clear");
        EmployeesRepository.clear();
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
    }

    @FXML
    private void initialize() {
        habitat.setConfiguration(configuration);
        developersDelayTextField.textProperty().addListener(this::developersDelayOnChange);
        managersDelayTextField.textProperty().addListener(this::managersDelayOnChange);
        developerLivingTime.textProperty().addListener((this::developerLivingTimeOnChange));
        managerLivingTime.textProperty().addListener(this::managerLivingTimeOnChange);

        stopButton.setDisable(true);
        log.info("controller init");
        simulationInfoCheckBox.setOnAction(this::toggleCheckBoxHandler);
        toggleCheckBoxHandler(null);
        startButton.setOnAction(this::startSimulation);
        menuStartItem.setOnAction(event -> startButton.fire());
        objectsInfoButton.setOnAction(this::showObjectsInfoForm);


        threadMenuItemStream().forEach(devPriorityMenuButton.getItems()::add);
        threadMenuItemStream().forEach(mgrPriorityMenuButton.getItems()::add);

        devPriorityMenuButton.getItems().forEach(menuItem -> menuItem.setOnAction(e -> {
            developerBirthSimulation.setPriority(Integer.parseInt(menuItem.getText()));
            devPriorityMenuButton.setText(String.valueOf(developerBirthSimulation.getPriority()));
            mgrPriorityMenuButton.setText(String.valueOf(managerBirthSimulation.getPriority()));
        }));
        mgrPriorityMenuButton.getItems().forEach(menuItem -> menuItem.setOnAction(e -> {
            managerBirthSimulation.setPriority(Integer.parseInt(menuItem.getText()));
            mgrPriorityMenuButton.setText(String.valueOf(managerBirthSimulation.getPriority()));
            devPriorityMenuButton.setText(String.valueOf(developerBirthSimulation.getPriority()));
        }));

        menuItemStream().forEach(developersProbabilityMenu.getItems()::add);
        menuItemStream().forEach(managersRatioMenu.getItems()::add);
        developersProbabilityMenu.getItems().forEach(menuItem -> menuItem.setOnAction(this::setupDevelopersProbability));
        managersRatioMenu.getItems().forEach(menuItem -> menuItem.setOnAction(this::setupManagersRatio));

        devThreadStartButton.setOnAction(event -> {
            moveDevelopers.continueSimulation();
        });
        devThreadStopButton.setOnAction(event -> {
            moveDevelopers.pauseSimulation();
        });
        mgrThreadStartButton.setOnAction(event -> {
            moveManagers.continueSimulation();
        });
        mgrThreadStopButton.setOnAction(event -> {
            moveManagers.pauseSimulation();
        });

        terminalMenuItem.setOnAction(this::terminalFormOpen);
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
                .boxed()
                .map(i -> i / 10.0)
                .map(Object::toString)
                .map(MenuItem::new);
        //стрим не финализирован
    }

    Stream<MenuItem> threadMenuItemStream() {
        return IntStream.rangeClosed(1, 3)
                .boxed()
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
    Pair<Stage, FXMLLoader> createNewModalityForm(String fxmlFile) {
        startPauseTime = System.currentTimeMillis();
        pauseSimulation();
        final Stage formStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        formStage.setScene(new Scene(loader.load()));
        formStage.setOnCloseRequest(e -> {
            continueSimulation();
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
        });
        formStage.initModality(Modality.WINDOW_MODAL);
        formStage.initOwner(root.getScene().getWindow());
        return new Pair<>(formStage, loader);
    }

    @SneakyThrows
    void terminalFormOpen(ActionEvent event) {
        final Stage formStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/terminal.fxml"));
        formStage.setScene(new Scene(loader.load()));
        TerminalController controller = loader.getController();

        formStage.show();
    }

    @SneakyThrows
    void helpMeItemAction(ActionEvent event) {
        createNewModalityForm("/helpme.fxml").getKey().show();
    }

    @SneakyThrows
    void errorSceneStart(Throwable exception) {
        Pair<Stage, FXMLLoader> scene = createNewModalityForm("/error.fxml");
        ErrorController controller = scene.getValue().getController();
        controller.initialize(exception);
        controller.closeErrorWindowButton.setOnAction(event -> scene.getKey().close());
        scene.getKey().show();
    }

    @SneakyThrows
    void showSimulationInfoForm(ActionEvent rootEvent) {
        objectsInfoButton.setDisable(true);
        Pair<Stage, FXMLLoader> scene = createNewModalityForm("/stopSimulationInfo.fxml");
        StopSimulationInfoController controller = scene.getValue().getController();
        controller.setAllTheLabels(habitat);
        controller.simulationTime.setText("Simulation time: " + getSimulationTime());
        startPauseTime = System.currentTimeMillis();
        controller.stopButtonFromInfo.setOnAction(event -> {
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            scene.getKey().close();
            stopHandler(rootEvent);
        });

        controller.continueButton.setOnAction(event -> {
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            scene.getKey().close();
            continueSimulation();
        });
        scene.getKey().setOnCloseRequest(event -> {
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            scene.getKey().close();
            continueSimulation();
        });
        scene.getKey().show();

    }

    @SneakyThrows
    void showObjectsInfoForm(ActionEvent rootEvent) {
        stopButton.setDisable(true);
        Pair<Stage, FXMLLoader> scene = createNewModalityForm("/objectsInfo.fxml");
        ObjectsInfoController controller = scene.getValue().getController();
        startPauseTime = System.currentTimeMillis();
        controller.stopButtonFromInfo.setOnAction(event -> {
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            scene.getKey().close();
            stopHandler(rootEvent);
        });

        controller.continueButton.setOnAction(event -> {
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            scene.getKey().close();
            continueSimulation();
        });
        scene.getKey().setOnCloseRequest(event -> {
            pausedTime = pausedTime + System.currentTimeMillis() - startPauseTime;
            continueSimulation();
        });
        scene.getKey().show();
    }

    //    Методы обновления
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