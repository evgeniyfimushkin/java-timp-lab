package edu.evgen.habitat.employee;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Manager extends Employee {
    private final Integer anchorRadius = 80;
    private Double alfa = 0.0;
    private final Double anchorPointX;
    private final Double anchorPointY;
    public Manager(Long paneSize, Long livingTime) {
        super("/manager.png", paneSize, livingTime);
        this.anchorPointX = initAnchorPoint(x);
        this.anchorPointY = initAnchorPoint(y);
    }
    @Override
    public void move() {
            alfa += 0.01;
            moveX(anchorPointX + anchorRadius * cos(alfa));
            moveY(anchorPointY + anchorRadius * sin(alfa));
        }
    private Double initAnchorPoint(Double point){
        return (point + anchorRadius) > paneSize + anchorRadius ?
                (point - anchorRadius) :
                (point - anchorRadius) < 0 ? point : (point - anchorRadius);
    }
}
