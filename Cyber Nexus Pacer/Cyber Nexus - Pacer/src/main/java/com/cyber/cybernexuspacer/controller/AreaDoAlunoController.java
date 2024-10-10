package com.cyber.cybernexuspacer.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class AreaDoAlunoController {
    @FXML
    private Label welcomeText;

    @FXML
    private Pane alunoSprintsBox;

    @FXML
    private Pane alunosSprintsBtns;

    @FXML
    private AnchorPane areaAlunoContainer;

    @FXML
    private Pane areaAlunoTitulo;

    @FXML
    private Button btnConfirmaCriterios;

    @FXML
    private Button btnSairNotas;

    @FXML
    private Button btnSprint1;

    @FXML
    private Button btnSprint111;

    @FXML
    private Button btnSprint2;

    @FXML
    private Button btnSprint3;

    @FXML
    private Button btnSprint4;

    @FXML
    private Pane cabecalhoCyber;

    @FXML
    private CheckBox checkBoxDois;

    @FXML
    private CheckBox checkBoxDois1;

    @FXML
    private CheckBox checkBoxDois11;

    @FXML
    private CheckBox checkBoxDois111;

    @FXML
    private CheckBox checkBoxQuatro;

    @FXML
    private CheckBox checkBoxQuatro1;

    @FXML
    private CheckBox checkBoxQuatro11;

    @FXML
    private CheckBox checkBoxQuatro111;

    @FXML
    private CheckBox checkBoxTres;

    @FXML
    private CheckBox checkBoxTres1;

    @FXML
    private CheckBox checkBoxTres11;

    @FXML
    private CheckBox checkBoxTres111;

    @FXML
    private CheckBox checkBoxUm;

    @FXML
    private CheckBox checkBoxUm1;

    @FXML
    private CheckBox checkBoxUm11;

    @FXML
    private CheckBox checkBoxUm111;

    @FXML
    private ScrollPane criteriosNotas;

    @FXML
    private Pane grupoAlunoInfo;

    @FXML
    private Label lblTxtDescricaoDocriterio1;

    @FXML
    private Label lblTxtDescricaoDocriterio11;

    @FXML
    private Label lblTxtDescricaoDocriterio111;

    @FXML
    private Label lblTxtDescricaoDocriterio1111;

    @FXML
    private Label lblTxtNumberFour;

    @FXML
    private Label lblTxtNumberFour1;

    @FXML
    private Label lblTxtNumberFour11;

    @FXML
    private Label lblTxtNumberFour111;

    @FXML
    private Label lblTxtNumberOne;

    @FXML
    private Label lblTxtNumberOne1;

    @FXML
    private Label lblTxtNumberOne11;

    @FXML
    private Label lblTxtNumberOne111;

    @FXML
    private Label lblTxtNumberTree;

    @FXML
    private Label lblTxtNumberTree1;

    @FXML
    private Label lblTxtNumberTree11;

    @FXML
    private Label lblTxtNumberTree111;

    @FXML
    private Label lblTxtNumberTwo;

    @FXML
    private Label lblTxtNumberTwo1;

    @FXML
    private Label lblTxtNumberTwo11;

    @FXML
    private Label lblTxtNumberTwo111;

    @FXML
    private Label lblTxtTitulodocriterio1;

    @FXML
    private Label lblTxtTitulodocriterio11;

    @FXML
    private Label lblTxtTitulodocriterio111;

    @FXML
    private Label lblTxtTitulodocriterio1111;

    @FXML
    private Pane paneSprint1;

    @FXML
    private Pane paneSprint11;

    @FXML
    private Pane paneSprint111;

    @FXML
    private Pane paneSprint1111;

    @FXML
    void onClickbtnSair(ActionEvent event) throws IOException {
        Main.setRoot("login-view");
    }
}
