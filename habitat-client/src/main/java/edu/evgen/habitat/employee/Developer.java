package edu.evgen.habitat.employee;

import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Slf4j

public class Developer extends Employee {

    final Double speed = (1 + random.nextInt(5)) * 0.1;
    final Long changeDirectionDelay = random.nextBoolean() ? 1L : 2L;
    Double xSpeed;
    Double ySpeed;
   private void changeDirection() {
       xSpeed = random.nextBoolean() ? speed : -speed;
       ySpeed = random.nextBoolean () ? speed : -speed;
    }
    private LocalDateTime checkPoint;
    public Developer(Long paneSize, Long livingTime) {
        super("/developer.png", paneSize, livingTime);
        changeDirection();
        checkPoint = getBirthTime();
    }

    public Developer(Developer employee) {
        super(employee);
        this.xSpeed = employee.xSpeed;
        this.ySpeed = employee.ySpeed;
        this.checkPoint = employee.checkPoint;
    }

    public Developer(Developer employee,Boolean key) {
        super(employee, key);
        this.xSpeed = employee.xSpeed;
        this.ySpeed = employee.ySpeed;
        this.checkPoint = employee.checkPoint;
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
    @Override
    protected void move(Double x, Double y) {
        if ((x < paneSize) && (x > 0))
            this.x = x;
        else
            xSpeed = -xSpeed;
        if ((y < paneSize) && (y > 0))
            this.y = y;
        else
            ySpeed = -ySpeed;
        Platform.runLater(() -> {
            imageView.setX(this.x);
            imageView.setY(this.y);
        });
    }
}
