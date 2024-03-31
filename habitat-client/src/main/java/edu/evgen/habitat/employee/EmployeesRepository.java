package edu.evgen.habitat.employee;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

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


    static Optional<Connection> getConnection() {
        try {
            return Optional.of(DriverManager.getConnection(DataBase, USERNAME, PASSWORD));
        } catch (SQLException e) {
            log.warn("Cannot connect DataBase: {} {} {}", DataBase, USERNAME, PASSWORD);
            return Optional.empty();
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

    //    public static void indexManagers() {
//        try {
//            Statement statement = connection.createStatement();
//            String SQL = "SELECT * FROM managers";
//            ResultSet resultSet = statement.executeQuery(SQL);
//            while (resultSet.next()) {
//                Manager manager = new Manager(resultSet.getLong("paneSize"),
//                        resultSet.getLong("livingTime"));
//                manager.setId(resultSet.getLong("id"));
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @SneakyThrows
    public static void loadEmployeesDB() {
        getConnection()
                .ifPresentOrElse(
                        connection -> {
                            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees")) {
                                statement.execute();
                                ResultSet resultSet = statement.getResultSet();
                                while (resultSet.next()) {
                                    Employee employee = resultSet.getString("type").equals("manager") ?
                                            new Manager(resultSet.getLong("x"),
                                                    resultSet.getLong("y"),
                                                    resultSet.getLong("id"),
                                                    resultSet.getLong("livingTime"),
                                                    resultSet.getLong("paneSize")) :
                                            new Developer(resultSet.getLong("x"),
                                                    resultSet.getLong("y"),
                                                    resultSet.getLong("id"),
                                                    resultSet.getLong("livingTime"),
                                                    resultSet.getLong("paneSize"));
                                }
                            } catch (SQLException e) {
                                log.error("LOAD ERROR {}", e);
                            }
                        },
                        () -> log.info("No DB, NO SAVE"));
    }

    /**
     * CREATE TABLE public.employees (
     * id numeric primary key,
     * type varchar(32) not null,
     * livingtime integer not null,
     * panesize integer not null,
     * x integer not null,
     * y integer not null,
     */
    @SneakyThrows
    public static void saveDB(){
        log.info("SAVE DB <-");
        getConnection()
                .ifPresentOrElse(
                        connection -> {
                            try (PreparedStatement statement = connection.prepareStatement("insert into employees values(?,?,?,?,?,?)")) {
                                employees.forEach(employee -> {
                                    saveEmployee((Employee) employee, statement);
                                });
                            } catch (SQLException e) {
                                log.error("SAVE ERROR: {}", e);
                            }
                        },
                        () -> log.info("No DB, NO SAVE")
                );
    }

    @SneakyThrows
    private static void saveEmployee(Employee employee, PreparedStatement statement) {
        log.info("SAVE EMPLOYEE <- {}", employee);
        statement.setLong(1, employee.getId());
        statement.setString(2, employee.getClass().getSimpleName());
        statement.setLong(3, employee.getLivingTime());
        statement.setLong(4, employee.getPaneSize());
        statement.setDouble(5, employee.getX());
        statement.setDouble(6, employee.getY());
        statement.execute();
    }

    private static Long getId() {
        Long currentId = random.nextLong();
        if (!ids.contains(currentId)) {
            ids.add(currentId);
            return currentId;
        } else return getId();
    }
}
