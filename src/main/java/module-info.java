module com.example {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires transitive java.desktop;
    requires org.apache.pdfbox;

    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    
    exports com.example.controller;
    exports com.example.model;
    exports com.example.service;
    exports com.example;
}