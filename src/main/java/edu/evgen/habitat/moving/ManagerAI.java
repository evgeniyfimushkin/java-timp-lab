package edu.evgen.habitat.moving;
import edu.evgen.habitat.employee.*;
import static edu.evgen.habitat.HabitatImpl.habitat;

public class ManagerAI extends BaseAI{
    public ManagerAI(Long moveDelay) {
        super(moveDelay);
    }
    @Override
    protected void update() {
        EmployeesRepository.getManagers().forEach(IBehaviour::move);
    }
}