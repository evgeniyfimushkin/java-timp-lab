package edu.evgen.server;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "edu.evgen.server")
@EnableScheduling
@EnableAsync
@Slf4j
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
