module SEProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens SEProject to javafx.fxml;
    exports SEProject;
}