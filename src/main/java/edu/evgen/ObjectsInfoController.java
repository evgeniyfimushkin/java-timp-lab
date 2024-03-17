package edu.evgen;

import edu.evgen.habitat.employee.EmployeesRepository;
import edu.evgen.habitat.employee.IBehaviour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

public class ObjectsInfoController {

    @FXML
    TableView<IBehaviour> objectsTable;
    @FXML
    TableColumn<IBehaviour, Image> colImage;
    @FXML

    TableColumn<IBehaviour, Long> colBirthTime;
    @FXML
    TableColumn<IBehaviour, Long> colId;
    @FXML
    Button continueButton, stopButtonFromInfo;

    @FXML
    public void initialize() {
        colImage.setCellValueFactory(new PropertyValueFactory<>("imageViewForTable"));
        colBirthTime.setCellValueFactory(new PropertyValueFactory<>("birthTime"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        setUpTable();
    }
    private void setUpTable() {
        ObservableList<IBehaviour> objectsList = FXCollections.observableArrayList();
        objectsList.addAll(EmployeesRepository.employees);
        objectsTable.setItems(objectsList);
        objectsTable.getSortOrder().add(colBirthTime);
        objectsTable.sort();
    }
}
