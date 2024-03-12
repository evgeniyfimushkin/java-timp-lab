package edu.evgen;

import edu.evgen.habitat.employee.IBehaviour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.List;

public class ObjectsInfoController {

    @FXML
    TableView<IBehaviour> objectsTable;
    @FXML
    TableColumn<IBehaviour, Image> colImage;
    @FXML

    TableColumn<IBehaviour, Long> colBirthTime;
    @FXML
    TableColumn<IBehaviour, Class> colClass;
    @FXML
    TableColumn<IBehaviour, Long> colId;
    @FXML
    Button continueButton, stopButtonFromInfo;
    ObservableList<IBehaviour> objectsList;
    @FXML
    public void initialize(){
        colImage.setCellValueFactory(new PropertyValueFactory<>("EmployeeImage"));
        colBirthTime.setCellValueFactory(new PropertyValueFactory<>("EmployeeBirthTime"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("EmployeeClass"));
        colId.setCellValueFactory(new PropertyValueFactory<>("EmployeeId"));
    }
    public void setObservableList(Collection<IBehaviour> developers, Collection<IBehaviour> managers){
        try {
            objectsList = FXCollections.observableArrayList();
            objectsList.addAll(developers);
            objectsList.addAll(managers.stream().toList());
            objectsTable.setItems(objectsList);
        } catch (NullPointerException ignore){}
    }
}
