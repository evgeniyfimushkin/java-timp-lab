package edu.evgen.habitat.employee;

import lombok.Synchronized;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeesRepository {
    public final static List<IBehaviour> employees = new LinkedList<>();
    private final static TreeSet<Long> ids = new TreeSet();
    private final static HashMap<Long, LocalDateTime> birthDays = new HashMap();
    private static Random random = new Random();
    @Synchronized
    public static void add(IBehaviour employee){
        employee.setId(getId());
        ids.add(employee.getId());
        birthDays.put(employee.getId(), employee.getBirthTime());
        employees.add(employee);
    }
    @Synchronized
    public static void remove(IBehaviour employee){
        ids.remove(employee.getId());
        birthDays.remove(employee.getId());
        employees.remove(employee);
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
    public static void clear(){
        employees.clear();
        ids.clear();
        birthDays.clear();
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
