package edu.evgen.habitat;

import java.util.Optional;

public interface IBehaviour {
   Long getBirthDelay();


    /**
     * @return контейней в котором может быть employee или пустота
     */
    Optional<IBehaviour> birthAttempt();
}
