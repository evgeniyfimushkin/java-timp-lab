package edu.evgen;

import edu.evgen.habitat.Habitat;
import edu.evgen.habitat.employee.EmployeesRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class StopSimulationInfoController {
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
        developersCountLabel.setText(String.valueOf(EmployeesRepository.getDevelopers().size()));
        managersCountLabel.setText(String.valueOf(EmployeesRepository.getManagers().size()));
        managersDelayLabel.setText("Delay: " + habitat.getConfiguration().getManagerDelay().toString());
        developersDelayLabel.setText("Delay: " + habitat.getConfiguration().getDeveloperDelay().toString());
        managersRatioLabel.setText("Ratio: " + habitat.getConfiguration().getManagerRatio().toString());
        developersProbabilityLabel.setText("Probability: " + habitat.getConfiguration().getDeveloperProbability().toString());
    }

}
