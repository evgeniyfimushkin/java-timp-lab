package edu.evgen.habitat.employee;


import javafx.scene.image.ImageView;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

//Data: ломбок генерит конструктор, геттеры и сеттеры
@Data
public abstract class Employee implements IBehaviour {

    protected final Long birthDelay;

    protected LocalDateTime lastBirthAttempt = LocalDateTime.now();

    protected final Random random = new Random();
    protected final ImageView imageView;
    protected final Long paneSize;
    protected final LocalDateTime birthTime = LocalDateTime.now();

    public Optional<IBehaviour> birthAttempt(){
        return Optional.empty();
    }

    public Employee(Long birthDelay,  String pic, Long paneSize) {
        this.paneSize = paneSize;
        this.birthDelay = birthDelay;
        this.imageView = new ImageView(pic);
        imageView.setX((int) (paneSize * random.nextDouble()));
        imageView.setY((int) (paneSize * random.nextDouble()));
    }
}
