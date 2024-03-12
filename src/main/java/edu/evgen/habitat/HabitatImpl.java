package edu.evgen.habitat;

import edu.evgen.habitat.employee.Developer;
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

    final List<IBehaviour> developers = new ArrayList<>();
    final List<IBehaviour> managers = new ArrayList<>();
    final Random random = new Random();

    HabitatConfiguration configuration;
    private HabitatImpl(){}
    @Override
    public Collection<IBehaviour> mustDie(){
        return Stream.concat(developers.stream(),managers.stream())
                .filter(IBehaviour::mustDie)
                .toList();
    }
    @Override
    public Optional<? extends IBehaviour> birthAttempt() {
        log.info("birthAttempt <-");

        LocalDateTime now = LocalDateTime.now();

        if (
                (developers.isEmpty() || developers.getLast().getBirthTime().plusSeconds(configuration.getDeveloperDelay()).isBefore(now)) &&
                        (random.nextDouble() <= configuration.getDeveloperProbability())
        ) {
            log.info("Developer birth!");
            IBehaviour employee = new Developer(configuration.getPaneSize(), configuration.getDeveloperLivingTime());
            developers.add(employee);
            return Optional.of(employee);
        }

        if (
                (managers.isEmpty() || managers.getLast().getBirthTime().plusSeconds(configuration.getManagerDelay()).isBefore(now)) &&
                        ((double)managers.size()/Math.max(developers.size(),1) < configuration.getManagerRatio())
        ) {
            log.info("Manager birth!");
            IBehaviour employee = new Manager(configuration.getPaneSize(),configuration.getManagerLivingTime());
            managers.add(employee);
            return Optional.of(employee);

        }
            return Optional.empty();
    }


//    @SneakyThrows
//    private <T extends IBehaviour> Optional<T> birthAttempt(boolean isBirth, Function<Long, T> employeeConstructor, Collection<IBehaviour> employeeCollection){
//        if (!isBirth)
//            return Optional.empty();
//
//        T employee = employeeConstructor.apply(configuration.getPaneSize());
//        log.info("{} birth!", employee.getClass().getSimpleName());
//        employeeCollection.add(employee);
//        return Optional.of(employee);
//    }
//
//    @SneakyThrows
//    private <T extends IBehaviour> Optional<T> birthAttempt(boolean isBirth, Class<T> employeeClass, Collection<IBehaviour> employeeCollection){
//        if (!isBirth)
//            return Optional.empty();
//
//        log.info("{} birth!", employeeClass);
//        T employee = employeeClass.getDeclaredConstructor(Long.class).newInstance(configuration.getPaneSize());
//        employeeCollection.add(employee);
//        return Optional.of(employee);
//    }
//    private Boolean isBirthDeveloper(){
//        return (managers.isEmpty() ||
//                managers.getLast().getBirthTime().plusSeconds(configuration.getManagerDelay()).isBefore(LocalDateTime.now())) &&
//                (double)managers.size()/Math.max(developers.size(), 1) < configuration.getManagerRatio();
//    }
//    private Boolean isBirthManager(){
//        return (managers.isEmpty() ||
//                managers.getLast().getBirthTime().plusSeconds(configuration.getManagerDelay()).isBefore(LocalDateTime.now())) &&
//                ((double)managers.size()/Math.max(developers.size(),1) < configuration.getManagerRatio());
//    }

    @Override
    public Integer getDeveloperCount() {
        return developers.size();
    }

    @Override
    public Integer getManagerCount() {
        return managers.size();
    }
    @Override
    public Collection<IBehaviour> getDevelopers(){return developers;}
    @Override
    public Collection<IBehaviour> getManagers(){return managers;}
    @Override
    public void clear(){
        developers.clear();
        managers.clear();
    }
}