module com.example.appmusic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.appmusic to javafx.fxml;
    exports com.example.appmusic;
    exports com.example.appmusic.controllers;
    opens com.example.appmusic.controllers to javafx.fxml;
}