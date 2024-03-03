package edu.evgen.habitat;

import javafx.scene.image.ImageView;

import java.util.Optional;

public interface IBehaviour {
   Long getBirthDelay();

    /**
     * @return контейней в котором может быть employee или пустота
     */
    Optional<IBehaviour> birthAttempt();

    ImageView getImageView();
}
