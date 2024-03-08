package edu.evgen.habitat.employee;

import javafx.scene.image.ImageView;

import java.time.LocalDateTime;

public interface IBehaviour {
    LocalDateTime getBirthTime();

    ImageView getImageView();
}
