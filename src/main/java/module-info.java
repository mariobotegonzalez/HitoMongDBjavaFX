module com.empresa.hitojavafxmongo {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;

    opens com.empresa.hitojavafxmongo to javafx.fxml;
    exports com.empresa.hitojavafxmongo;
}