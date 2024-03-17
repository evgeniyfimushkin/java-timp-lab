package edu.evgen.habitat.employee;

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
        this.move(
                paneSize * random.nextDouble(),
                paneSize * random.nextDouble());
        this.pic = pic;
        EmployeesRepository.addEmployee(this);
    }
    protected abstract void move(Double x, Double y);
}
