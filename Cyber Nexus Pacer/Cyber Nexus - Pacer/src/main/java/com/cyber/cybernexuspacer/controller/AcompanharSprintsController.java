package com.cyber.cybernexuspacer.controller;

import com.cyber.cybernexuspacer.dao.AcompanharSprintsDao;
import com.cyber.cybernexuspacer.dao.AreaDoAlunoDao;
import com.cyber.cybernexuspacer.dao.CriterioDao;
import com.cyber.cybernexuspacer.dao.SprintDao;
import com.cyber.cybernexuspacer.entity.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.geom.Area;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;


public class AcompanharSprintsController {

    @FXML
    private ScrollPane ScrollPaneSprints;

    @FXML
    private AnchorPane paneAlunos;

    @FXML
    private Button btnGerarAluno;

    @FXML
    private Button btnLimpar;

    @FXML
    private Button btnGerarGrupo;

    @FXML
    private Button btnSair;

    @FXML
    private Button btn_confirmar;

    @FXML
    private ImageView btn_print_nota_grupo11;

    @FXML
    private ComboBox<String> gruposComboBox;

    @FXML
    private ComboBox<String> criteriosComboBox;

    @FXML
    private ImageView logofatec;

    @FXML
    private ImageView logofatec1;

    @FXML
    private Text nota_individual1;

    @FXML
    private Pane paneSprints;

    @FXML
    private VBox vBoxSprintsBtns;

    private int sprintSelecionada = 1;

    private Map<String, String> alteracoesGrupo = new HashMap<>();


    @FXML
    public void initialize() throws SQLException {
        carregarSprints();
        carregarGrupos();
        carregarCriterios();
        carregarAlunos();


    }

    private void carregarSprints() throws SQLException {
        SprintDao sprintDao = new SprintDao();
        List<Sprint> sprints = sprintDao.listarSprints();  // Busca todas as sprints no banco
        vBoxSprintsBtns.getChildren().clear();  // Limpa o conteúdo anterior


        // Cria um botão para cada sprint
        for (Sprint sprint : sprints) {
            Button sprintButton = new Button("Sprint " + sprint.getNumSprint());
            sprintButton.setPrefSize(150, 40);
            //sprintButton.setLayoutX(11);
            sprintButton.setLayoutY(11);
            sprintButton.setStyle("-fx-background-color: #86B6DD; -fx-font-weight: bolder;");
            sprintButton.setOnAction(event -> {
                // Ação quando a sprint for clicada
                sprintSelecionada = sprint.getNumSprint();
                System.out.println("Sprint " + sprintSelecionada + " selecionada.");
                // Você pode adicionar a lógica para lidar com o clique na sprint
            });

            int numSprint = vBoxSprintsBtns.getChildren().size();
            double novaPosicaoY = numSprint == 0 ? 11 : numSprint * 70;  // Incrementa a posição vertical a cada aluno
            sprintButton.setLayoutY(novaPosicaoY);

            vBoxSprintsBtns.getChildren().add(sprintButton);  // Adiciona o botão à interface

        double alturaTotal = sprints.size() * 70;
        vBoxSprintsBtns.setPrefHeight(alturaTotal);

        }
    }

    private void carregarGrupos() throws SQLException {
        AcompanharSprintsDao acompanharSprintsDao = new AcompanharSprintsDao();
        List<PontuacaoGrupo> alunos = acompanharSprintsDao.listarGrupos();

        // Preenche o ComboBox com os nomes dos grupos
        gruposComboBox.getItems().setAll(
                alunos.stream()
                        .map(PontuacaoGrupo::getGrupo)  // Nome do grupo
                        .toList()
        );
    }

    private void carregarCriterios() throws SQLException {
        CriterioDao criterioDao = new CriterioDao();
        List<Criterio> criterios = criterioDao.listarCriterios();

        // Preenche o ComboBox com os nomes dos grupos
        criteriosComboBox.getItems().setAll(
                criterios.stream()
                        .map(Criterio::getTitulo)  // Nome do grupo
                        .toList()
        );
    }

    private void carregarAlunos() throws SQLException {
        AcompanharSprintsDao acompanharSprintsDao = new AcompanharSprintsDao();
        List<AreaDoAluno> alunos = acompanharSprintsDao.listarAlunos();

        paneAlunos.getChildren().clear();

        for(AreaDoAluno aluno : alunos) {
            exibirAlunos(aluno.getNomeAluno(),aluno.getGrupo());
        }

    }

    @FXML
    private void onGrupoSelecionado(ActionEvent event) {
        String grupoSelecionado = gruposComboBox.getValue();
        if (grupoSelecionado != null) {
            try {
                carregarAlunosDoGrupo(grupoSelecionado);  // Chama o método que carrega os alunos
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //Arrumar criterios para que puxe o filtro dos grupos caso selecionado
    @FXML
    private void onCriterioSelecionado(ActionEvent event) {
        String criterioSelecionado = criteriosComboBox.getValue();
        //String grupoSelecionado = gruposComboBox.getValue();
        if (criterioSelecionado != null) {
            try {
                //carregarAlunosDoGrupo(grupoSelecionado);
                carregarNotasDoCriterio(criterioSelecionado,sprintSelecionada); //Chama o método que carrega os alunos

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // Carrega os alunos do grupo selecionado
    private void carregarAlunosDoGrupo(String grupo) throws SQLException {
        AcompanharSprintsDao acompanharSprintsDao = new AcompanharSprintsDao();
        List<AreaDoAluno> alunos = acompanharSprintsDao.listarAlunosPorGrupo(grupo);

        paneAlunos.getChildren().clear();

        for(AreaDoAluno aluno : alunos) {
            exibirAlunos(aluno.getNomeAluno(),aluno.getGrupo());
        }

    }

    // Carrega os alunos do grupo selecionado
    private void carregarNotasDoCriterio(String tituloCriterio, int numSprint) throws SQLException {
        AcompanharSprintsDao acompanharSprintsDao = new AcompanharSprintsDao();
        List<AcompanharSprints> notas = acompanharSprintsDao.listarNotasPorCriterioESprint(tituloCriterio, numSprint);

        // Limpa o painel antes de atualizar os dados
        paneAlunos.getChildren().clear();

        // Itera sobre os resultados e exibe os alunos com suas notas calculadas
        for (AcompanharSprints notaAluno : notas) {
            exibirAlunos(
                    notaAluno.getNomeAluno(),
                    notaAluno.getGrupo(),
                    notaAluno.getMediaNotaAluno(), // A média já é calculada no DAO
                    notaAluno.getSomaTotalCriterio()); // Soma total do critério para exibir ou usar, se necessário

        }
    }

    //Sobrecarga do metodo exibirAlunos
    private void exibirAlunos(String nome, String grupo) {
        // Chama a nova versão do método com valores padrão
        exibirAlunos(nome, grupo, 0.0, 0.0);
    }

    // Este método cria e exibe um aluno no painel
    private void exibirAlunos(String nome, String grupo, double mediaNota, double somaTotalCriterio) {
        // Criar um painel para cada aluno
        Pane alunoPane = new Pane();
        alunoPane.setPrefSize(550, 65);
        alunoPane.setLayoutX(4);
        alunoPane.setLayoutY(10);
        alunoPane.setStyle("-fx-background-color: #86B6DD; -fx-background-radius: 5;");

        Label nomeAluno = criaLabel(5,7, "Aluno: " + nome, "-fx-text-fill: black; -fx-font-size: 12px;");
        Label grupoAluno = criaLabel(5,40,"Grupo: ", "-fx-text-fill: black; -fx-font-size: 12px;");
        Label lblAluno = criaLabel(355,7,"NOTA DA SPRINT:", "-fx-text-fill: black; -fx-font-size: 12px;");
        Label mediaAluno = criaLabel(375,30,String.format("%.2f", mediaNota) + " / ", "-fx-text-fill: black; -fx-font-size: 12px;");
        Label notaTotal = criaLabel(407, 30, String.format("%.2f", somaTotalCriterio), "-fx-text-fill: black; -fx-font-size: 12px;");

        // Criar o ComboBox para os grupos
        ComboBox<String> grupoComboBox = new ComboBox<>();
        grupoComboBox.getItems().setAll(gruposComboBox.getItems());  // Preenche com os grupos disponíveis
        grupoComboBox.setValue(grupo);  // Define o grupo atual do aluno
        grupoComboBox.setLayoutX(50);
        grupoComboBox.setLayoutY(35);
        grupoComboBox.setPrefWidth(150);
        grupoComboBox.setOnAction(event -> {
            String grupoSelecionado = grupoComboBox.getValue();
            System.out.println("Grupo selecionado para o aluno: " + grupoSelecionado);
            alteracoesGrupo.put(nome, grupoSelecionado); // Armazena a alteração dos grupos dos alunos
        });

        // Botão para gerar relatório individual
        Button btnGerarRelatorio = new Button("Relatório");
        btnGerarRelatorio.setLayoutX(475);
        btnGerarRelatorio.setLayoutY(20);
        btnGerarRelatorio.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnGerarRelatorio.setOnAction(event -> onClickGerarRelatorioIndividual(nome));

        // Adicionar os textos e o ComboBox ao painel do aluno
        alunoPane.getChildren().addAll(
                nomeAluno, grupoAluno,grupoComboBox,lblAluno, mediaAluno,notaTotal,btnGerarRelatorio
        );

        int numeroDeAlunos = paneAlunos.getChildren().size();
        double novaPosicaoY = numeroDeAlunos == 0 ? 5 : numeroDeAlunos * 70;
        alunoPane.setLayoutY(novaPosicaoY);

        paneAlunos.getChildren().add(alunoPane);
        paneAlunos.setPrefHeight(novaPosicaoY);


    }

    public void gerarRelatorio(List<GrupoSprint> dados) throws Exception {
        // Criação do documento PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("relatorio_grupos.pdf"));
        document.open();

        // Título do relatório
        Paragraph titulo = new Paragraph("Relatório de Grupos e Sprints", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        // Criação da tabela
        int numColunas = dados.stream().mapToInt(g -> g.getNotas().size()).max().orElse(1) + 1; // +1 para o nome do grupo
        PdfPTable table = new PdfPTable(numColunas);
        table.setWidthPercentage(100);

        // Cabeçalho
        PdfPCell headerGrupo = new PdfPCell(new Phrase("Grupo", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(headerGrupo);

        for (int i = 1; i < numColunas; i++) {
            PdfPCell headerSprint = new PdfPCell(new Phrase("Sprint " + i, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            table.addCell(headerSprint);
        }

        // Dados
        for (GrupoSprint grupo : dados) {
            table.addCell(new PdfPCell(new Phrase(grupo.getNome())));
            for (int nota : grupo.getNotas()) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(nota))));
            }
            // Preenche células vazias para grupos com menos sprints
            for (int i = grupo.getNotas().size(); i < numColunas - 1; i++) {
                table.addCell(new PdfPCell(new Phrase("")));
            }
        }

        // Adiciona a tabela ao documento
        document.add(table);
        document.close();

        System.out.println("Relatório gerado com sucesso: relatorio_grupos.pdf");
    }

    public void gerarRelatorioAlunos(List<AcompanharSprints> alunos) throws Exception {
        // Criação do documento PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("relatorio_alunos_com_notas.pdf"));
        document.open();

        // Título do relatório
        Paragraph titulo = new Paragraph("Relatório de Alunos por Grupo", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        // Criação da tabela com 3 colunas: Grupo, Nome do Aluno, e Nota
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        // Cabeçalhos
        PdfPCell headerGrupo = new PdfPCell(new Phrase("Grupo", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(headerGrupo);

        PdfPCell headerAluno = new PdfPCell(new Phrase("Aluno", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(headerAluno);

        PdfPCell headerNota = new PdfPCell(new Phrase("Nota", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(headerNota);

        PdfPCell headerCriterio = new PdfPCell(new Phrase("Critério", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(headerCriterio);

        PdfPCell headerSprint = new PdfPCell(new Phrase("Sprint", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        table.addCell(headerSprint);

        // Dados
        for (AcompanharSprints aluno : alunos) {
            table.addCell(new PdfPCell(new Phrase(aluno.getGrupo())));
            table.addCell(new PdfPCell(new Phrase(aluno.getNomeAluno())));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", aluno.getMediaNotaAluno()))));
            table.addCell(new PdfPCell(new Phrase(aluno.getCriterio())));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(aluno.getSprint()))));
        }

        // Adiciona a tabela ao documento
        document.add(table);
        document.close();

        System.out.println("Relatório gerado com sucesso: relatorio_alunos_com_notas.pdf");
    }

    @FXML
    private void onClickGerarRelatorioIndividual(String nomeAluno) {
        try {
            AcompanharSprintsDao acompanharSprintsDao = new AcompanharSprintsDao();

            // Buscar as notas e informações do aluno pelo nome
            List<AcompanharSprints> alunoInfo = acompanharSprintsDao.listarNotasDoAluno(nomeAluno,sprintSelecionada);

            // Gerar o relatório individual
            gerarRelatorioAlunoIndividual(alunoInfo);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao gerar relatório individual: " + e.getMessage());
        }
    }

    private void gerarRelatorioAlunoIndividual(List<AcompanharSprints> alunoInfo) throws Exception {
        // Criação do documento PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("relatorio_individual.pdf"));
        document.open();

        // Título do relatório
        Paragraph titulo = new Paragraph("Relatório Individual do Aluno", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        // Criação da tabela com as informações do aluno
        PdfPTable table = new PdfPTable(4); // 4 colunas: Nome do Aluno, Nota, Critério, Sprint
        table.setWidthPercentage(100);

        // Cabeçalhos da tabela
        table.addCell(new PdfPCell(new Phrase("Aluno", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        table.addCell(new PdfPCell(new Phrase("Nota", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        table.addCell(new PdfPCell(new Phrase("Critério", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        table.addCell(new PdfPCell(new Phrase("Sprint", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));

        // Adicionar os dados do aluno
        for (AcompanharSprints sprints : alunoInfo) {
            table.addCell(new PdfPCell(new Phrase(sprints.getNomeAluno())));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", sprints.getMediaNotaAluno()))));
            table.addCell(new PdfPCell(new Phrase(sprints.getCriterio())));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(sprints.getSprint()))));
        }

        // Adiciona a tabela ao documento
        document.add(table);
        document.close();

        System.out.println("Relatório individual gerado com sucesso: relatorio_individual.pdf");
    }



    private Label criaLabel(double layoutX, double layoutY, String descricao, String style) {
        Label label = new Label(descricao);
        label.setLayoutX(layoutX);
        label.setLayoutY(layoutY);
        label.setStyle(style);
        return label;
    }


    @FXML
    void onClickGerarRelatorioAlunos(ActionEvent event) {
        try {
            AcompanharSprintsDao acompanharSprintsDao = new AcompanharSprintsDao();
            List<AcompanharSprints> alunos = acompanharSprintsDao.listarDadosParaRelatorioAlunos();
            gerarRelatorioAlunos(alunos);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }


    @FXML
    void onClickGerarRelatorioGrupos(ActionEvent event) {
        try {
            // 1. Coletar os dados (exemplo usando o DAO)
            AcompanharSprintsDao acompanharSprintsDao = new AcompanharSprintsDao();
            List<GrupoSprint> dados = acompanharSprintsDao.listarDadosParaRelatorio(); // Método no DAO para buscar os dados

            // 2. Gerar o relatório
            gerarRelatorio(dados);

        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
            // Exibir mensagem de erro para o usuário
        }
    }

    @FXML
    private void onGrupoAlunoSelecionado(ActionEvent event) {}

    @FXML
    void onClickLimpar(ActionEvent event) throws SQLException {
        carregarAlunos();
    }

    @FXML
    protected void onbtnconfirmar(ActionEvent event) throws SQLException {
        AcompanharSprintsDao acompanharSprintsDao = new AcompanharSprintsDao(); // DAO para manipular dados dos alunos
        for (Map.Entry<String, String> entry : alteracoesGrupo.entrySet()) {
            String nomeAluno = entry.getKey();
            String novoGrupo = entry.getValue();
            try {
                // Obter o ID do aluno baseado no nome (crie um método no DAO para isso)
                long idAluno = acompanharSprintsDao.obterIdPorNome(nomeAluno);
                acompanharSprintsDao.atualizarGrupoAluno(idAluno, nomeAluno, novoGrupo);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Erro ao atualizar grupo do aluno: " + nomeAluno);
            }
        }
        alteracoesGrupo.clear(); // Limpa o mapa após processar as alterações
        System.out.println("Alterações confirmadas.");
        carregarAlunos();
    }



    @FXML
    protected void onClickVoltarMenu(ActionEvent event) throws IOException {
        Main.setRoot("TelaMenu-view");
    }


}
