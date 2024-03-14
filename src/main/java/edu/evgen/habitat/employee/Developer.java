package edu.evgen.habitat.employee;

import edu.evgen.habitat.moving.DeveloperAI;
import lombok.extern.slf4j.Slf4j;
import java.lang.Math.*;


@Slf4j

public class Developer extends Employee{

//    static protected Thread thread = new Thread(
//            () -> {
//                Developer.setSignX();
//                Developer.setSignY();
//            }
//    );
//    static int minValue = -1;
//   static int maxValue = 1;
//    static Integer signX;
//   static Integer signY;
//
//
//   static public void setSignX() {
//        signX = minValue + (int) (Math.random() * (maxValue - minValue + 1));
//    }
//
//    static public void setSignY() {
//        signY = minValue + (int) (Math.random() * (maxValue - minValue + 1));
//    }

    public Developer(Long paneSize, Long livingTime) {
        super( "/developer.png", paneSize, livingTime);
    }

    @Override
    public void move() {
//        setX(this.getX()+signX*0.1);
//        setY(this.getY()+signY*0.1);
    }
}
