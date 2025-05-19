package com.workshopjavafxjdbc.controller;

import com.workshopjavafxjdbc.exception.DbException;
import com.workshopjavafxjdbc.exception.ValidationException;
import com.workshopjavafxjdbc.listener.DataChangeListener;
import com.workshopjavafxjdbc.model.Department;
import com.workshopjavafxjdbc.model.Seller;
import com.workshopjavafxjdbc.service.DepartmentService;
import com.workshopjavafxjdbc.service.SellerService;
import com.workshopjavafxjdbc.util.Alerts;
import com.workshopjavafxjdbc.util.Constraints;
import com.workshopjavafxjdbc.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller seller;

    private SellerService sellerService;

    private DepartmentService departmentService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirtDate;

    @FXML
    private TextField txtBaseSalary;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorBirthDate;

    @FXML
    private Label labelErrorBaseSalary;

    @FXML
    private ComboBox<Department> cbxDepartment;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    private ObservableList<Department> obsList;


    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setServices(SellerService sellerService, DepartmentService departmentService) {
        this.sellerService = sellerService;
        this.departmentService = departmentService;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtnSaveAction(ActionEvent event) {
        if (seller == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (sellerService == null) {
            throw new IllegalStateException("Service was null");
        }

        try {
            seller = getFormData();
            sellerService.saveOrUpdate(seller);
            notifyDataChangeListeners();
            Utils.currrentStage(event).close();

        } catch (ValidationException ex) {
            setErrorMessages(ex.getErrors());
        }catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Seller getFormData() {
        Seller obj = new Seller();

        ValidationException validationException = new ValidationException("Validation error");

        obj.setId(Utils.tryParseToInt(txtId.getText()));
        if (txtName.getText() == null || txtName.getText().trim().isEmpty()) {
            validationException.addError("name", "Field can't be empty");
        }
        obj.setName(txtName.getText());

        if (txtEmail.getText() == null || txtEmail.getText().trim().isEmpty()) {
            validationException.addError("email", "Field can't be empty");
        }
        obj.setEmail(txtEmail.getText());

        if (dpBirtDate.getValue() == null) {
            validationException.addError("birthDate", "Field can't be empty");
        } else {
            LocalDate date = dpBirtDate.getValue();
            obj.setBirthDate(date);
        }

        if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().isEmpty()) {
            validationException.addError("baseSalary", "Field can't be empty");
        }
        obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));

        obj.setDepartment(cbxDepartment.getValue());

        if (!validationException.getErrors().isEmpty()) {
            throw validationException;
        }

        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currrentStage(event).close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dpBirtDate, "dd/MM/yyyy");
        initComboBoxDepartment();
    }

    public void updateFormData() {
        if (seller == null) {
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(seller.getId()));
        txtName.setText(seller.getName());
        txtEmail.setText(seller.getEmail());
        txtBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
        dpBirtDate.setValue(seller.getBirthDate());

        if (seller.getDepartment() == null) {
            cbxDepartment.getSelectionModel().selectFirst();
        }
        cbxDepartment.setValue(seller.getDepartment());
    }

    public void loadAssociatedObjects() {
        List<Department> departments = departmentService.findAll();
        obsList = FXCollections.observableArrayList(departments);
        cbxDepartment.setItems(obsList);
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
        labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
        labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
        labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));
    }

    private void initComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText(empty ? "" : department.getName());
            }
        };
        cbxDepartment.setCellFactory(factory);
        cbxDepartment.setButtonCell(factory.call(null));
    }
}
