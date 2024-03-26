package edu.evgen.habitat.employee;
import javafx.scene.image.ImageView;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Random;
@Data
public abstract class Employee implements IBehaviour, Serializable {
    protected double x, y;
    protected final Random random = new Random();
    protected Long id;
    protected final Long livingTime;
    protected transient final ImageView imageView;
    protected transient final ImageView imageViewForTable;
    protected final String pic;
    protected final Long paneSize;
    protected transient final LocalDateTime birthTime = LocalDateTime.now();

    protected Double disapearCof;

    public Employee(String pic, Long paneSize, Long livingTime) {
        this.livingTime = livingTime;
        this.paneSize = paneSize;
        this.pic = pic;
        this.imageView = new ImageView(this.pic);
        this.imageViewForTable = new ImageView(this.pic);
        this.move(
                paneSize * random.nextDouble(),
                paneSize * random.nextDouble());
        this.disapearCof = (double)this.livingTime;
        EmployeesRepository.addEmployee(this);
    }
    public Employee(Employee employee){
        this.x = employee.x;
        this.y = employee.y;
        this.livingTime = employee.livingTime;
        this.pic = employee.pic;
        this.imageView = new ImageView(pic);
        this.imageViewForTable = new ImageView(pic);
        this.paneSize = employee.paneSize;
        this.disapearCof = employee.disapearCof;

        EmployeesRepository.addEmployee(this);
    }

    protected abstract void move(Double x, Double y);
    public void disapear(){
        disapearCof-=0.5;
        imageView.setOpacity(disapearCof/livingTime);
    }
}
