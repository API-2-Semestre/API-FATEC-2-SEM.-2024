package com.cyber.cybernexuspacer.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CriteriosController {

    @FXML
    private Button adicionarSprintButton;

    @FXML
    private Button btnAcompSprint;

    @FXML
    private Button btnSairCriterios;

    @FXML
    private VBox sprintContainer; // VBox onde as Sprints serão adicionadas

    @FXML
    private VBox sprintVBox; // VBox que contém o botão Adicionar Sprint

    @FXML
    private ScrollPane scrollPane; // ScrollPane para exibir os critérios

    @FXML
    private VBox criteriaContainer; // Container onde os critérios serão adicionados

    private int sprintCount = 0; // Contador para o número de sprints

    @FXML
    private void handleAdicionarSprint() {
        sprintCount++; // Incrementa o contador
    }

    @FXML
    private TextField tituloCritério; // TextField para o título do critério
    @FXML
    private TextField descricaoCritério; // TextField para a descrição do critério

    @FXML
    private void handleAdicionarCriterio() {
        String titulo = tituloCritério.getText();
        String descricao = descricaoCritério.getText();
    }
    @FXML
    public void handleSair(ActionEvent actionEvent) {
    }
    @FXML
    void handleAcompSprint(ActionEvent event) throws IOException {
        Main.setRoot("acompanharSprints-view");
    }
}
