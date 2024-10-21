package com.cyber.cybernexuspacer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class CriteriosController {

    ArrayList<Pane> PanesSprint;  // Agora com o tipo Pane

    @FXML
    private Button BtnDeleteSprint;

    @FXML
    private DatePicker DataFim;

    @FXML
    private DatePicker DataFim1;

    @FXML
    private DatePicker DataInicio;

    @FXML
    private DatePicker DataInicio1;

    @FXML
    private Label LblTxtFim;

    @FXML
    private Label LblTxtFim1;

    @FXML
    private Label LblTxtInicio;

    @FXML
    private Label LblTxtInicio1;

    @FXML
    private Label LblTxtSprintUm;

    @FXML
    private Label LblTxtSprintUm1;

    @FXML
    private Pane PaneSprintUm;

    @FXML
    private Pane PaneSprintZero;

    @FXML
    private Button adicionarSprintButton;

    @FXML
    private Button btnAcompSprint;

    @FXML
    private Button btnSairCriterios;

    @FXML
    private VBox criteriaContainer;  // Onde os novos critérios serão adicionados

    @FXML
    private VBox criteriaContainer1;

    @FXML
    private TextField descricaoCriterio;

    @FXML
    private VBox sprintContainer;

    @FXML
    private VBox sprintVBox;

    @FXML
    private TextField tituloCriterio;

    int sprintCount;

    public CriteriosController() {
        sprintCount = 1;
        PanesSprint = new ArrayList<>();
        PanesSprint.add(PaneSprintZero); // Adicionando até o 10
        PanesSprint.add(PaneSprintUm);
        // Se necessário, adicionar mais panes (PaneSprintDois, etc.)
    }

    @FXML
    public void initialize() {
        // Torna o PaneSprintUm invisível ao inicializar
        PaneSprintUm.setVisible(false);
    }

    // Este método será chamado quando o botão "Adicionar Sprint" for clicado
    @FXML
    private void handleAdicionarSprint() {
        // Torna o PaneSprintUm visível ao adicionar um sprint
        PaneSprintUm.setVisible(true);
    }

    @FXML
    void botaoDeleteSprint02(ActionEvent event) {
        PaneSprintUm.setVisible(false);
    }

    @FXML
    void OnClickBotaoTeste(ActionEvent event) {
        // Ação que será executada quando o botão "botaoTeste" for clicado
    }

    // Método para adicionar um novo critério à interface
    @FXML
    private void handleAdicionarCriterio() {
        // Captura o título e a descrição dos TextFields
        String titulo = tituloCriterio.getText();
        String descricao = descricaoCriterio.getText();

        // Verifica se o título ou a descrição estão vazios
        if (titulo.isEmpty() || descricao.isEmpty()) {
            // Exibe uma mensagem de erro ou faça alguma ação
            System.out.println("Preencha todos os campos antes de adicionar um critério.");
            return;
        }

        // Cria um novo Pane
        Pane novoCriterioPane = new Pane();
        novoCriterioPane.setStyle("-fx-border-color: black; -fx-padding: 10;");  // Adiciona um estilo básico

        // Cria Labels para mostrar o título e a descrição
        Label tituloLabel = new Label("Título: " + titulo);
        Label descricaoLabel = new Label("Descrição: " + descricao);

        // Define a posição dos Labels no Pane
        tituloLabel.setLayoutX(10);
        tituloLabel.setLayoutY(10);

        descricaoLabel.setLayoutX(10);
        descricaoLabel.setLayoutY(30);

        // Adiciona os Labels ao Pane
        novoCriterioPane.getChildren().addAll(tituloLabel, descricaoLabel);

        // Define o tamanho do Pane
        novoCriterioPane.setPrefSize(200, 60);  // Ajuste conforme o necessário

        // Adiciona o Pane ao container (ex: VBox criteriaContainer)
        criteriaContainer.getChildren().add(novoCriterioPane);

        // Limpa os campos de entrada após adicionar o critério
        tituloCriterio.clear();
        descricaoCriterio.clear();
    }

    @FXML
    public void handleSair(ActionEvent actionEvent) {
        // Lógica para sair da tela
    }

    @FXML
    void handleAcompSprint(ActionEvent event) throws IOException {
        Main.setRoot("acompanharSprints-view"); // Muda para a tela de acompanhamento de sprints
    }
}
