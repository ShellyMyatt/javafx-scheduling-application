module myatt.schedulingapplicationc195 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


//    opens myatt to javafx.fxml;
//    exports myatt;
    exports myatt.dao;
    opens myatt.dao to javafx.fxml;
    exports myatt.helper;
    opens myatt.helper to javafx.fxml;
//    exports myatt.main;
//    opens myatt.main to javafx.fxml;
    exports myatt.model;
    opens myatt.model to javafx.fxml;
    exports myatt.controller;
    opens myatt.controller to javafx.fxml;
    exports myatt;
    opens myatt to javafx.fxml;
}