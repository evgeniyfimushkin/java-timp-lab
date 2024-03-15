package edu.evgen.habitat.moving;
import edu.evgen.habitat.employee.*;


import static edu.evgen.habitat.HabitatImpl.habitat;

public class DeveloperAI extends BaseAI{
    public DeveloperAI(){super();}
    @Override
    protected void update() {
        synchronized (habitat.getDevelopers()){
            for (Employee iterator : habitat.getDevelopers()){//тут выдаёт Exception in thread "Thread-4" java.util.ConcurrentModificationException
//                at java.base/java.util.LinkedList$ListItr.checkForComodification(LinkedList.java:977)
//                at java.base/java.util.LinkedList$ListItr.next(LinkedList.java:899)
//                at edu.evgen.habitat.moving.DeveloperAI.update(DeveloperAI.java:12)
//                но продолжает работать
                    iterator.move();
            }
        }
    }
}
