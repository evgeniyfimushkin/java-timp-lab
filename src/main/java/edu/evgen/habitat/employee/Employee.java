package edu.evgen.habitat.employee;

import javafx.scene.image.ImageView;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.UUID;

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

//        allID.add(this.id);
//        allBirthTimes.put(id,this.birthTime);

        this.livingTime = livingTime;
        this.paneSize = paneSize;
        this.imageView = new ImageView(pic);
        imageView.setX((int) (paneSize * random.nextDouble()));
        imageView.setY((int) (paneSize * random.nextDouble()));
    }
}
