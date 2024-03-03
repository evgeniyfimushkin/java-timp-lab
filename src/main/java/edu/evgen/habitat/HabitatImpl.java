package edu.evgen.habitat;

import edu.evgen.habitat.employee.Developer;
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
    final List<Developer> developers = new ArrayList<>();
    final List<Manager> managers = new ArrayList<>();
    final Random random = new Random();
    final Long paneSize = 500L;
    @Override
    public Optional<IBehaviour> birthAttempt() {
        log.info("birthAttempt <-");
        LocalDateTime now = LocalDateTime.now();

        if (
                (developers.isEmpty() || developers.getLast().getBirthTime().plusSeconds(developerDelay).isBefore(now)) &&
                        (random.nextDouble() <= developerProbability)
        ) {
            log.info("Developer birth!");
            developers.add(new Developer(developerDelay, developerProbability, paneSize));
            return Optional.of(developers.getLast());

        }

        if (
                (managers.isEmpty() || managers.getLast().getBirthTime().plusSeconds(managerDelay).isBefore(now)) &&
                        ((double)managers.size()/Math.max(developers.size(),1) < managerRatio)
        ) {
            log.info("Manager birth!");
            managers.add(new Manager(managerDelay, paneSize));
            return Optional.of(developers.getLast());

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