module cs211.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens cs211.project.application to javafx.fxml;
    exports cs211.project.application;
    exports cs211.project.controllers;
    opens cs211.project.controllers to javafx.fxml;
    exports cs211.project.models;
    opens cs211.project.models to java.base;
}