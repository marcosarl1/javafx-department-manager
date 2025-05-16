package com.workshopjavafxjdbc.controller;

import com.workshopjavafxjdbc.Main;
import com.workshopjavafxjdbc.listener.DataChangeListener;
import com.workshopjavafxjdbc.model.Department;
import com.workshopjavafxjdbc.service.DepartmentService;
import com.workshopjavafxjdbc.util.Alerts;
import com.workshopjavafxjdbc.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable, DataChangeListener {

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
    public void onBtnNewAction(ActionEvent event) {
        Stage parentStage = Utils.currrentStage(event);
        Department department = new Department();
        createDialogForm(department, "/com/workshopjavafxjdbc/DepartmentForm.fxml", parentStage);
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

    private void createDialogForm(Department department, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = fxmlLoader.load();

            DepartmentFormController controller = fxmlLoader.getController();
            controller.setDepartment(department);
            controller.setDepartmentService(new DepartmentService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Department data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        updateTbView();
    }
}
