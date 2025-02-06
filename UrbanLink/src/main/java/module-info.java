module com.example.urbanlink {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.urbanlink to javafx.fxml;
    exports com.example.urbanlink;
}