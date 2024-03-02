package edu.evgen.habitat;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class Developer extends Employee{

    protected final Double birthProbability;

    public Developer(Long birthDelay, Double birthProbability) {
        super(birthDelay, "/developer.png");
        this.birthProbability = birthProbability;
    }

    @Override
    public Optional<IBehaviour> birthAttempt() {
        log.info("birthAttempt <-");
        LocalDateTime now = LocalDateTime.now();

        if (lastBirthAttempt.plusSeconds(birthDelay).isAfter(now))
            return Optional.empty();

        lastBirthAttempt = now;

        return (random.nextDouble() > birthProbability) ? Optional.of(new Developer(birthDelay, birthProbability)) : Optional.empty();

    }
}
