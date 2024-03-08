package edu.evgen.habitat;

import edu.evgen.habitat.employee.IBehaviour;

import java.util.Optional;

public interface Habitat {
    HabitatConfiguration getConfiguration();
    Optional<? extends IBehaviour> birthAttempt();
    Integer getDeveloperCount();
    Integer getManagerCount();
    void clear();
}
