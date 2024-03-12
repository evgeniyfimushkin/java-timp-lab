package edu.evgen.habitat;

import edu.evgen.habitat.employee.Employee;
import edu.evgen.habitat.employee.IBehaviour;

import java.util.Collection;
import java.util.Optional;

public interface Habitat {
    HabitatConfiguration getConfiguration();
    Collection<Employee> mustDie();
    void setConfiguration(HabitatConfiguration configuration);
    Optional<? extends IBehaviour> birthAttempt();
    Integer getDeveloperCount();
    Integer getManagerCount();
    Collection<Employee> getDevelopers();
    Collection<Employee> getManagers();

    void clear();
}
