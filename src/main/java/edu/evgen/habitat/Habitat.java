package edu.evgen.habitat;

import edu.evgen.habitat.employee.Employee;
import edu.evgen.habitat.employee.IBehaviour;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.TreeSet;

public interface Habitat {
    HabitatConfiguration getConfiguration();
    Collection<Employee> mustDie();
    void setConfiguration(HabitatConfiguration configuration);
    Optional<? extends IBehaviour> birthAttempt();
    Integer getDeveloperCount();
    Integer getManagerCount();
    Collection<Employee> getDevelopers();
    Collection<Employee> getManagers();
    TreeSet<Long> getAllID();
    HashMap<Long, LocalDateTime> getALLBirthTimes();

    void clear();
}
