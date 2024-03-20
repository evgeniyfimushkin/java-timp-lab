package edu.evgen;
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
                        hire <count> - create Managers
                        fire - delete all the Managers
                        help - display this message
                        exit - exit the terminal session
                        """
        );
    }

    private void commandHire(String countString) {
        Integer count;
        try {
            count = Integer.parseInt(countString);
            if (count <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e){
            output("Bad value: "+countString);
            return;
        }
        for (Integer i = 0; i < count; i++){
            Manager Carl = new Manager(400L,20L);
            output("Manager born!\n Id: "+ Carl.getId() + "\nBirthTime: " + Carl.getBirthTime());
        }
        output("-------------------------------");
        output(countString + " managers was born!");
        sceneController.refreshStatistic();

    }

    private void commandFire() {
        output(EmployeesRepository.getManagers().size()+" managers was deleted.");
        EmployeesRepository.getManagers().stream().forEach(EmployeesRepository::removeEmployee);
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
                    commandHire(tokens[1]);
            }
            case "fire" -> {
                    commandFire();
            }
            case "exit" -> {
                commandExit();
            }
            case "" -> {

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
                cursorCommand = commands.size();
                processCommand(command);
                textArea.setScrollTop(100);
            }
            if (event.getCode() == KeyCode.UP) {
                if (!commands.isEmpty() && cursorCommand>0 && cursorCommand <= commands.size()) {
                    cursorCommand --;
                    commandField.setText(commands.get(cursorCommand));
                    for (Integer i = 0; i < commands.getLast().length(); i++)
                        commandField.forward();
                }
            }
            if (event.getCode() == KeyCode.DOWN) {
                if (!commands.isEmpty() && cursorCommand>=0 && cursorCommand < commands.size() - 1) {
                    cursorCommand ++;
                    commandField.setText(commands.get(cursorCommand));
                    for (Integer i = 0; i < commands.getLast().length(); i++)
                        commandField.forward();
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
