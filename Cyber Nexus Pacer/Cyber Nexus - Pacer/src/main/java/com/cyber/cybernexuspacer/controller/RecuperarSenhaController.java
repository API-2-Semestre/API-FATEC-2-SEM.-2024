package com.cyber.cybernexuspacer.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class RecuperarSenhaController {
    @FXML
    private AnchorPane anchorFundo;

    @FXML
    private Button btnClickEnviar;

    @FXML
    private ImageView imgFatec;

    @FXML
    private Label lblTxtFatec;

    @FXML
    private Label lblTxtSistemaPacer;

    @FXML
    private PasswordField pswdFildSenha;

    @FXML
    private PasswordField pswdFildSenhaNovamente;

    @FXML
    private Text textInsiraEnail;

    @FXML
    private Text txtDigiteNovamente;

    @FXML
    private Text txtDigiteSenhha;

    @FXML
    void onClickEnviar(ActionEvent event) throws IOException {
        Main.setRoot("AreaDoAluno-view");
    }

}


