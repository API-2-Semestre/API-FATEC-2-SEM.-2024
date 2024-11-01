package com.cyber.cybernexuspacer.controller;

import com.cyber.cybernexuspacer.dao.CriterioDao;
import com.cyber.cybernexuspacer.dao.SprintDao;
import com.cyber.cybernexuspacer.entity.Criterio;
import com.cyber.cybernexuspacer.entity.Sprint;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CriteriosController {

    @FXML
    private Button BtnDeleteSprint;

    @FXML
    private Button adicionarSprintButton;

    @FXML
    private Button btnAcompSprint;

    @FXML
    private Button btnCriarCriterio;

    @FXML
    private Button btnSairCriterios;

    @FXML
    private AnchorPane campo_criterios;

    @FXML
    private AnchorPane campo_sprints;

    @FXML
    private TextField descricaoCriterio;

    @FXML
    private ScrollPane scrollCriteriosCriados;

    @FXML
    private TextField tituloCriterio;

    private int sprintCount = 0;
    private List<Sprint> sprints = new ArrayList<>();
    private List<Criterio> criterios = new ArrayList<>();

    @FXML
    public void initialize() throws SQLException {
        carregarCriterios();
        carregarSprints();
    }

    // *** MÉTODOS DE EXIBIÇÃO DE MENSAGENS ***
    private void exibirMensagem(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensagem);
        alert.showAndWait();
    }

    // *** MÉTODOS PARA CRITÉRIOS ***
    @FXML
    private void handleAdicionarCriterio() {
        String titulo = tituloCriterio.getText();
        String descricao = descricaoCriterio.getText();

        if (titulo.isEmpty() || descricao.isEmpty()) {
            exibirMensagem("Os campos de título e descrição não podem estar vazios.");
            return;
        }

        criterios.add(new Criterio(titulo, descricao));

        //exibindo no historico
        exibirCriterio(titulo, descricao);

        //limpando campos de entrada
        tituloCriterio.clear();
        descricaoCriterio.clear();
    }

    private void exibirCriterio(String titulo, String descricao) {
        // Criação do campo de exibição do critério
        Pane novoCriterio = new Pane();
        novoCriterio.setPrefSize(445, 70);
        novoCriterio.setLayoutX(4);
        novoCriterio.setLayoutY(10);
        novoCriterio.setStyle("-fx-background-color: #86B6DD; -fx-background-radius: 4;");


        // Adicionando o título e a descrição no critério
        Label tituloLabel = criaLabel(10,10,"Título:   " + titulo, "-fx-text-fill: white; -fx-font-size: 14px;");

        Label descricaoLabel = criaLabel(10,30,"Descrição: " + descricao, "-fx-text-fill: white; -fx-font-size: 12px;");
        descricaoLabel.setWrapText(true);  // Permite quebra de linha
        descricaoLabel.setPrefWidth(400);  // Defina uma largura preferencial


        novoCriterio.getChildren().addAll(tituloLabel, descricaoLabel);

        // Verifica quantos critérios já existem no campo_criterios
        int numeroDeCriterios = campo_criterios.getChildren().size();

        // Calcula a posição Y para o novo critério
        double novaPosicaoY = numeroDeCriterios == 0 ? 5 : numeroDeCriterios * 78;

        // Define a posição do novo critério
        novoCriterio.setLayoutY(novaPosicaoY);

        // Adiciona o novo critério ao AnchorPane (campo_criterios)
        campo_criterios.getChildren().add(novoCriterio);
    }

    private void carregarCriterios() throws SQLException {
        CriterioDao criterioDao = new CriterioDao();
        List<Criterio> criteriosBd = criterioDao.listarCriterios();

        for (Criterio criterio : criteriosBd) {
            exibirCriterio(criterio.getTitulo(), criterio.getDescricao());
        }
    }


//***ADICIONAR SPRINTS*****//


    @FXML
    private void handleAdicionarSprint() {
        sprintCount++;

        DatePicker dataInicioPicker = new DatePicker();
        DatePicker dataFimPicker = new DatePicker();

        Sprint novoSprint = new Sprint("Sprint " + sprintCount, null, null);

        // Adiciona o novo sprint à lista
        sprints.add(novoSprint);
        exibirSprint(novoSprint, dataInicioPicker, dataFimPicker);

        // Adicione listeners para os DatePickers
        dataInicioPicker.setOnAction(e -> {
            if (dataInicioPicker.getValue() != null) {
                novoSprint.setDataInicio(Date.valueOf(dataInicioPicker.getValue())); // Converta para java.sql.Date
            }
        });

        dataFimPicker.setOnAction(e -> {
            if (dataFimPicker.getValue() != null) {
                novoSprint.setDataFim(Date.valueOf(dataFimPicker.getValue())); // Converta para java.sql.Date
            }
        });

    }

    private void exibirSprint(Sprint sprint, DatePicker dataInicioPicker, DatePicker dataFimPicker) {

        Pane pane = criarPaneSprint(sprint, dataInicioPicker, dataFimPicker);
        campo_sprints.getChildren().add(pane);
    }

    private Pane criarPaneSprint(Sprint sprint, DatePicker dataInicioPicker, DatePicker dataFimPicker) {
        Pane pane = new Pane();
        pane.setPrefSize(220, 120);
        pane.setLayoutX(6);
        pane.setLayoutY(7);
        pane.setStyle("-fx-background-color: #86B6DD; -fx-background-radius: 5;");

        Label numSprintLabel = criaLabel(25,13,sprint.getNumSprint(), "-fx-text-fill: white; -fx-font-size: 14px;");
        Label dataInicioLabel = criaLabel(12,50,"Data de Início:", "-fx-text-fill: white; -fx-font-size: 14px;");
        Label dataFimLabel = criaLabel(12,85,"Data de Fim:", "-fx-text-fill: white; -fx-font-size: 14px;");


        Button btnDeleteSprint = new Button("X");
        btnDeleteSprint.setLayoutX(160);
        btnDeleteSprint.setLayoutY(14);

        btnDeleteSprint.setOnAction(e -> {
            try {
                handleDeleteSprint(sprints.size() - 1);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });


        dataInicioPicker.setLayoutX(114);
        dataInicioPicker.setLayoutY(48);
        dataInicioPicker.setPrefSize(92, 25);

        dataFimPicker.setLayoutX(114);
        dataFimPicker.setLayoutY(81);
        dataFimPicker.setPrefSize(92, 25);

        // Adicione os elementos ao painel
        pane.getChildren().addAll(numSprintLabel, dataInicioLabel, dataFimLabel, dataInicioPicker, dataFimPicker, btnDeleteSprint);

        int numeroDeCriterios = campo_sprints.getChildren().size();
        double novaPosicaoY = numeroDeCriterios == 0 ? 5 : numeroDeCriterios * 130;
        pane.setLayoutY(novaPosicaoY);


        return pane;
    }


    private void handleDeleteSprint(int index) throws SQLException {
        if (index < 0 || index >= sprints.size()) return;

        sprints.remove(index);
        carregarSprints();
    }

    private void carregarSprints() throws SQLException {
        SprintDao sprintDao = new SprintDao();
        List<Sprint> sprintsBd = sprintDao.listarSprints();

        for (Sprint sprint : sprintsBd) {
            DatePicker dataInicioPicker = new DatePicker();
            DatePicker dataFimPicker = new DatePicker();

            // Defina os valores dos DatePickers com as datas da Sprint
            if (sprint.getDataInicio() != null) {
                dataInicioPicker.setValue(sprint.getDataInicio().toLocalDate()); // Converte java.sql.Date para LocalDate
            }
            if (sprint.getDataFim() != null) {
                dataFimPicker.setValue(sprint.getDataFim().toLocalDate()); // Converte java.sql.Date para LocalDate
            }

            // Exiba a Sprint com os DatePickers
            exibirSprint(sprint, dataInicioPicker, dataFimPicker);
        }
    }

    //*** SALVAR OS DADOS ****//

    @FXML
    void onClickConfirmar(ActionEvent event) throws SQLException {
        SprintDao sprintDao = new SprintDao();

        for (Sprint sprint : sprints) {

            if (sprint.getDataInicio() == null || sprint.getDataFim() == null) {
                exibirMensagem("Datas não podem ser vazias para a Sprint: " + sprint.getNumSprint());
                return; // Saia do método se houver datas vazias
            }

            sprintDao.salvarSprint(sprint);
        }


        //sprints.clear(); // Limpa a lista após salvar, se necessário
        carregarSprints(); // Atualiza a interface se necessário

        //adicionar crierios
        CriterioDao criterioDao = new CriterioDao();
        for (Criterio criterio : criterios) {
            criterioDao.inserirCriterio(criterio);
        }

        exibirMensagem("Informações salvas com sucesso!");
    }

    //*** DELETAR OS DADOS ****//

    @FXML
    void handleDeleteSprint(ActionEvent event) {
        SprintDao sprintDao = new SprintDao();

    }


    //*** OUTROS ****//

    private Label criaLabel(double layoutX, double layoutY, String descricao, String style) {
        Label label = new Label(descricao);
        label.setLayoutX(layoutX);
        label.setLayoutY(layoutY);
        label.setStyle(style);
        return label;
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

