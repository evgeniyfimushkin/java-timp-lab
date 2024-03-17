package edu.evgen.habitat.employee;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Slf4j

public class Developer extends Employee {

    final Double speed = random.nextInt(5) * 0.1;
    final Long changeDirectionDelay = random.nextBoolean() ? 1L : 2L;
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
            }
            move(
                    x + xSpeed,
                    y + ySpeed);
    }
}
