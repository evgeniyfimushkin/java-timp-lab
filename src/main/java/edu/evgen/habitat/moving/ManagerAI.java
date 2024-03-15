package edu.evgen.habitat.moving;
import edu.evgen.habitat.employee.*;
import static edu.evgen.habitat.HabitatImpl.habitat;

public class ManagerAI extends BaseAI{
    public ManagerAI() {
        super();
    }

    @Override
    protected void update() {
        synchronized (habitat.getManagers()){
            for (Employee iterator: habitat.getManagers()){
                    iterator.move();
            }
        }
    }
}
