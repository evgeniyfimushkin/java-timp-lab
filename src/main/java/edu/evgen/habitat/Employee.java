package edu.evgen.habitat;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

//Data: ломбок генерит конструктор
@Data
public abstract class Employee implements IBehaviour {

    protected final Long birthDelay;

    protected LocalDateTime lastBirthAttempt = LocalDateTime.now();

    protected final Random random = new Random();

    protected Integer x;

    protected  Integer y;

    protected final String pic;

    public Optional<IBehaviour> birthAttempt(){
        return Optional.empty();
    }
}
