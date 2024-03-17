package edu.evgen.habitat;
import edu.evgen.habitat.employee.*;
import lombok.Data;
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
    private HabitatImpl(){}
    @Override
    public Collection<IBehaviour> mustDie(){
        return EmployeesRepository.employees.stream()
                .filter(IBehaviour::mustDie)
                .toList();
    }
    @Override
    public Optional<? extends Employee> birthAttempt() {
                log.info("birthAttempt <-");

                LocalDateTime now = LocalDateTime.now();

                if (
                        (getDevelopers().isEmpty() || getDevelopers().getLast().getBirthTime().plusSeconds(configuration.getDeveloperDelay()).isBefore(now)) &&
                                (random.nextDouble() <= configuration.getDeveloperProbability())
                ) {
                    log.info("Developer birth!");
                    Employee employee = new Developer(configuration.getPaneSize(), configuration.getDeveloperLivingTime());
                    getDevelopers().add(employee);
                    return Optional.of(employee);
                }

                if (
                        (getManagers().isEmpty() || getManagers().getLast().getBirthTime().plusSeconds(configuration.getManagerDelay()).isBefore(now)) &&
                                ((double) getManagers().size() / Math.max(getDevelopers().size(), 1) < configuration.getManagerRatio())
                ) {
                    log.info("Manager birth!");
                    Employee employee = new Manager(configuration.getPaneSize(), configuration.getManagerLivingTime());
                    getManagers().add(employee);
                    return Optional.of(employee);

                }
                return Optional.empty();
            }
}