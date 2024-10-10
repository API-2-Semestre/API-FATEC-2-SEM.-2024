package com.cyber.cybernexuspacer.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

public class AcompanharSprintsController {

    @FXML
    private ImageView btn_print_nota_grupo1;

    @FXML
    private ImageView btn_print_nota_grupo2;

    @FXML
    private ImageView btn_print_nota_grupo3;

    @FXML
    private ImageView btn_print_nota_grupo4;

    @FXML
    private ImageView btn_print_nota_individual1;

    @FXML
    private ImageView btn_print_nota_individual2;

    @FXML
    private ImageView btn_print_nota_individual3;

    @FXML
    private ImageView btn_print_nota_individual4;

    @FXML
    private Button btnconfirmar;

    @FXML
    private Button btnsair;

    @FXML
    private Button btnsprint1;

    @FXML
    private Button btnsprint2;

    @FXML
    private Button btnsprint3;

    @FXML
    private Button btnsprint4;

    @FXML
    private ImageView logofatec;

    @FXML
    private Text nota_grupo1;

    @FXML
    private Text nota_grupo2;

    @FXML
    private Text nota_grupo3;

    @FXML
    private Text nota_grupo4;

    @FXML
    private Text nota_individual1;

    @FXML
    private Text nota_individual2;

    @FXML
    private Text nota_individual3;

    @FXML
    private Text nota_individual4;

    @FXML
    private Label txt_acompanhamento_pacer;

    @FXML
    private Text txt_nota_grupo1;

    @FXML
    private Text txt_nota_grupo2;

    @FXML
    private Text txt_nota_grupo3;

    @FXML
    private Text txt_nota_grupo4;

    @FXML
    private Text txt_nota_individual1;

    @FXML
    private Text txt_nota_individual2;

    @FXML
    private Text txt_nota_individual3;

    @FXML
    private Text txt_nota_individual4;

    @FXML
    private Label txt_ola_professor;

    @FXML
    private Text txt_selecaodesprints;

    @FXML
    private Text txtaluno1;

    @FXML
    private Text txtaluno2;

    @FXML
    private Text txtaluno3;

    @FXML
    private Text txtaluno4;

    @FXML
    private Label txtcibernexus;

    @FXML
    private Text txtgrupo1;

    @FXML
    private Text txtgrupo2;

    @FXML
    private Text txtgrupo3;

    @FXML
    private Text txtgrupo4;

    @FXML
    protected void onbtnconfirmar(ActionEvent event) {

    }

    @FXML
    protected void onbtnsair(ActionEvent event) throws IOException {
        Main.setRoot("login-view");
    }

    @FXML
    protected void onbtnsprint1(ActionEvent event) {

    }

    @FXML
    protected void onbtnsprint2(ActionEvent event) {

    }

    @FXML
    protected void onbtnsprint3(ActionEvent event) {

    }

    @FXML
    protected void onbtnsprint4(ActionEvent event) {

    }

}
