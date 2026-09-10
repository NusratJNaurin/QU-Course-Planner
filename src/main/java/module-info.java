module com.example.smartcourseplanner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires javafx.graphics;

    opens com.example.smartcourseplanner to javafx.fxml;
    exports com.example.smartcourseplanner;
}