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
    Button closeButton;
    @FXML
    private void initialize(){
        closeButton.setOnAction(event -> stage.close());

    }
    public void setAllTheLabels(Habitat habitat){
        developersCountLabel.setText(habitat.getDeveloperCount().toString());
        managersCountLabel.setText(habitat.getManagerCount().toString());
        managersDelayLabel.setText("Delay: " + habitat.getManagerDelay().toString());
        developersDelayLabel.setText("Delay: " + habitat.getDeveloperDelay().toString());
        managersRatioLabel.setText("Ratio: " + habitat.getManagerRatio().toString());
        developersProbabilityLabel.setText("Probability: " + habitat.getDeveloperProbability().toString());

    }
    public void setCloseButton(Stage stage){
        this.stage = stage;
    }
}