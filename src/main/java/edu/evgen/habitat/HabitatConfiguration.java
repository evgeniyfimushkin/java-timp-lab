package edu.evgen.habitat;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.*;
import java.util.Properties;

@Data
@Builder
public class HabitatConfiguration {
    private Long processDelay;
    private Long developerLivingTime;
    private Long managerLivingTime;
    private Long developerDelay;
    private Long managerDelay;
    private Double developerProbability;
    private Double managerRatio;
    private Long paneSize;
    private Long moveDelay;
    private final Properties properties = new Properties();

//    public Long getProcessDelay(){
//        return Long.parseLong(properties.getProperty("processDelay", "100"));
//    }
//
//    void setProcessDelay(Long processDelay) {
//        properties.setProperty("processDelay", processDelay.toString());
//    }

    @SneakyThrows
    public void save() {
        HabitatProperty.merge(this,properties);
        try (FileOutputStream outputStream = new FileOutputStream("config.properties")) {
            properties.store(outputStream, null);
        }
    }
    public void load() {
        try (FileReader fileReader = new FileReader("config.properties");) {
            properties.load(fileReader);
        } catch (IOException e) {
        }
        HabitatProperty.readProperties(this,properties);
    }
}
