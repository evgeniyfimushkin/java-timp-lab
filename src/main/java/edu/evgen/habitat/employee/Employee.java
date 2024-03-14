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
    private double x,y;
    private final Random random = new Random();
    private Long id;
    private final Long livingTime;
    private final ImageView imageView;
    private final ImageView imageViewForTable;
    private final String pic;
    private final Long paneSize;
    private final LocalDateTime birthTime = LocalDateTime.now();

    private final static TreeSet<Long> allID = new TreeSet();
    private final static HashMap<Long,LocalDateTime> allBirthTimes = new HashMap();

    public Employee(String pic, Long paneSize, Long livingTime) {
        setUniqId();
        this.livingTime = livingTime;
        this.paneSize = paneSize;
        this.imageView = new ImageView(pic);
        this.imageViewForTable = new ImageView(pic);
        this.setX((int) (paneSize * random.nextDouble()));
        this.setY((int) (paneSize * random.nextDouble()));
        this.pic=pic;
        allBirthTimes.put(this.id,this.birthTime);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        if ((x < paneSize)&&(x>0)){
            synchronized (imageView) {
                imageView.setX(x);
            }
        }
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        if ((y < paneSize)&&(y>0)) {
            synchronized (imageView) {
                imageView.setY(y);
            }
        }
    }

    void setUniqId(){
        Long temp = random.nextLong();
        if (!allID.contains(temp)){
            this.id=temp;
            allID.add(this.id);
        }
       else setUniqId();
    }

    public static HashMap<Long, LocalDateTime> getAllBirthTimes() {
        return allBirthTimes;
    }

    public static TreeSet<Long> getAllID() {
        return allID;
    }
}
