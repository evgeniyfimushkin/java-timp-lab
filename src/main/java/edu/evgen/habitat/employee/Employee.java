package edu.evgen.habitat.employee;

import javafx.scene.image.ImageView;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Random;

@Data
public abstract class Employee implements IBehaviour {


    protected final Random random = new Random();
    protected final ImageView imageView;
    protected final Long paneSize;
    protected final LocalDateTime birthTime = LocalDateTime.now();

    public Employee(String pic, Long paneSize) {
        this.paneSize = paneSize;
        this.imageView = new ImageView(pic);
        imageView.setX((int) (paneSize * random.nextDouble()));
        imageView.setY((int) (paneSize * random.nextDouble()));
    }
}
