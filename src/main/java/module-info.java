module org.example.urbanlink {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens org.example.urbanlink to javafx.fxml;
    //exports org.example.urbanlink;
    exports org.example.urbanlink.controllers;
    opens org.example.urbanlink.controllers to javafx.fxml;
}