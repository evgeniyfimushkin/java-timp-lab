package edu.evgen.habitat;

import edu.evgen.habitat.employee.*;
import lombok.Data;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

import static edu.evgen.habitat.employee.EmployeesRepository.*;

@Data
@Slf4j
public class HabitatImpl implements Habitat {

    public static Habitat habitat = new HabitatImpl();
    final Random random = new Random();
    HabitatConfiguration configuration;

    private HabitatImpl() {
    }

    @Override
    public Optional<? extends IBehaviour> managerBirthAttempt(){
        if (
                (getManagers().isEmpty() || getManagers().getLast().getBirthTime().plusSeconds(configuration.getManagerDelay()).isBefore(LocalDateTime.now())) &&
                        ((double) getManagers().size() / Math.max(getDevelopers().size(), 1) < configuration.getManagerRatio())
        ) {
            log.info("Manager birth!");
            return Optional.of(new Manager(configuration.getPaneSize(), configuration.getManagerLivingTime()));

        }
        return Optional.empty();
    }

    @Override
    public Optional<? extends Employee> developerBirthAttempt() {
        log.info("Developer birth attempt <-");
        if (
                (getDevelopers().isEmpty() || getDevelopers().getLast().getBirthTime().plusSeconds(configuration.getDeveloperDelay()).isBefore(LocalDateTime.now())) &&
                        (random.nextDouble() <= configuration.getDeveloperProbability())
        ) {
            log.info("Developer birth!");
            return Optional.of(
                    new Developer(configuration.getPaneSize(), configuration.getDeveloperLivingTime()));
        }

        return Optional.empty();
    }
}