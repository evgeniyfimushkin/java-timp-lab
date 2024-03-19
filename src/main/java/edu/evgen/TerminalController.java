package edu.evgen;

import edu.evgen.habitat.employee.EmployeesRepository;
import edu.evgen.habitat.employee.IBehaviour;
import edu.evgen.habitat.employee.Manager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Setter
public class TerminalController {
    private final static List<String> commands = new LinkedList<>();
    Stage stage;
    SceneController sceneController;
    @FXML
    TextField commandField;
    @FXML
    TextArea textArea;

    public TerminalController(Stage stage, SceneController sceneController){
        this.stage = stage;
        this.sceneController = sceneController;
    }
    private void output(String message) {
        textArea.appendText(message + "\n");
    }

    private boolean checkAmountOfArguments(String[] tokens, int min, int max) {
        if (tokens.length < min) {
            output("Not enough arguments for " + tokens[0]);
            return false;
        }
        if (tokens.length > max) {
            output("Too many arguments for " + tokens[0]);
            return false;
        }
        return true;
    }

    private void commandHelp() {
        output(
                """
                        Availible commands:
                        hire <employee type> <count> - hire some employee
                        fire <employee type> <count> - fire some employee
                        help - display this message
                        exit - exit the terminal session
                        """
        );
    }

    private void commandHire() {

        Manager Carl = new Manager();
        output("Manager was born!\n Id: "+ Carl.getId() + "\nBirthTime: " + Carl.getBirthTime());
        sceneController.refreshStatistic();

    }

    private void commandFire() {
        EmployeesRepository.getDevelopers().stream().forEach(EmployeesRepository::removeEmployee);
        sceneController.refreshStatistic();
    }

    private void commandExit() {
        stage.close();
    }

    private void processCommand(String command) {
        String[] tokens = command.split(" ");
        switch (tokens[0]) {
            case "help" -> {
                commandHelp();
            }
            case "hire" -> {
                if (checkAmountOfArguments(tokens, 2, 2))
                    commandHire();
            }
            case "fire" -> {
                if (checkAmountOfArguments(tokens, 3, 3))
                    commandFire();
            }
            case "exit" -> {
                commandExit();
            }
            default -> {
                output("No such a command: " + command);
            }
        }
    }

    private void setKeyActions() {
        commandField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String command = commandField.getText().trim();
                textArea.appendText("> " + command + "\n");
                commandField.clear();
                commands.add(command);
                processCommand(command);
            }
            if (event.getCode() == KeyCode.UP) {
                commandField.setText(commands.getLast());
            }
        });
    }

    @FXML
    public void initialize() {
        setKeyActions();
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setFocusTraversable(false);
        commandField.requestFocus();
    }
}
