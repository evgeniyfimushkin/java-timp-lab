package edu.evgen.habitat.employee;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Developer extends Employee{

    protected final Double birthProbability;

    public Developer(Double birthProbability, Long paneSize) {
        super( "/developer.png", paneSize);
        this.birthProbability = birthProbability;
    }

}
