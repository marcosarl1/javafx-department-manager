package com.workshopjavafxjdbc.controller;

import com.workshopjavafxjdbc.exception.DbException;
import com.workshopjavafxjdbc.exception.ValidationException;
import com.workshopjavafxjdbc.listener.DataChangeListener;
import com.workshopjavafxjdbc.model.Seller;
import com.workshopjavafxjdbc.service.SellerService;
import com.workshopjavafxjdbc.util.Alerts;
import com.workshopjavafxjdbc.util.Constraints;
import com.workshopjavafxjdbc.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller seller;

    private SellerService sellerService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;


    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
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
        Seller dep = new Seller();

        ValidationException validationException = new ValidationException("Validation error");

        dep.setId(Utils.tryParseToInt(txtId.getText()));
        if (txtName.getText() == null || txtName.getText().trim().isEmpty()) {
            validationException.addError("name", "Field can't be empty");
        }
        dep.setName(txtName.getText());

        if (!validationException.getErrors().isEmpty()) {
            throw validationException;
        }

        return dep;
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
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    public void updateFormData() {
        if (seller == null) {
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(seller.getId()));
        txtName.setText(seller.getName());
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("name")) {
            labelErrorName.setText(errors.get("name"));
        }
    }
}
