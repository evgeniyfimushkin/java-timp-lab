package edu.evgen;

import edu.evgen.habitat.employee.IBehaviour;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;

public class ObjectsInfoController {

    @FXML
    TableView<IBehaviour> objectsTable;
    @FXML
    TableColumn<IBehaviour, Image> colImage;
    TableColumn<IBehaviour, Long> colBirthTime;
    TableColumn<IBehaviour, Class> colClass;
    TableColumn<IBehaviour, Long> colId;
    @FXML
    Button continueButton, stopButtonFromInfo;

    public void initialize(Throwable e){

    }
}
