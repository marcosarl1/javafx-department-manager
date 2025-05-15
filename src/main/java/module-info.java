module com.workshopjavafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.workshopjavafxjdbc to javafx.fxml;
    opens com.workshopjavafxjdbc.controller to javafx.fxml;
    exports com.workshopjavafxjdbc;
}