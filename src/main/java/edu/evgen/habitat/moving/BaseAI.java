package edu.evgen.habitat.moving;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseAI implements Runnable {
    public Thread thread;
    private final Long moveDelay;
    public void threadStart() {
        log.info("Base AI Thread started");
        thread = new Thread(this);
        thread.start();
    }
    public void interruptThread() {
        log.info("Base AI Thread Interrupted");
        thread.interrupt();
    }
    protected abstract void update();

    @Override
    public void run() {
        try {
            while (!thread.isInterrupted()) {
                update();
                Thread.sleep(moveDelay);
            }
        } catch (InterruptedException e) {
        }
    }

}