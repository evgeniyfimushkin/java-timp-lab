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
    Long developerDelay;
    Long managerDelay;
    Double developerProbability;
    Double managerRatio;
    final List<IBehaviour> developers = new ArrayList<>();
    final List<IBehaviour> managers = new ArrayList<>();
    final Random random = new Random();
    Long paneSize;

    public HabitatImpl(Long developerDelay, Long managerDelay, Double developerProbability, Double managerRatio, Long paneSize) {
        this.developerDelay = developerDelay;
        this.managerDelay = managerDelay;
        this.developerProbability = developerProbability;
        this.managerRatio = managerRatio;
        this.paneSize = paneSize;
    }

    @Override
    public Optional<IBehaviour> birthAttempt() {
        log.info("birthAttempt <-");
        LocalDateTime now = LocalDateTime.now();

        if (
                (developers.isEmpty() || developers.getLast().getBirthTime().plusSeconds(developerDelay).isBefore(now)) &&
                        (random.nextDouble() <= developerProbability)
        ) {
            log.info("Developer birth!");
            IBehaviour employee = new Developer(developerProbability, paneSize);
            developers.add(employee);
            return Optional.of(employee);

        }

        if (
                (managers.isEmpty() || managers.getLast().getBirthTime().plusSeconds(managerDelay).isBefore(now)) &&
                        ((double)managers.size()/Math.max(developers.size(),1) < managerRatio)
        ) {
            log.info("Manager birth!");
            IBehaviour employee = new Manager(paneSize);
            managers.add(employee);
            return Optional.of(employee);

        }
            return Optional.empty();
    }

    public void setDeveloperDelay(Long developerDelay) {
        this.developerDelay = developerDelay;
    }

    public void setManagerDelay(Long managerDelay) {
        this.managerDelay = managerDelay;
    }

    public void setDeveloperProbability(Double developerProbability) {
        this.developerProbability = developerProbability;
    }

    public void setManagerRatio(Double managerRatio) {
        this.managerRatio = managerRatio;
    }

    public void setPaneSize(Long paneSize) {
        this.paneSize = paneSize;
    }

    @Override
    public Integer getDeveloperCount() {
        return developers.size();
    }

    @Override
    public Integer getManagerCount() {
        return managers.size();
    }
    @Override
    public void clear(){
        developers.clear();
        managers.clear();
    }
}