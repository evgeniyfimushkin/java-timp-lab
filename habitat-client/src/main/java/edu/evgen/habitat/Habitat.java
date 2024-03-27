package edu.evgen.habitat;
import edu.evgen.habitat.employee.IBehaviour;
import java.util.Collection;
import java.util.Optional;

public interface Habitat {
    HabitatConfiguration getConfiguration();
    void setConfiguration(HabitatConfiguration configuration);
    Optional<? extends IBehaviour> developerBirthAttempt();
    Optional<? extends IBehaviour> managerBirthAttempt();
}
