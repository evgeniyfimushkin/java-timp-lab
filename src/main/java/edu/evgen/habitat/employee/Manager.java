package edu.evgen.habitat.employee;

public class Manager extends Employee {
    public Manager(Long paneSize, Long livingTime) {
        super( "/manager.png", paneSize, livingTime);
    }
    @Override
    public void move() {
        
    }
}
