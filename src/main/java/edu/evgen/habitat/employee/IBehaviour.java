package edu.evgen.habitat.employee;

import javafx.scene.image.ImageView;

import java.time.LocalDateTime;

public interface IBehaviour {
    LocalDateTime getBirthTime();
    Long getLivingTime();
    Long getId();
    void setId(Long id);
    ImageView getImageView();
    void disapear();
    ImageView getImageViewForTable();
    default Boolean mustDie(){
        return getBirthTime().plusSeconds(getLivingTime()).isBefore(LocalDateTime.now());
    };
    void move();
}
