package edu.evgen.habitat.employee;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Random;

@Data
public abstract class Employee implements IBehaviour {
    protected double x, y;
    protected final Random random = new Random();
    protected Long id;
    protected final Long livingTime;
    protected final ImageView imageView;
    protected final ImageView imageViewForTable;
    protected final String pic;
    protected final Long paneSize;
    protected final LocalDateTime birthTime = LocalDateTime.now();

    public Employee(String pic, Long paneSize, Long livingTime) {
        this.livingTime = livingTime;
        this.paneSize = paneSize;
        this.imageView = new ImageView(pic);
        this.imageViewForTable = new ImageView(pic);
        this.moveX((int) (paneSize * random.nextDouble()));
        this.moveY((int) (paneSize * random.nextDouble()));
        this.pic = pic;
        EmployeesRepository.addEmployee(this);
    }


    protected void moveX(double x) {
        if ((x < paneSize) && (x > 0)) {
            this.x = x;
            imageView.setX(x);
        }
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

    protected void moveY(double y) {
        if ((y < paneSize) && (y > 0)) {
            this.y = y;
            imageView.setY(y);
        }
    }

}
