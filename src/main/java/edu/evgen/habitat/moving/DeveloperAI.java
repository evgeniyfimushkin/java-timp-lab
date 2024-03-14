package edu.evgen.habitat.moving;
import edu.evgen.habitat.employee.Employee;

import static edu.evgen.habitat.HabitatImpl.habitat;

public class DeveloperAI extends BaseAI{

    public DeveloperAI(){super();}
    @Override
    protected void update() {
        synchronized (habitat.getDevelopers()){
            for (Employee iterator : habitat.getDevelopers()){
                iterator.setX(iterator.getX()+0.1);
            }
        }
    }
}
