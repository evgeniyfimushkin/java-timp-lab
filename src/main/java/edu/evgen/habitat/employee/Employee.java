package edu.evgen.habitat.employee;

import javafx.scene.image.ImageView;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Random;

@Data
public abstract class Employee implements IBehaviour {
    private final Random random = new Random();
    private final Long id;
    private final Long livingTime;
    private final ImageView imageView;
    private final Long paneSize;
    private final LocalDateTime birthTime = LocalDateTime.now();

    public Employee(String pic, Long paneSize, Long livingTime) {
        this.id = random.nextLong();
        this.livingTime = livingTime;
        this.paneSize = paneSize;
        this.imageView = new ImageView(pic);
        imageView.setX((int) (paneSize * random.nextDouble()));
        imageView.setY((int) (paneSize * random.nextDouble()));
    }
}
