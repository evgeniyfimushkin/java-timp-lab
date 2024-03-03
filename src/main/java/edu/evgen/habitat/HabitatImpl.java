package edu.evgen.habitat;

import edu.evgen.habitat.employee.Developer;
import edu.evgen.habitat.employee.IBehaviour;
import edu.evgen.habitat.employee.Manager;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class HabitatImpl implements Habitat {
    final Long developerDelay;
    final Long managerDelay;
    final Double developerProbability;
    final Double managerRatio;
    final Set<Developer> developers = new HashSet<>();
    final Set<Manager> managers = new HashSet<>();
    @Override
    public Optional<IBehaviour> birthAttempt() {
        return Optional.empty();
    }

    @Override
    public Integer getDeveloperCount() {
        return developers.size();
    }

    @Override
    public Integer getManagerCount() {
        return managers.size();
    }
}