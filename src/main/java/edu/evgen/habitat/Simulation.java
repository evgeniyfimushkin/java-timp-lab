package edu.evgen.habitat;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Simulation implements Runnable {
    private final Runnable action;
    private final Long moveDelay;
    private final Thread thread;
    private final Object runningMutex = new Object();
    public Boolean run = false;

    public Simulation(Runnable action, Long moveDelay, String threadName){
        this.action = action;
        this.moveDelay = moveDelay;
        this.thread = new Thread(this,threadName);
    }
    @Override
    public void run() {
        log.info("start moving");
        run = true;
        try {
            do {
                synchronized (runningMutex) {
                    while (!run) {
                        runningMutex.wait();
                    }
                }
                Thread.sleep(moveDelay);
                action.run();
            } while (true);
        } catch (Throwable trowable) {
            log.error("moving error: ", trowable);
        }
        log.info("stop moving");
    }

    public void continueSimulation() {
        synchronized (runningMutex) {
            run = true;
            runningMutex.notify();
        }
    }
    public void pauseSimulation(){
        run = false;
    }

}