package edu.evgen;

import edu.evgen.habitat.employee.Developer;
import edu.evgen.habitat.employee.EmployeesRepository;
import edu.evgen.habitat.employee.Manager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Setter
public class TerminalController {
    private final static List<String> commands = new ArrayList<>();
    Stage stage;
    SceneController sceneController;
    @FXML
    TextField commandField;
    @FXML
    TextArea textArea;
    private Integer cursorCommand;

    public TerminalController(Stage stage, SceneController sceneController) {
        this.stage = stage;
        this.sceneController = sceneController;
    }

    private void output(String message) {
        textArea.appendText(String.format("%s\n", message));
    }

    private boolean checkAmountOfArguments(String[] tokens, int min, int max) {
        if (tokens.length < min) {
            output(String.format("Not enough arguments for %s", tokens[0]));
            return false;
        }
        if (tokens.length > max) {
            output(String.format("Too many arguments for %s", tokens[0]));
            return false;
        }
        return true;
    }

    private void commandHelp() {
        output(
                """
                        Availible commands:
                        hire <count> - create Managers
                        fire - delete all the Managers
                        help - display this message
                        exit - exit the terminal session
                        clients - show all client in server
                        exchange <client-id> - swap managers and developers with some client
                        """
        );
    }

    private void commandHire(String countString) {
        Integer count;
        try {
            count = Integer.parseInt(countString);
            if (count <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            output(String.format("Bad value: %s", countString));
            return;
        }
        IntStream.rangeClosed(0, count - 1).forEach(e -> {
            Manager Carl = new Manager(400L, 30L);
            output(String.format("Manager born!\n Id: %s \nBirthTime: %s", Carl.getId(), Carl.getBirthTime()));
        });
        output("-------------------------------");
        output(String.format("%s managers was born!", countString));
        sceneController.refreshStatistic();

    }

    private void commandHireDevs(String countString) {
        Integer count;
        try {
            count = Integer.parseInt(countString);
            if (count <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            output(String.format("Bad value: %s", countString));
            return;
        }
        IntStream.rangeClosed(0, count - 1).forEach(e -> {
            Developer Carl = new Developer(400L, 30L);
            output(String.format("Developer born!\n Id: %s \nBirthTime: %s", Carl.getId(), Carl.getBirthTime()));
        });
        output("-------------------------------");
        output(String.format("%s managers was born!", countString));
        sceneController.refreshStatistic();

    }

    private void commandFire() {
        output(String.format("%s  managers was deleted.", EmployeesRepository.getManagers().size()));
        EmployeesRepository.getManagers().forEach(EmployeesRepository::removeEmployee);
        sceneController.refreshStatistic();
    }

    private void commandExchange(String id) {
        if (sceneController.client.get() != null)
            sceneController.client.get().sendManagers(id);
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
                    commandHire(tokens[1]);
            }
            case "hireDevs" -> {
                if (checkAmountOfArguments(tokens, 2, 2))
                    commandHireDevs(tokens[1]);
            }
            case "fire" -> {
                commandFire();
            }
            case "exit" -> {
                commandExit();
            }
            case "exchange" -> {
                if (checkAmountOfArguments(tokens, 2, 2))
                    commandExchange(tokens[1]);
            }
            case "" -> {

            }
            default -> {
                output(String.format("No such a command: %s", command));
            }
        }
    }

    private void setKeyActions() {
        commandField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String command = commandField.getText().trim();
                textArea.appendText(String.format("> %s\n", command));
                commandField.clear();
                commands.add(command);
                cursorCommand = commands.size();
                processCommand(command);
                textArea.setScrollTop(100);
            }
            if (event.getCode() == KeyCode.UP) {
                if (!commands.isEmpty() && cursorCommand > 0 && cursorCommand <= commands.size()) {
                    cursorCommand--;
                    commandField.setText(commands.get(cursorCommand));
                    IntStream.rangeClosed(0, commands.getLast().length()).forEach(e -> commandField.forward());
                }
            }
            if (event.getCode() == KeyCode.DOWN) {
                if (!commands.isEmpty() && cursorCommand >= 0 && cursorCommand < commands.size() - 1) {
                    cursorCommand++;
                    commandField.setText(commands.get(cursorCommand));
                    IntStream.rangeClosed(0, commands.getLast().length()).forEach(e -> commandField.forward());
                }
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
        textArea.setText("""
                \n\n\n\n\n\n\n\n\n\n\n\n
                Welcome to the Terminal!
                help - to see commands
                """);
        textArea.setScrollTop(100);
    }
}
