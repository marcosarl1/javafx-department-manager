package com.workshopjavafxjdbc.controller;

import com.workshopjavafxjdbc.Main;
import com.workshopjavafxjdbc.model.Department;
import com.workshopjavafxjdbc.service.DepartmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    private DepartmentService departmentService;

    @FXML
    private TableView<Department> tbDepartment;

    @FXML
    private TableColumn<Department, Integer> tbColId;

    @FXML
    private TableColumn<Department, String> tbColName;

    @FXML
    private Button btnNew;

    private ObservableList<Department> obsList;

    @FXML
    public void onBtnNewAction() {

    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tbColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbColName.setCellValueFactory(new PropertyValueFactory<>("name"));

        Stage stage = (Stage) Main.getScene().getWindow();
        tbDepartment.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTbView() {
        if (departmentService == null) {
            throw new IllegalArgumentException("Service was null");
        }

        List<Department> departments = departmentService.findAll();
        obsList = FXCollections.observableArrayList(departments);
        tbDepartment.setItems(obsList);
    }
}
