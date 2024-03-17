package edu.evgen.habitat;
import edu.evgen.habitat.employee.EmployeesRepository;
import edu.evgen.habitat.employee.IBehaviour;
import javafx.application.Platform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static edu.evgen.habitat.HabitatImpl.habitat;

@Slf4j
@RequiredArgsConstructor
public class BaseMoving implements Runnable {
    private final Long moveDelay;
    public Boolean run = false;

    @Override
    public void run() {
        log.info("start living");
        run = true;
        try {
            do {
                Thread.sleep(moveDelay);
                EmployeesRepository.employees.forEach(IBehaviour::move);
            } while (run);
        } catch (Throwable ignore) {
        }
        log.info("stop living");
    }

}