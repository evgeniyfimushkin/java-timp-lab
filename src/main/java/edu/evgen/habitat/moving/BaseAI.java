package edu.evgen.habitat.moving;

import edu.evgen.habitat.HabitatImpl;
import lombok.SneakyThrows;

public abstract class BaseAI implements Runnable {
    protected Thread thread;

    public BaseAI() {
        this.thread = new Thread(this);
        thread.start();
    }
    protected abstract void update();
    @Override
    @SneakyThrows
    public void run(){
        while(true){
            update();
            Thread.sleep(1);
        }
    }

}