package edu.evgen.habitat;

import edu.evgen.habitat.employee.Developer;
import edu.evgen.habitat.employee.Employee;
import edu.evgen.habitat.employee.IBehaviour;
import edu.evgen.habitat.employee.Manager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Data
@Slf4j
public class HabitatImpl implements Habitat {

    public static Habitat habitat = new HabitatImpl();

    final List<Employee> developers = new LinkedList<>();
    final List<Employee> managers = new LinkedList<>();


    final Random random = new Random();

    HabitatConfiguration configuration;
    private HabitatImpl(){}
    @Override
    public Collection<Employee> mustDie(){
        return Stream.concat(developers.stream(),managers.stream())
                .filter(IBehaviour::mustDie)
                .toList();
    }
    @Override
    public Optional<? extends Employee> birthAttempt() {
        log.info("birthAttempt <-");

        LocalDateTime now = LocalDateTime.now();

        if (
                (developers.isEmpty() || developers.getLast().getBirthTime().plusSeconds(configuration.getDeveloperDelay()).isBefore(now)) &&
                        (random.nextDouble() <= configuration.getDeveloperProbability())
        ) {
            log.info("Developer birth!");
            Employee employee = new Developer(configuration.getPaneSize(), configuration.getDeveloperLivingTime());
            developers.add(employee);
            return Optional.of(employee);
        }

        if (
                (managers.isEmpty() || managers.getLast().getBirthTime().plusSeconds(configuration.getManagerDelay()).isBefore(now)) &&
                        ((double)managers.size()/Math.max(developers.size(),1) < configuration.getManagerRatio())
        ) {
            log.info("Manager birth!");
            Employee employee = new Manager(configuration.getPaneSize(),configuration.getManagerLivingTime());
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
    @Override
    public Collection<Employee> getDevelopers(){return developers;}
    @Override
    public Collection<Employee> getManagers(){return managers;}
    @Override
    public void clear(){
        developers.clear();
        managers.clear();
    }
}