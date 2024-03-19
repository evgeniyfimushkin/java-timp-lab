package edu.evgen.habitat.employee;

import edu.evgen.SceneController;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
public class EmployeesRepository {
    public final static List<IBehaviour> employees = new LinkedList<>();
    public static Pane habitatPane;
    private final static TreeSet<Long> ids = new TreeSet();
    private final static HashMap<Long, LocalDateTime> birthDays = new HashMap();
    private static Random random = new Random();
    @Synchronized
    public static void addEmployee(IBehaviour employee){
        log.info("add employee <- {} {}", employees.size(),habitatPane.getChildren().size());
        employee.setId(getId());
        ids.add(employee.getId());
        birthDays.put(employee.getId(), employee.getBirthTime());
        employees.add(employee);
        Platform.runLater(() -> habitatPane.getChildren().add(employee.getImageView()));
        log.info("add employee -> {} {}", employees.size(),habitatPane.getChildren().size());
    }
    @Synchronized
    public static void removeEmployee(IBehaviour employee){
        log.info("remove employee <- {} {}", employees.size(),habitatPane.getChildren().size());
        ids.remove(employee.getId());
        birthDays.remove(employee.getId());
        employees.remove(employee);
        Platform.runLater(() -> habitatPane.getChildren().remove(employee.getImageView()));
        log.info("remove employee <- {} {}", employees.size(),habitatPane.getChildren().size());
    }
    @Synchronized
    public static List<IBehaviour> getDevelopers(){
        return employees.stream()
                .filter(Developer.class::isInstance)
                .collect(Collectors.toList());
    }
    @Synchronized
    public static List<IBehaviour> getManagers(){
        return employees.stream()
                .filter(Manager.class::isInstance)
                .collect(Collectors.toList());
    }
//    @Synchronized
//    public static void moveAll(){
//        employees.forEach(IBehaviour::move);
//    }
    @Synchronized
    public static void moveDevelopers(){
        getDevelopers().forEach(IBehaviour::move);
    }
    @Synchronized
    public static void moveManagers(){
        getManagers().forEach(IBehaviour::move);
    }
    @Synchronized
    public static void disappearEmployee(){
        employees.forEach(IBehaviour::disapear);
    }
    @Synchronized
    public static void clear(){
        employees.clear();
        ids.clear();
        birthDays.clear();
        Platform.runLater(habitatPane.getChildren()::clear);
    }
    private static Long getId(){
        Long currentId = random.nextLong();
        if (!ids.contains(currentId)){
            ids.add(currentId);
            return currentId;
        }
        else return getId();
    }
}
