module com.cyber.cybernexuspacer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.cyber.cybernexuspacer to javafx.fxml;
    exports com.cyber.cybernexuspacer;
}