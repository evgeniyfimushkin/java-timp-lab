package edu.evgen.habitat;

import javafx.scene.image.ImageView;

import java.util.Optional;

public interface IBehaviour {
   Long getBirthDelay();

   String getPic();


    /**
     * @return контейней в котором может быть employee или пустота
     */
    Optional<IBehaviour> birthAttempt();

    default ImageView getImageView() {
        return new ImageView(getPic());
    }
}
