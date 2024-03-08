package edu.evgen.habitat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HabitatConfiguration {

    private Long developerDelay;
    private Long managerDelay;
    private Double developerProbability;
    private Double managerRatio;
    private Long paneSize;
}
