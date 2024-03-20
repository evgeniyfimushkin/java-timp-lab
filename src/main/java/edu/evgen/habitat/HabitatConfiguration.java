package edu.evgen.habitat;

import lombok.Builder;
import lombok.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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


    public void save() throws IOException {
        FileWriter fileWriter = new FileWriter("config.txt");

        fileWriter.write("processDelay=" + processDelay + "\n");
        fileWriter.write("developerLivingTime=" + developerLivingTime + "\n");
        fileWriter.write("managerLivingTime=" + managerLivingTime + "\n");
        fileWriter.write("developerDelay=" + developerDelay + "\n");
        fileWriter.write("managerDelay=" + managerDelay + "\n");
        fileWriter.write("developerProbability=" + developerProbability + "\n");
        fileWriter.write("managerRatio=" + managerRatio + "\n");
        fileWriter.write("paneSize=" + paneSize + "\n");
        fileWriter.write("moveDelay=" + moveDelay + "\n");

        fileWriter.close();
    }
    public void load() throws IOException{
        FileReader fileReader = new FileReader("config.txt");
        BufferedReader reader = new BufferedReader(fileReader);
        String readLine;
        while ((readLine = reader.readLine()) != null){
            String[] parts = readLine.split("=");
            String fieldName = parts[0];
            String fieldValue = parts[1];
            if (parts.length == 2) {
                switch (fieldName) {
                    case "processDelay" -> processDelay = Long.parseLong(fieldValue);
                    case "developerLivingTime" -> developerLivingTime = Long.parseLong(fieldValue);
                    case "managerLivingTime" -> managerLivingTime = Long.parseLong(fieldValue);
                    case "developerDelay" -> developerDelay = Long.parseLong(fieldValue);
                    case "managerDelay" -> managerDelay = Long.parseLong(fieldValue);
                    case "developerProbability" -> developerProbability = Double.parseDouble(fieldValue);
                    case "managerRatio" -> managerRatio = Double.parseDouble(fieldValue);
                    case "paneSize" -> paneSize = Long.parseLong(fieldValue);
                    case "moveDelay" -> moveDelay = Long.parseLong(fieldValue);
                }
            }
        }
        reader.close();
        fileReader.close();
    }
}
