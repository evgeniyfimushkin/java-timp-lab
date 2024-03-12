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

    final TreeSet<Long> allID = new TreeSet();
    final HashMap<Long,LocalDateTime> allBirthTimes = new HashMap();

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
            allID.add(employee.getId());
            allBirthTimes.put(employee.getId(),employee.getBirthTime());
            return Optional.of(employee);
        }

        if (
                (managers.isEmpty() || managers.getLast().getBirthTime().plusSeconds(configuration.getManagerDelay()).isBefore(now)) &&
                        ((double)managers.size()/Math.max(developers.size(),1) < configuration.getManagerRatio())
        ) {
            log.info("Manager birth!");
            Employee employee = new Manager(configuration.getPaneSize(),configuration.getManagerLivingTime());
            managers.add(employee);
            allID.add(employee.getId());
            allBirthTimes.put(employee.getId(),employee.getBirthTime());
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
    public TreeSet<Long> getAllID(){ return allID; }
    @Override
    public HashMap<Long, LocalDateTime> getALLBirthTimes(){ return allBirthTimes; }
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