package com.cyber.cybernexuspacer.controller;

import com.cyber.cybernexuspacer.dao.CriterioDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CriteriosController {

    @FXML
    private Button adicionarSprintButton;

    @FXML
    private Button btnCriarCriterio;

    @FXML
    private Button btnSairCriterios;

    @FXML
    private VBox sprintContainer; // VBox onde as Sprints serão adicionadas

    @FXML
    private ScrollPane scrollCriteriosCriados;

    @FXML
    private Pane campoParaCriterio;

    @FXML
    private Pane campoParaCriterio1;

    @FXML
    private Pane campoParaCriterio11;

    @FXML
    private Pane campoParaCriterio12;

    @FXML
    private VBox sprintVBox; // VBox que contém o botão Adicionar Sprint

    @FXML
    private AnchorPane campo_criterios; // ScrollPane para exibir os critérios

    private int sprintCount = 0; // Contador para o número de sprints

    @FXML
    private TextField tituloCriterio; // TextField para o título do critério

    @FXML
    private TextField descricaoCriterio; // TextField para a descrição do critério

    private int criterioIdCounter = 1; // Contador de ID fictício para os critérios (para fins de exemplo)

    @FXML
    private void handleAdicionarSprint() {
        sprintCount++; // Incrementa o contador
    }

    @FXML
    private void handleAdicionarCriterio() {
        String titulo = tituloCriterio.getText();
        String descricao = descricaoCriterio.getText();

        // Caso campo vazio
        if (titulo.isEmpty() || descricao.isEmpty()) {
            System.out.println("Os campos de título e descrição não podem estar vazios.");
            return;
        }

        // Adicionar ao BD e obter um ID fictício para o critério
        CriterioDao criterioDao = new CriterioDao();
        int criterioId = criterioIdCounter++; // Simulação de ID único (substitua pela lógica correta ao usar banco de dados)
        criterioDao.inserirCriterio(titulo, descricao);

        // Criação do campo de exibição do critério
        Pane novoCriterio = new Pane();
        novoCriterio.setPrefSize(445, 50);
        novoCriterio.setStyle("-fx-background-color: #86B6DD; -fx-background-radius: 4;");
        novoCriterio.setLayoutX(4);

        // Adicionando o título e a descrição no critério
        Label tituloLabel = new Label("Título:   " + titulo);
        tituloLabel.setLayoutX(10);
        tituloLabel.setLayoutY(10);
        tituloLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Label descricaoLabel = new Label("Descrição: " + descricao);
        descricaoLabel.setLayoutX(10);
        descricaoLabel.setLayoutY(30);
        descricaoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        // Botão de exclusão
        Button btnExcluir = new Button("Excluir");
        btnExcluir.setLayoutX(370);
        btnExcluir.setLayoutY(10);
        btnExcluir.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white;");
        btnExcluir.setOnAction(event -> handleExcluirCriterio(novoCriterio, criterioId));

        novoCriterio.getChildren().addAll(tituloLabel, descricaoLabel, btnExcluir);

        // Calcula a posição Y para o novo critério
        double novaPosicaoY = campo_criterios.getChildren().size() * 58;
        novoCriterio.setLayoutY(novaPosicaoY);

        // Adiciona o novo critério ao AnchorPane (campo_criterios)
        campo_criterios.getChildren().add(novoCriterio);

        // Limpa os campos de texto para adicionar novos critérios
        tituloCriterio.clear();
        descricaoCriterio.clear();
    }

    @FXML
    private void handleExcluirCriterio(Pane criterioPane, int criterioId) {
        // Remover do BD
        CriterioDao criterioDao = new CriterioDao();
        criterioDao.excluirCriterio(criterioId);

        // Remover da interface
        campo_criterios.getChildren().remove(criterioPane);

        // Reposiciona os critérios restantes
        for (int i = 0; i < campo_criterios.getChildren().size(); i++) {
            Pane pane = (Pane) campo_criterios.getChildren().get(i);
            pane.setLayoutY(i * 58);
        }
    }

    @FXML
    public void handleSair(ActionEvent actionEvent) throws IOException {
        Main.setRoot("TelaMenu-view");
    }

    @FXML
    void handleAcompSprint(ActionEvent event) throws IOException {
        Main.setRoot("acompanharSprints-view");
    }
}
