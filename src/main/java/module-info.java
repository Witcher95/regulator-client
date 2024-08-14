module com.example.technosisclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.example.regulatorclient;
    opens com.example.regulatorclient.dto;
}