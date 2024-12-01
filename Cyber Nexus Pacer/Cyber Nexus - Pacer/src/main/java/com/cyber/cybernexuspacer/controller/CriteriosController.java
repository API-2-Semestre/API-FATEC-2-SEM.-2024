package com.cyber.cybernexuspacer.controller;

import com.cyber.cybernexuspacer.dao.CriterioDao;
import com.cyber.cybernexuspacer.dao.SprintDao;
import com.cyber.cybernexuspacer.entity.Criterio;
import com.cyber.cybernexuspacer.entity.Sprint;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    //private AnchorPane campo_sprints;
    private VBox campo_sprints;

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
        descricaoLabel.setPrefWidth(380);  // Defina uma largura preferencial

        Button excluirButton = new Button("Excluir");
        excluirButton.setLayoutX(372);
        excluirButton.setLayoutY(8);
        excluirButton.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-background-color: #FF0000;");
        excluirButton.setOnAction(e -> handleExcluirCriterio(titulo, novoCriterio));


        novoCriterio.getChildren().addAll(tituloLabel, descricaoLabel, excluirButton);

        // Verifica quantos critérios já existem no campo_criterios para setar posição de altura correto
        int numeroDeCriterios = campo_criterios.getChildren().size();
        double novaPosicaoY = numeroDeCriterios == 0 ? 5 : numeroDeCriterios * 78;
        novoCriterio.setLayoutY(novaPosicaoY);


        // Adiciona o novo critério ao AnchorPane (campo_criterios)
        campo_criterios.getChildren().add(novoCriterio);
        campo_criterios.setPrefHeight(novaPosicaoY);
    }

    private void carregarCriterios() throws SQLException {
        CriterioDao criterioDao = new CriterioDao();
        List<Criterio> criteriosBd = criterioDao.listarCriterios();

        for (Criterio criterio : criteriosBd) {
            exibirCriterio(criterio.getTitulo(), criterio.getDescricao());
        }
    }

    private void handleExcluirCriterio(String titulo, Pane criterioPane) {
        CriterioDao criterioDao = new CriterioDao();
        SprintDao sprintDao = new SprintDao(); // Para buscar a data final da primeira sprint

        try {
            // Obter a data final da primeira sprint
            Date dataFinalPrimeiraSprint = (Date) sprintDao.buscarDataFinalPrimeiraSprint();

            if (dataFinalPrimeiraSprint != null && new Date(System.currentTimeMillis()).after(dataFinalPrimeiraSprint)) {
                // Caso a data atual seja posterior à data final da primeira sprint, exibe mensagem e interrompe
                exibirMensagem("Não é possível excluir o critério após a data final da primeira sprint.");
                return;
            }

            // Excluir o critério
            criterioDao.deletarCriterio(titulo);
            campo_criterios.getChildren().remove(criterioPane);
            exibirMensagem("Critério excluído com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            exibirMensagem("Erro ao excluir o critério.");
        }
    }





//***ADICIONAR SPRINTS*****//


    @FXML
    private void handleAdicionarSprint() {
        sprintCount++;

        DatePicker dataInicioPicker = new DatePicker();
        DatePicker dataFimPicker = new DatePicker();

        // Inicializando o novo Sprint com um ID temporário (pode ser 0)
        Sprint novoSprint = new Sprint(0, sprintCount, null, null);

        // Adiciona o novo sprint à lista
        sprints.add(novoSprint);
        //exibirSprint(novoSprint, dataInicioPicker, dataFimPicker);

        // Cria um novo HBox para armazenar os componentes da sprint
        HBox novoSprintBox = new HBox(10); // Espaçamento entre os componentes
        Label labelSprint = new Label("Nova Sprint");
        novoSprintBox.getChildren().addAll(labelSprint, dataInicioPicker, dataFimPicker);

        // Adiciona o novo HBox ao VBox que contém as sprints
        //campo_sprints.getChildren().add(novoSprintBox);
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

        Label numSprintLabel = criaLabel(25,13, "Sprint" + sprint.getNumSprint(), "-fx-text-fill: white; -fx-font-size: 14px;");
        Label dataInicioLabel = criaLabel(12,50,"Data de Início:", "-fx-text-fill: white; -fx-font-size: 14px;");
        Label dataFimLabel = criaLabel(12,85,"Data de Fim:", "-fx-text-fill: white; -fx-font-size: 14px;");


        Button btnDeleteSprint = new Button("X");
        btnDeleteSprint.setLayoutX(160);
        btnDeleteSprint.setLayoutY(14);
        btnDeleteSprint.setOnAction(e -> {
            // Mensagem de confirmação
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Deleção");
            alert.setHeaderText("Você realmente deseja excluir esta Sprint?");
            alert.setContentText("Esta ação não pode ser desfeita.");

            // Adiciona os botões de confirmação
            ButtonType buttonTypeYes = new ButtonType("Sim");
            ButtonType buttonTypeNo = new ButtonType("Não");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // Exibe o diálogo e aguarda a resposta
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                sprint.setMarkedForDeletion(); // Marca para deleção

                try {
                    handleDeleteSprint(sprint.getId());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
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

    private void carregarSprints() throws SQLException {
        if (!sprints.isEmpty()) {
            return; // Se já existirem sprints, não recarregue do banco
        }

        SprintDao sprintDao = new SprintDao();
        List<Sprint> sprintsBd = sprintDao.listarSprints();

        // Limpa o painel onde as Sprints são exibidas
        campo_sprints.getChildren().clear();

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

        for (int i = 0; i < sprints.size(); i++) {
            Sprint sprint = sprints.get(i);

            if (sprint.getDataInicio() == null || sprint.getDataFim() == null) {
                exibirMensagem("Datas não podem ser vazias para a Sprint: " + sprint.getNumSprint());
                return; // Saia do metodo se houver datas vazias
            }

            // Verifique se a sprint já existe no banco antes de tentar salvar
            // Caso a sprint já tenha um ID, significa que ela foi salva antes
            if (sprint.getId() == 0) { // Se o ID for 0, é uma nova sprint
                int id = sprintDao.salvarSprint(sprint);
                sprint.setId(id); // Atualize o objeto Sprint com o ID gerado
                sprint.setNumSprint(i + 1);
            }
        }

        //adicionar crierios
        CriterioDao criterioDao = new CriterioDao();
        for (Criterio criterio : criterios) {
            criterioDao.inserirCriterio(criterio);
        }

        carregarSprints(); // Atualiza a interface se necessário
        exibirMensagem("Informações salvas com sucesso!");
    }


    //*** DELETAR OS DADOS ****//


    private void handleDeleteSprint(int index) throws SQLException {
        SprintDao sprintDao = new SprintDao();

        // Itera sobre as sprints e remove as marcadas para deleção
        for (int i = sprints.size() - 1; i >= 0; i--) {
            Sprint sprint = sprints.get(i);
            if (sprint.isMarkedForDeletion()) {
                sprints.remove(i); // Remove da lista
            }
        }
        sprintDao.deletarSprint(index); // Exclui do banco de dados
        carregarSprints(); // Atualiza a interface após deleção
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

