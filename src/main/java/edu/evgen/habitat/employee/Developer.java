package edu.evgen.habitat.employee;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Developer extends Employee{


    public Developer(Long paneSize, Long livingTime) {
        super( "/developer.png", paneSize, livingTime);
    }

}
