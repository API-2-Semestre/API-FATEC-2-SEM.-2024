package com.cyber.cybernexuspacer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label lblBemVindo;


    @FXML
    protected void onHelloButtonClick() throws IOException {
        HelloApplication.setRoot("criterios.fxml");
    }
}