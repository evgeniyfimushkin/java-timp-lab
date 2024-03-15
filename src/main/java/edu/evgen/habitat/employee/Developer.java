package edu.evgen.habitat.employee;

import edu.evgen.habitat.moving.DeveloperAI;
import lombok.extern.slf4j.Slf4j;

import java.lang.Math.*;
import java.time.LocalDateTime;


@Slf4j

public class Developer extends Employee {

    static int minValue = -1;
    static int maxValue = 1;
    Integer speed = 1 + (int) (Math.random() * (5 - 1 + 1));
    Long timeToChangeVector = 1 + (long) (Math.random() * (2 - 1 + 1));
    Integer signX;
    Integer signY;
   public Integer setSignX() {
        return signX = minValue + (int) (Math.random() * (maxValue - minValue + 1));
    }

    public Integer setSignY() {
        return signY = minValue + (int) (Math.random() * (maxValue - minValue + 1));
    }
    private LocalDateTime checkPoint;

    public Developer(Long paneSize, Long livingTime) {
        super("/developer.png", paneSize, livingTime);
        setSignX();
        setSignY();
        checkPoint = getBirthTime();
    }

    @Override
    public void move() {
        if (LocalDateTime.now().isAfter(checkPoint.plusSeconds(timeToChangeVector)) || signX == 0 || signY == 0) {
            checkPoint = LocalDateTime.now();
            setSignX();
            setSignY();
            log.info("X = {}",this.signX);
            log.info("Y = {}",this.signY);
        }//Тут выдаёт NPE Cannot invoke "javafx.scene.Node.getScene()" because "node" is null
//        at javafx.graphics/javafx.scene.Scene$ScenePulseListener.synchronizeSceneNodes(Scene.java:2483)
        setX(this.getX() + signX * 0.1*speed);
        setY(this.getY() + signY * 0.1*speed);
    }
}
