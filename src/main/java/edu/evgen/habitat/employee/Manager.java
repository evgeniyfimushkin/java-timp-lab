package edu.evgen.habitat.employee;

import javafx.application.Platform;

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
            move(
                    anchorPointX + anchorRadius * cos(alfa),
                    anchorPointY + anchorRadius * sin(alfa));
        }
    private Double initAnchorPoint(Double point){
        return (point + anchorRadius) > paneSize + anchorRadius ?
                (point - anchorRadius) :
                (point - anchorRadius) < 0 ? point : (point - anchorRadius);
    }
    protected void move(double x, double y) {
        if ((x < paneSize) && (x > 0))
            this.x = x;
        if ((y < paneSize) && (y > 0))
            this.y = y;
        Platform.runLater(() -> {
            imageView.setX(this.x);
            imageView.setY(this.y);
        });
    }
}
