package com.workshopjavafxjdbc.controller;

import com.workshopjavafxjdbc.Main;
import com.workshopjavafxjdbc.exception.DbException;
import com.workshopjavafxjdbc.listener.DataChangeListener;
import com.workshopjavafxjdbc.model.Seller;
import com.workshopjavafxjdbc.service.SellerService;
import com.workshopjavafxjdbc.service.SellerService;
import com.workshopjavafxjdbc.util.Alerts;
import com.workshopjavafxjdbc.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerListController implements Initializable, DataChangeListener {

    private SellerService sellerService;

    @FXML
    private TableView<Seller> tbSeller;

    @FXML
    private TableColumn<Seller, Integer> tbColId;

    @FXML
    private TableColumn<Seller, String> tbColName;

    @FXML
    private TableColumn<Seller, String> tbColEmail;

    @FXML
    private TableColumn<Seller, LocalDate> tbColBirthDate;

    @FXML
    private TableColumn<Seller, Double> tbColBaseSalary;

    @FXML
    private TableColumn<Seller, Seller> tblColEdit;

    @FXML
    private TableColumn<Seller, Seller> tblColRemove;

    @FXML
    private Button btnNew;

    private ObservableList<Seller> obsList;

    @FXML
    public void onBtnNewAction(ActionEvent event) {
        Stage parentStage = Utils.currrentStage(event);
        Seller Seller = new Seller();
        createDialogForm(Seller, "/com/workshopjavafxjdbc/SellerForm.fxml", parentStage);
    }

    public void setSellerService(SellerService SellerService) {
        this.sellerService = SellerService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tbColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbColEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbColBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tbColBirthDate, "dd/MM/yyyy");
        tbColBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(tbColBaseSalary, 2);

        Stage stage = (Stage) Main.getScene().getWindow();
        tbSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTbView() {
        if (sellerService == null) {
            throw new IllegalArgumentException("Service was null");
        }

        List<Seller> Sellers = sellerService.findAll();
        obsList = FXCollections.observableArrayList(Sellers);
        tbSeller.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Seller Seller, String absoluteName, Stage parentStage) {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(absoluteName));
//            Pane pane = fxmlLoader.load();
//
//            ellerFormController controller = fxmlLoader.getController();
//            controller.setSeller(Seller);
//            controller.setSellerService(new SellerService());
//            controller.subscribeDataChangeListener(this);
//            controller.updateFormData();
//
//
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("Enter Seller data");
//            dialogStage.setScene(new Scene(pane));
//            dialogStage.setResizable(false);
//            dialogStage.initOwner(parentStage);
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.showAndWait();
//        } catch (IOException e) {
//            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
//        }
    }

    @Override
    public void onDataChanged() {
        updateTbView();
    }

    private void initEditButtons() {
        tblColEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tblColEdit.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("edit");

            @Override
            protected void updateItem(Seller Seller, boolean empty) {
                super.updateItem(Seller, empty);

                if (Seller == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(Seller, "/com/workshopjavafxjdbc/SellerForm.fxml", Utils.currrentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tblColRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tblColRemove.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("remove");

            @Override
            protected void updateItem(Seller Seller, boolean empty) {
                super.updateItem(Seller, empty);

                if (Seller == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(Seller));
            }
        });
    }

    private void removeEntity(Seller Seller) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
        if (result.get() == ButtonType.OK) {
            if (sellerService == null) {
                throw new IllegalStateException("Service was null");
            }
            try {
                sellerService.remove(Seller);
                updateTbView();
            } catch (DbException e) {
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
