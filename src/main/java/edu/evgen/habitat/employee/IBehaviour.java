package edu.evgen.habitat.employee;

import javafx.scene.image.ImageView;

import java.time.LocalDateTime;

public interface IBehaviour {
    LocalDateTime getBirthTime();
    Long getLivingTime();
    Long getId();

    ImageView getImageView();
    default Boolean mustDie(){
        return getBirthTime().plusSeconds(getLivingTime()).isBefore(LocalDateTime.now());
    };
}
