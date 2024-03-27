package edu.evgen.habitat.employee;

import edu.evgen.SceneController;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.Driver;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class EmployeesRepository {

    private static final String DataBase = "jdbc:postgresql://localhost:5433/employees";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "underpender";
    private static Connection connection;


    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(DataBase, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final static List<IBehaviour> employees = new LinkedList<>();
    public static Pane habitatPane;
    private final static TreeSet<Long> ids = new TreeSet();
    private final static HashMap<Long, LocalDateTime> birthDays = new HashMap();
    private static Random random = new Random();

    @Synchronized
    public static void addEmployee(IBehaviour employee) {
        log.info("add employee <- {} {}", employees.size(), habitatPane.getChildren().size());
        employee.setId(getId());
        ids.add(employee.getId());
        birthDays.put(employee.getId(), employee.getBirthTime());
        employees.add(employee);
        Platform.runLater(() -> habitatPane.getChildren().add(employee.getImageView()));
        log.info("add employee -> {} {}", employees.size(), habitatPane.getChildren().size());
    }

    @Synchronized
    public static void removeEmployee(IBehaviour employee) {
        log.info("remove employee <- {} {}", employees.size(), habitatPane.getChildren().size());
        ids.remove(employee.getId());
        birthDays.remove(employee.getId());
        employees.remove(employee);
        Platform.runLater(() -> habitatPane.getChildren().remove(employee.getImageView()));
        log.info("remove employee <- {} {}", employees.size(), habitatPane.getChildren().size());
    }

    @Synchronized
    public static List<IBehaviour> getDevelopers() {
        return employees.stream()
                .filter(Developer.class::isInstance)
                .collect(Collectors.toList());
    }

    @Synchronized
    public static List<IBehaviour> getManagers() {
        return employees.stream()
                .filter(Manager.class::isInstance)
                .collect(Collectors.toList());
    }

    @Synchronized
    public static void moveDevelopers() {
        getDevelopers().forEach(IBehaviour::move);
    }

    @Synchronized
    public static void moveManagers() {
        getManagers().forEach(IBehaviour::move);
    }

    @Synchronized
    public static void disappearEmployee() {
        employees.forEach(IBehaviour::disapear);
    }

    @Synchronized
    public static void clear() {
        employees.clear();
        ids.clear();
        birthDays.clear();
        Platform.runLater(habitatPane.getChildren()::clear);
    }

    @Synchronized
    public static void saveRepository() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialFileName("employees.bin");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Employees", "*.bin"));
        File file = fileChooser.showSaveDialog(habitatPane.getScene().getWindow());

        ObjectOutputStream outputStream;

        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            for (int i = 0; i < employees.size(); i++) {
                outputStream.writeObject(employees.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Synchronized
    public static Collection<IBehaviour> mustDie() {
        return employees.stream()
                .filter(IBehaviour::mustDie)
                .toList();
    }

    @Synchronized
    public static void loadRepository() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Employees", "*.bin"));
        File file = fileChooser.showOpenDialog(habitatPane.getScene().getWindow());

        ObjectInputStream inputStream;
        //habitat.clear();

        try {
            boolean keepReading = true;
            inputStream = new ObjectInputStream(new FileInputStream(file));
            while (keepReading) {
                try {
                    //Employee employee = (Employee) inputStream.readObject();
                    Employee employee = (Employee) inputStream.readObject();
                    if (employee.getClass().toString().equals("class edu.evgen.habitat.employee.Manager")) {
                        Manager temp = new Manager((Manager) employee);
                    } else {
                        Developer temp = new Developer((Developer) employee);
                    }
                    log.info(employee.getClass().toString());
                } catch (EOFException e) {
                    keepReading = false;
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void indexManagers() {
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM managers";
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Manager manager = new Manager(resultSet.getLong("paneSize"),
                        resultSet.getLong("livingTime"));
                manager.setId(resultSet.getLong("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void indexDevelopers() {
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM developers";
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Developer developer = new Developer(resultSet.getLong("paneSize"),
                        resultSet.getLong("livingTime"));
                developer.setId(resultSet.getLong("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveDB() throws SQLException {
        Statement statementDevs = connection.createStatement();
        getDevelopers().forEach(developer -> {
            String SQL = "INSERT INTO developers VALUES(" + developer.getId() + "," + developer.getLivingTime() + "," + 400 + ")";
            try {
                statementDevs.executeUpdate(SQL);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        Statement statementMgr = connection.createStatement();
        getManagers().forEach(manager -> {
            String SQL = "INSERT INTO managers VALUES(" + manager.getId() + ",'" + manager.getLivingTime() + ",'" + 400 + "')";
            try {
                statementMgr.executeUpdate(SQL);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Long getId() {
        Long currentId = random.nextLong();
        if (!ids.contains(currentId)) {
            ids.add(currentId);
            return currentId;
        } else return getId();
    }
}
