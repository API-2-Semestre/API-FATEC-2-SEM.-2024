package com.cyber.cybernexuspacer.controller;

import com.cyber.cybernexuspacer.dao.AreaDoAlunoDao;
import com.cyber.cybernexuspacer.dao.CriterioDao;
import com.cyber.cybernexuspacer.dao.SprintDao;
import com.cyber.cybernexuspacer.entity.AreaDoAluno;
import com.cyber.cybernexuspacer.entity.Criterio;
import com.cyber.cybernexuspacer.entity.Sprint;
import com.cyber.cybernexuspacer.session.AlunoSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaDoAlunoController {

    @FXML
    private Label lblNomeGrupo;

    @FXML
    private Text lblNotaGrupo;

    @FXML
    private AnchorPane campo_criterios;

    @FXML
    private VBox vBoxSprintsBtns;

    @FXML
    private ComboBox<String> alunosComboBox;  // Referência para o ComboBox de alunos

    private int sprintSelecionada = 0;

    private Map<String, Integer> alunosMap = new HashMap<>();  // Mapeamento nome -> ID do aluno

    private List<CheckBox> checkboxes = new ArrayList<>();

    // Estrutura para armazenar os critérios e suas notas associadas
    private Map<String, Integer> criteriosSelecionados = new HashMap<>();


    @FXML
    public void initialize() throws SQLException {
        carregarCriterios();
        carregarAlunos();
        carregarSprints();
        exibirNota();
    }


    @FXML
    private void onClickbtnConfirmaNota(ActionEvent event) throws SQLException {


        // Obtenha o ID do avaliador e do avaliado, assim como o número do sprint
        String nomeReceptor = alunosComboBox.getValue();  // Nome do aluno selecionado no ComboBox
        Integer idReceptor = alunosMap.get(nomeReceptor); // O ID correspondente ao nome do aluno selecionado

        AreaDoAluno alunoLogado = AlunoSession.getAlunoLogado();
        int idAvaliador = alunoLogado.getIdAlunoAvaliador(); // Agora pegamos o ID do aluno logado

        // Verifique se a sprint foi selecionada
        if (sprintSelecionada == 0) {
            // Se a sprintSelecionada for 0, significa que a sprint não foi selecionada
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecione uma sprint antes de salvar as notas.");
            alert.showAndWait();
            return;  // Interrompe a execução do método caso a sprint não tenha sido selecionada
        }

        // Consultar a nota disponível para o grupo
        AreaDoAlunoDao areaDoAlunoDao = new AreaDoAlunoDao();
        double notaDisponivel = areaDoAlunoDao.buscaNota(sprintSelecionada);  // Pega a nota disponível do banco

        // Calcular a soma das notas atribuídas
        int somaNotas = criteriosSelecionados.values().stream().mapToInt(Integer::intValue).sum();

        // Verificar se a soma das notas é válida
        if (somaNotas > notaDisponivel) {
            // Se a soma das notas for maior que a nota disponível
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("A soma das notas atribuídas é maior do que as notas disponíveis para distribuir.");
            alert.showAndWait();
            return;  // Não continua com a confirmação
        }


        // Para cada critério, verifica qual foi a nota atribuída
        for (Map.Entry<String, Integer> entry : criteriosSelecionados.entrySet()) {
            String tituloCriterio = entry.getKey();
            int nota = entry.getValue();

            // Agora você pode salvar a nota no banco de dados chamando o método no DAO
            //AreaDoAlunoDao areaDoAlunoDao = new AreaDoAlunoDao();
            areaDoAlunoDao.salvarNota(idAvaliador, idReceptor, nota, tituloCriterio, sprintSelecionada); // Salva a nota na tabela NOTAS

        }

        exibirNota();
        // Exibe uma mensagem para o usuário
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // Definindo o título do alerta
        alert.setTitle("Nota Salva");
        // Definindo o cabeçalho (pode ser nulo ou você pode colocar uma breve descrição)
        alert.setHeaderText(null);
        alert.setContentText("Nota salva com sucesso!");
        alert.showAndWait();
    }


    // Método para salvar a nota selecionada para um critério específico
    private void salvarNotaCriterio(String tituloCriterio, int nota) {
        // Armazenando a nota associada ao critério
        criteriosSelecionados.put(tituloCriterio, nota);
    }

    private void carregarSprints() throws SQLException {
        SprintDao sprintDao = new SprintDao();
        List<Sprint> sprints = sprintDao.listarSprints();  // Busca todas as sprints no banco
        vBoxSprintsBtns.getChildren().clear();  // Limpa o conteúdo anterior


        // Cria um botão para cada sprint
        for (Sprint sprint : sprints) {
            if (sprint.getLiberado() == 1) {
                Button sprintButton = new Button("Sprint " + sprint.getNumSprint());
                sprintButton.setPrefSize(188, 40);
                sprintButton.setLayoutX(6);
                sprintButton.setLayoutY(7);
                sprintButton.setStyle("-fx-background-color: #86B6DD; -fx-font-weight: bolder; -fx-padding: 10 20 10 20;");
                sprintButton.setOnAction(event -> {
                    // Ação quando a sprint for clicada
                    sprintSelecionada = sprint.getNumSprint();
                    System.out.println("Sprint " + sprint.getNumSprint() + " selecionada.");
                    // Você pode adicionar a lógica para lidar com o clique na sprint
                });
                vBoxSprintsBtns.getChildren().add(sprintButton);  // Adiciona o botão à interface
            }
        }
    }


    private void carregarAlunos() throws SQLException {
        AreaDoAlunoDao areaDoAlunoDao = new AreaDoAlunoDao();
        List<AreaDoAluno> alunos = areaDoAlunoDao.listarAlunos();

        // Preenche o ComboBox com os nomes dos alunos
        alunosComboBox.getItems().setAll(
                alunos.stream()
                        .map(AreaDoAluno::getNomeAluno)  // Nome do aluno
                        .toList()
        );

        // Armazenar o ID de cada aluno no map
        for (AreaDoAluno aluno : alunos) {
            alunosMap.put(aluno.getNomeAluno(), aluno.getIdAlunoReceptor());  // Pegando id do receptor da nota para salvar no banco
        }

        // Adiciona um listener para capturar a seleção
        alunosComboBox.setOnAction(event -> {
            String alunoSelecionado = alunosComboBox.getValue();
            if (alunoSelecionado != null) {
                Integer idAlunoReceptor = alunosMap.get(alunoSelecionado);
                System.out.println("ID do aluno selecionado: " + idAlunoReceptor);
                // Agora você pode salvar o idAluno em uma variável ou usá-lo para outras operações
            }
        });
    }

    public void exibirNota() {
        try {
            AreaDoAlunoDao areaDoAlunoDao = new AreaDoAlunoDao();
            double nota = 0;
            try {
                nota = areaDoAlunoDao.buscaNota(sprintSelecionada);
                // Exibe a nota no UI
            } catch (RuntimeException e) {
                // Aqui você pode capturar a RuntimeException ou a SQLException
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Nota não encontrada");
                alert.setHeaderText("Nota não encontrada para o grupo do aluno.");
                alert.setContentText("Verifique os dados e tente novamente mais tarde.");
                alert.showAndWait();
            }
            List<AreaDoAluno> alunos = areaDoAlunoDao.listarAlunos();


            // Armazenar o ID de cada aluno no map
            for (AreaDoAluno aluno : alunos) {
                lblNomeGrupo.setText(aluno.getGrupo());
            }
            // Define o texto da Label com o valor da nota
            lblNotaGrupo.setText(String.valueOf(nota)); // Formata para 2 casas decimais


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exibirCriterio(String titulo, String descricao) {
        // Criação do campo de exibição do critério
        Pane paneCriterio = new Pane();
        paneCriterio.setPrefSize(476, 75);
        paneCriterio.setLayoutX(4);
        //paneCriterio.setLayoutY(10);
        paneCriterio.setStyle("-fx-background-color: #86B6DD; -fx-background-radius: 4;");

        // Adicionando o título e a descrição no critério
        Label tituloLabel = new Label( titulo);
        tituloLabel.setLayoutX(7);
        tituloLabel.setLayoutY(7);
        tituloLabel.setStyle("-fx-text-fill: black; -fx-font-size: 14px; -fx-font-family: 'Arial'; ");

        Label descricaoLabel = new Label(descricao);
        descricaoLabel.setPrefWidth(300);
        descricaoLabel.setLayoutX(7);
        descricaoLabel.setLayoutY(34);
        descricaoLabel.setWrapText(true);  // Permite quebra de linha
        descricaoLabel.setStyle("-fx-text-fill: black; -fx-font-size: 12px; -fx-font-family: 'Arial';");

        // Criando o ToggleGroup
        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton radioZero = criaRadioButton(350,10);
        radioZero.setToggleGroup(toggleGroup);

        RadioButton radioUm = criaRadioButton(380,10);
        radioUm.setToggleGroup(toggleGroup);

        RadioButton radioDois = criaRadioButton(410,10);
        radioDois.setToggleGroup(toggleGroup);

        RadioButton radioTres = criaRadioButton(440,10);
        radioTres.setToggleGroup(toggleGroup);

        // Associando as ações aos RadioButtons
        radioZero.setOnAction(e -> salvarNotaCriterio(titulo, 0));
        radioUm.setOnAction(e -> salvarNotaCriterio(titulo, 1));
        radioDois.setOnAction(e -> salvarNotaCriterio(titulo, 2));
        radioTres.setOnAction(e -> salvarNotaCriterio(titulo, 3));

        Label lblNotaZero = criaLabel(355,33,"0");
        Label lblNotaUm = criaLabel(385,33,"1");
        Label lblNotaDois = criaLabel(415,33,"2");
        Label lblNotaTres = criaLabel(445,33,"3");

        paneCriterio.getChildren().addAll(
                tituloLabel, descricaoLabel,
                radioZero,radioUm,radioDois,radioTres,
                lblNotaZero ,lblNotaUm, lblNotaDois, lblNotaTres

        );

        // Verifica quantos critérios já existem no campo_criterios
        int numeroDeCriterios = campo_criterios.getChildren().size();

        // Calcula a posição Y para o novo critério
        double novaPosicaoY = numeroDeCriterios == 0 ? 5 : numeroDeCriterios * 85;

        // Define a posição do novo critério
        paneCriterio.setLayoutY(novaPosicaoY);

        // Adiciona o novo critério ao AnchorPane (campo_criterios)
        campo_criterios.getChildren().add(paneCriterio);
        campo_criterios.setPrefHeight(novaPosicaoY);
    }

    private void carregarCriterios() throws SQLException {
        CriterioDao criterioDao = new CriterioDao();
        List<Criterio> criterios = criterioDao.listarCriterios();

        for (Criterio criterio : criterios) {
            exibirCriterio(criterio.getTitulo(), criterio.getDescricao());
        }
    }


    private RadioButton criaRadioButton(double layoutX, double layoutY) {
        RadioButton radioButton = new RadioButton();
        radioButton.setLayoutX(layoutX);
        radioButton.setLayoutY(layoutY);
        return radioButton;
    }

    private Label criaLabel(double layoutX, double layoutY, String descricao) {
        Label label = new Label(descricao);
        label.setLayoutX(layoutX);
        label.setLayoutY(layoutY);
        label.setStyle("-fx-text-fill: black; -fx-font-size: 15px; -fx-font-family: 'Arial';");
        return label;
    }

    @FXML
    void onClickbtnSair(ActionEvent event) throws IOException {
        Main.setRoot("login-view");
    }


}
