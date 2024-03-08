package edu.evgen;

import edu.evgen.habitat.Habitat;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Data;


public class stopSimulationInfoController {
    Stage stage;

    @FXML
    Label developersOptionLabel, managersOptionLabel,
            simulationTime,
            developersCountLabel, managersCountLabel,
            managersDelayLabel, developersDelayLabel,
            developersProbabilityLabel, managersRatioLabel;
    @FXML
    Button continueButton, stopButtonFromInfo;
    public void setAllTheLabels(Habitat habitat){
        developersCountLabel.setText(habitat.getDeveloperCount().toString());
        managersCountLabel.setText(habitat.getManagerCount().toString());
        managersDelayLabel.setText("Delay: " + habitat.getConfiguration().getManagerDelay().toString());
        developersDelayLabel.setText("Delay: " + habitat.getConfiguration().getDeveloperDelay().toString());
        managersRatioLabel.setText("Ratio: " + habitat.getConfiguration().getManagerRatio().toString());
        developersProbabilityLabel.setText("Probability: " + habitat.getConfiguration().getDeveloperProbability().toString());
    }

}
