package edu.evgen.habitat.employee;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Slf4j

public class Developer extends Employee {

    Double speed = (1 + (int) (Math.random() * (5 - 1 + 1)))*0.1;
    Long changeDirectionDelay = 1 + (long) (Math.random() * (2 - 1 + 1));
    Double xSpeed;
    Double ySpeed;
   private void changeDirection() {
       xSpeed = random.nextBoolean() ? speed : -speed;
       ySpeed = random.nextBoolean() ? speed : -speed;
    }

    private LocalDateTime checkPoint;

    public Developer(Long paneSize, Long livingTime) {
        super("/developer.png", paneSize, livingTime);
        changeDirection();
        checkPoint = getBirthTime();
    }

    @Override
    public void move() {
            if (LocalDateTime.now().isAfter(checkPoint.plusSeconds(changeDirectionDelay))) {
                checkPoint = LocalDateTime.now();
                changeDirection();
                log.info("X = {}", this.xSpeed);
                log.info("Y = {}", this.ySpeed);
            }//Тут выдаёт NPE Cannot invoke "javafx.scene.Node.getScene()" because "node" is null
//        at javafx.graphics/javafx.scene.Scene$ScenePulseListener.synchronizeSceneNodes(Scene.java:2483)
            moveX(x + xSpeed);
            moveY(y + ySpeed);
    }
}
