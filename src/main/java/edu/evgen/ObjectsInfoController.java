package edu.evgen;

import edu.evgen.habitat.employee.Employee;
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
    TableView<Employee> objectsTable;
    @FXML
    TableColumn<Employee, Image> colImage;
    @FXML

    TableColumn<Employee, Long> colBirthTime;
    @FXML
    TableColumn<Employee, Class> colClass;
    @FXML
    TableColumn<Employee, Long> colId;
    @FXML
    Button continueButton, stopButtonFromInfo;
    ObservableList<Employee> objectsList;
    @FXML
    public void initialize(){
        colImage.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        colBirthTime.setCellValueFactory(new PropertyValueFactory<>("birthTime"));
//        colClass.setCellValueFactory(new PropertyValueFactory<>("EmployeeClass"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    }
    public void setObservableList(Collection<Employee> developers, Collection<Employee> managers){
        try {
            objectsList = FXCollections.observableArrayList();
            objectsList.addAll(developers);
            objectsList.addAll(managers.stream().toList());
            objectsTable.setItems(objectsList);
            objectsTable.getSortOrder().add(colBirthTime);
            objectsTable.sort();

        } catch (NullPointerException ignore){}
    }
}
