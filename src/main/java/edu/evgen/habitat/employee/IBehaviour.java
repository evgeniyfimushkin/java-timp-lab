package edu.evgen.habitat.employee;

import javafx.scene.image.ImageView;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IBehaviour {
   Long getBirthDelay();

    /**
     * @return контейней в котором может быть employee или пустота
     */
    Optional<IBehaviour> birthAttempt();
    LocalDateTime getBirthTime();

    ImageView getImageView();
}
