package com.cyber.cybernexuspacer.controller;

import com.cyber.cybernexuspacer.dao.PontuacaoGruposDao;
import com.cyber.cybernexuspacer.dao.SprintDao;
import com.cyber.cybernexuspacer.entity.PontuacaoGrupo;
import com.cyber.cybernexuspacer.entity.Sprint;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PontuacaoGruposController {

    @FXML
    private TextField txtDiasLiberados;

    @FXML
    private AnchorPane pontuacao_scroll_sprints; // Container para os botões das Sprints

    @FXML
    private AnchorPane mostrar_Grupos; // Container para exibir os grupos e notas

    private final Map<String, TextField> notasMap = new HashMap<>(); // Para gerenciar os campos de notas

    private Button selectedSprintButton; // Referência ao botão da sprint selecionada

    @FXML
    public void initialize() throws SQLException {
        carregarSprints(); // Carregar Sprints dinamicamente
    }

    private void carregarSprints() throws SQLException {
        SprintDao sprintDao = new SprintDao();
        List<Sprint> sprints = sprintDao.listarSprints();

        double yPosition = 55.0; // Posição inicial para os botões
        for (Sprint sprint : sprints) {
            Button sprintButton = new Button("SPRINT " + sprint.getNumSprint());
            sprintButton.setLayoutX(10.0);
            sprintButton.setLayoutY(yPosition);
            sprintButton.setPrefWidth(120.0);
            sprintButton.setPrefHeight(30.0);
            if (sprint.getLiberado() == 1){
                sprintButton.setStyle("-fx-background-color: #86B6DD; -fx-text-fill: WHITE; -fx-font-size: 12px;");
            }else{
                sprintButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: WHITE; -fx-font-size: 12px;");
            }


            // Evento ao clicar no botão
            sprintButton.setOnAction(event -> {
                try {
                    handleSprintSelection(sprintButton, sprint.getNumSprint());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            pontuacao_scroll_sprints.getChildren().add(sprintButton);
            yPosition += 40.0;
        }
    }

    private void handleSprintSelection(Button clickedButton, int numSprint) throws SQLException {
        // Resetar a cor de todos os botões para a cor original
        for (var node : pontuacao_scroll_sprints.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setStyle("-fx-background-color: #86B6DD; -fx-text-fill: WHITE; -fx-font-size: 12px;");
            }
        }

        // Alterar a cor do botão clicado
        clickedButton.setStyle("-fx-background-color: #369C06; -fx-text-fill: WHITE; -fx-font-size: 12px;");

        // Atualizar o botão selecionado
        selectedSprintButton = clickedButton;

        // Carregar as notas para a sprint selecionada
        carregarNotasPorSprint(numSprint);
    }

    private void carregarNotasPorSprint(int numSprint) throws SQLException {
        PontuacaoGruposDao pontuacaoGruposDao = new PontuacaoGruposDao();
        List<PontuacaoGrupo> grupos = pontuacaoGruposDao.pesquisarGrupos();

        mostrar_Grupos.getChildren().clear();

        double yPosition = 10.0; // Posição inicial dos grupos
        for (PontuacaoGrupo grupo : grupos) {
            // Busca as notas da Sprint
            List<Integer> notas = pontuacaoGruposDao.selecionarNotasSprint(grupo.getGrupo(), numSprint);

            int nota = notas.isEmpty() ? 0 : notas.get(0); // Se não houver nota, exibe 0

            // Exibir o grupo e a nota
            Pane grupoPane = criarGrupoPane(grupo.getGrupo(), grupo.getIntegrantes(), nota, yPosition);
            mostrar_Grupos.getChildren().add(grupoPane);
            yPosition += 70.0; // Incrementar a posição para o próximo grupo
        }
    }

    private Pane criarGrupoPane(String grupo, int integrantes, int nota, double yPosition) {
        Pane grupoPane = new Pane();
        grupoPane.setPrefSize(579, 60);
        grupoPane.setLayoutX(4);
        grupoPane.setLayoutY(yPosition);
        grupoPane.setStyle("-fx-background-color: #93DD86; -fx-background-radius: 4;");

        Label nomeLabel = new Label("Nome: " + grupo);
        nomeLabel.setLayoutX(10);
        nomeLabel.setLayoutY(10);
        nomeLabel.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");

        Label integrantesLabel = new Label("Integrantes da Equipe: " + integrantes);
        integrantesLabel.setLayoutX(10);
        integrantesLabel.setLayoutY(30);
        integrantesLabel.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");

        TextField notaField = new TextField(String.valueOf(nota));
        notaField.setLayoutX(480);
        notaField.setLayoutY(15);
        notaField.setPrefWidth(50);
        notaField.setStyle("-fx-font-size: 12px;");

        notasMap.put(grupo, notaField);

        grupoPane.getChildren().addAll(nomeLabel, integrantesLabel, notaField);
        return grupoPane;
    }


    @FXML
    void onClickBloquearSprint(ActionEvent event) throws SQLException {
        if (selectedSprintButton == null) {
            System.out.println("Nenhuma sprint selecionada!");
            return;
        }

        // Extrair o número da sprint selecionada
        int numSprint = Integer.parseInt(selectedSprintButton.getText().replace("SPRINT ", ""));

        // Chamar o método do DAO para liberar a sprint
        PontuacaoGruposDao.bloquearSprint(numSprint);
        carregarSprints();
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Sprint " + numSprint + " bloqueada com sucesso!");
    }

    @FXML
    void onClickLiberarSprint(ActionEvent event) throws SQLException {
        if (selectedSprintButton == null) {
            System.out.println("Nenhuma sprint selecionada!");
            return;
        }

        // Extrair o número da sprint selecionada
        int numSprint = Integer.parseInt(selectedSprintButton.getText().replace("SPRINT ", ""));

        // Chamar o método do DAO para liberar a sprint
        PontuacaoGruposDao.liberarSprint(numSprint);
        carregarSprints();
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Sprint " + numSprint + " liberada com sucesso!");
    }


    @FXML
    void onbtnconfirmar(ActionEvent event) throws SQLException {
        // Atualizar as notas no banco
        PontuacaoGruposDao pontuacaoGruposDao = new PontuacaoGruposDao();

        if (selectedSprintButton == null) {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Nenhuma sprint selecionada!");
            return;
        }

        // Extrair o número da sprint selecionada
        int numSprint = Integer.parseInt(selectedSprintButton.getText().replace("SPRINT ", ""));

        // 1. Atualizar as notas para os grupos
        for (Map.Entry<String, TextField> entry : notasMap.entrySet()) {
            String grupo = entry.getKey();
            int novaNota = Integer.parseInt(entry.getValue().getText());
            pontuacaoGruposDao.salvarNotaGrupo(grupo, numSprint, novaNota);
        }

        // 2. Atualizar os dias liberados para a sprint
        String diasLiberadosStr = txtDiasLiberados.getText();

        // Validar se o valor inserido é um número válido
        if (diasLiberadosStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Por favor, insira o número de dias liberados.");
            return;
        }

        int diasLiberados;
        try {
            diasLiberados = Integer.parseInt(diasLiberadosStr); // Converte o valor para inteiro
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Erro", "Valor inválido para dias liberados.");
            return;
        }

        // Chamar o método do DAO para atualizar os dias liberados para a sprint selecionada
        SprintDao.atualizarDiasLiberados(numSprint, diasLiberados);

        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Notas salvas no banco com sucesso!");
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Dias liberados atualizados para a Sprint " + numSprint + " com sucesso!");
    }

    // Método auxiliar para mostrar alertas
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null); // Não queremos um cabeçalho
        alert.showAndWait();
    }


    @FXML
    void onbtnsair(ActionEvent event) throws IOException {
        Main.setRoot("TelaMenu-view");
    }
}
