package edu.evgen.habitat.moving;

import edu.evgen.habitat.employee.*;


import static edu.evgen.habitat.HabitatImpl.habitat;

public class DeveloperAI extends BaseAI {
    public DeveloperAI(Long moveDelay) {
        super(moveDelay);
    }
    @Override
    protected void update() {
        EmployeesRepository.getDevelopers().forEach(IBehaviour::move);
    }
}
