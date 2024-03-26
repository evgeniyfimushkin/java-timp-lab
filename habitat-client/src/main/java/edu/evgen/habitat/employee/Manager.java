package edu.evgen.habitat.employee;

import javafx.application.Platform;

import java.io.Serializable;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Manager extends Employee{
    private final Integer anchorRadius = 80;
    private Double alfa = 0.0;
    private final Double speed = 0.01 * (random.nextDouble()+0.3);
    private final Double anchorPointX;
    private final Double anchorPointY;
    Integer direction = random.nextBoolean() ? 1 : -1;
    public Manager(Long paneSize, Long livingTime) {
        super("/manager.png", paneSize, livingTime);
        this.anchorPointX = initAnchorPoint(x);
        this.anchorPointY = initAnchorPoint(y);
    }

    public Manager(Manager employee) {
        super(employee);
        this.alfa = employee.alfa;
        this.anchorPointX = employee.anchorPointX;
        this.anchorPointY = employee.anchorPointY;
        this.direction = employee.direction;
    }

    public Manager (){
        super("/manager.png", 400L, 5L);
        this.anchorPointX = initAnchorPoint(x);
        this.anchorPointY = initAnchorPoint(y);
    }


    @Override
    public void move() {
            alfa += direction*speed;
            move(
                    anchorPointX + anchorRadius * cos(alfa),
                    anchorPointY + anchorRadius * sin(alfa));
        }
    private Double initAnchorPoint(Double point){
        return (point + anchorRadius) > paneSize + anchorRadius ?
                (point - anchorRadius) :
                (point - anchorRadius) < 0 ? point : (point - anchorRadius);
    }
    protected void move(Double x, Double y) {
        if ((x < paneSize) && (x > 0))
            this.x = x;
        else
            direction = -direction;
        if ((y < paneSize) && (y > 0))
            this.y = y;
        else
            direction = -direction;
        Platform.runLater(() -> {
            imageView.setX(this.x);
            imageView.setY(this.y);
        });
    }
}
