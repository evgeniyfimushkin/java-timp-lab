package edu.evgen.habitat;

import edu.evgen.habitat.employee.IBehaviour;

import java.util.Optional;

public interface Habitat {
    Optional<IBehaviour> birthAttempt();

    Long getDeveloperDelay();
    Long getManagerDelay();
    Double getDeveloperProbability();
    Double getManagerRatio();
    Integer getDeveloperCount();
    Integer getManagerCount();
    void clear();
}
