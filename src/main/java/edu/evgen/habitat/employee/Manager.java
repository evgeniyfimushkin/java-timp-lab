package edu.evgen.habitat.employee;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Manager extends Employee {
    private Integer anchorRadius = 80;
    private Double alfa = 0.0;
    private Double anchorPointX =
            (this.getX() + anchorRadius) > getPaneSize()
                    ?
                    (this.getX() - anchorRadius)
                    :
                    (this.getX() - anchorRadius) < 0 ? getX():(this.getX() - anchorRadius);
    private Double anchorPointY =
            (this.getY() + anchorRadius) > getPaneSize()
                    ?
                    (this.getY() - anchorRadius)
                    :
                    (this.getY() - anchorRadius) < 0 ? getY():(this.getY() - anchorRadius);

    public Manager(Long paneSize, Long livingTime) {
        super("/manager.png", paneSize, livingTime);
    }

    @Override
    public void move() {
        alfa += 0.01;
        setX(anchorPointX + anchorRadius * cos(alfa));
        setY(anchorPointY + anchorRadius * sin(alfa));
    }
}
