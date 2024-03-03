package edu.evgen.habitat;

import edu.evgen.habitat.employee.Developer;
import edu.evgen.habitat.employee.Employee;
import edu.evgen.habitat.employee.IBehaviour;
import edu.evgen.habitat.employee.Manager;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

@Data
@RequiredArgsConstructor
@Slf4j
public class HabitatImpl implements Habitat {
    final Long developerDelay;
    final Long managerDelay;
    final Double developerProbability;
    final Double managerRatio;
    final List<IBehaviour> developers = new ArrayList<>();
    final List<IBehaviour> managers = new ArrayList<>();
    final Random random = new Random();
    final Long paneSize;
    @Override
    public Optional<IBehaviour> birthAttempt() {
        log.info("birthAttempt <-");
        LocalDateTime now = LocalDateTime.now();

        if (
                (developers.isEmpty() || developers.getLast().getBirthTime().plusSeconds(developerDelay).isBefore(now)) &&
                        (random.nextDouble() <= developerProbability)
        ) {
            log.info("Developer birth!");
            IBehaviour employee = new Developer(developerDelay, developerProbability, paneSize);
            developers.add(employee);
            return Optional.of(employee);

        }

        if (
                (managers.isEmpty() || managers.getLast().getBirthTime().plusSeconds(managerDelay).isBefore(now)) &&
                        ((double)managers.size()/Math.max(developers.size(),1) < managerRatio)
        ) {
            log.info("Manager birth!");
            IBehaviour employee = new Manager(managerDelay, paneSize);
            managers.add(employee);
            return Optional.of(employee);

        }
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