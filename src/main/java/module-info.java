module scheduler.schedulingapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


//    opens scheduler to javafx.fxml;
//    exports scheduler;
    exports scheduler.dao;
    opens scheduler.dao to javafx.fxml;
    exports scheduler.helper;
    opens scheduler.helper to javafx.fxml;
//    exports scheduler.main;
//    opens scheduler.main to javafx.fxml;
    exports scheduler.model;
    opens scheduler.model to javafx.fxml;
    exports scheduler.controller;
    opens scheduler.controller to javafx.fxml;
    exports scheduler;
    opens scheduler to javafx.fxml;
}