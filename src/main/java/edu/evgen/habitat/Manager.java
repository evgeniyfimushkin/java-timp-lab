package edu.evgen.habitat;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class Manager extends Employee {
    public Manager(Long birthDelay) {
        super(birthDelay, "/manager.png");
    }

    @Override
    public Optional<IBehaviour> birthAttempt() {
        log.info("birthAttempt <-");
        LocalDateTime now = LocalDateTime.now();

        if (lastBirthAttempt.plusSeconds(birthDelay).isAfter(now))
            return Optional.empty();

        lastBirthAttempt = now;

        return Optional.of(new Manager(birthDelay));

    }
}
