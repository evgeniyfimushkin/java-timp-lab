package edu.evgen.habitat.moving;

import edu.evgen.habitat.HabitatImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseAI implements Runnable {
    public Thread thread;

    public void threadStart() {
        log.info("Base AI Thread started");
        thread = new Thread(this);
        thread.start();
    }

    public void interruptThread() {
        log.info("Base AI Thread Interrupted");
        thread.interrupt();
    }

    public BaseAI() {
    }

    protected abstract void update();

    @Override
    public void run() {
        try {
            while (!thread.isInterrupted()) {
                thread.sleep(1);
                update();
            }
        } catch (InterruptedException e) {

        }
    }

}