package edu.evgen.habitat;

import edu.evgen.habitat.employee.IBehaviour;

import java.util.Optional;

public interface Habitat {
    Optional<IBehaviour> birthAttempt();
    public void setDeveloperDelay(Long developerDelay);

    public void setManagerDelay(Long managerDelay);

    public void setDeveloperProbability(Double developerProbability);

    public void setManagerRatio(Double managerRatio) ;

    public void setPaneSize(Long paneSize);
    Long getDeveloperDelay();
    Long getManagerDelay();
    Double getDeveloperProbability();
    Double getManagerRatio();
    Integer getDeveloperCount();
    Integer getManagerCount();
    void clear();
}
