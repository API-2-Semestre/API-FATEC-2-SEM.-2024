package com.cyber.cybernexuspacer.controller;

import com.cyber.cybernexuspacer.dao.CadastroTurmaDao;
import com.cyber.cybernexuspacer.dao.ConexaoDao;
import com.cyber.cybernexuspacer.dao.SprintDao;
import com.cyber.cybernexuspacer.entity.AreaDoAluno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static com.mysql.cj.conf.PropertyKey.logger;
//---------------------

public class CadastroDeTurmaController {

    @FXML
    private Button button_carregarDocumento;

    @FXML
    private Button button_confirmarAlunos;

    @FXML
    private Button button_sair;

    @FXML
    private Label label_cadastrodeturma;

    @FXML
    private Label label_olaProfessor;

    @FXML
    private Label label_top;

    @FXML
    private Pane pane_planilha;
    @FXML
    private TableColumn<?, ?> coluna_Aluno;

    @FXML
    private TableColumn<?, ?> coluna_Email;

    @FXML
    private TableColumn<?, ?> coluna_Grupo;

    @FXML
    private ImageView logo_fatec;

    @FXML
    private TableView<AreaDoAluno> tabela;

    private ObservableList<AreaDoAluno> listaAluno = FXCollections.observableArrayList();

    private Connection connection;

    @FXML
    public void initialize() {
        // Configura as colunas com os dados da classe Pessoa
        coluna_Aluno.setCellValueFactory(new PropertyValueFactory<>("nomeAluno"));
        coluna_Email.setCellValueFactory(new PropertyValueFactory<>("email"));
        coluna_Grupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));


        // Configura a tabela para exibir a lista de pessoas
        tabela.setItems(listaAluno);
    }

    // Método para garantir que a conexão está aberta
    private Connection ensureConnectionIsOpen() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = ConexaoDao.getConnection(); // ou use seu pool de conexões
        }
        return connection;
    }

    @FXML
    void onClickbtnConfirmarAlunos(ActionEvent event) throws IOException, SQLException {
        Connection conn = null;
        try {
            // Garante que a conexão está aberta
            conn = ensureConnectionIsOpen();
            conn.setAutoCommit(false); // Inicia a transação

            boolean sucesso = true; // Flag para verificar se tudo ocorreu bem

            for (AreaDoAluno aluno : listaAluno) {
                // Verifica se o aluno já existe
                if (!CadastroTurmaDao.alunoExists(aluno)) {
                    // Chama a DAO para cadastrar o aluno e os dados relacionados
                    CadastroTurmaDao.CadastrarAlunos(aluno, conn);
                } else {
                    // Alerta caso o aluno já exista
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Aluno Duplicado");
                    alert.setHeaderText("O aluno " + aluno.getNomeAluno() + " já está cadastrado.");
                    alert.setContentText("Verifique a lista de alunos.");
                    alert.showAndWait();
                    System.out.println("Aluno já existe: " + aluno.getNomeAluno());
                    sucesso = false; // Marca que houve um problema com esse aluno
                }
            }

            if (sucesso) {

                // Confirma a transação
                conn.commit();

                // Mensagem de sucesso
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText("Alunos cadastrados com sucesso!");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            // Em caso de erro, reverte a transação
            if (conn != null) {
                conn.rollback();
            }

            // Alerta o usuário sobre o erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao Cadastrar Alunos");
            alert.setHeaderText("Ocorreu um erro ao cadastrar os alunos.");
            alert.setContentText("Por favor, verifique os dados e tente novamente.");
            alert.showAndWait();

            e.printStackTrace();
        } finally {
            // Restaura o auto-commit e fecha a conexão se não for mais necessária
            if (conn != null) {
                conn.setAutoCommit(true); // Restaura o comportamento padrão
                conn.close(); // Fecha a conexão
            }
        }
    }


    @FXML
    public void onClickCarregarPlanilha(javafx.event.ActionEvent actionEvent) {
        try {
            // Verifica se existem sprints cadastradas
            SprintDao sprintDao = new SprintDao();
            List existeSprint = sprintDao.listarSprints();

            if (existeSprint.isEmpty()) {
                // Exibe um alerta caso não existam sprints cadastradas
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aviso");
                alert.setHeaderText("Nenhuma Sprint Encontrada");
                alert.setContentText("Por favor, cadastre as sprints antes de importar o arquivo de alunos.");
                alert.showAndWait();
                return; // Interrompe a execução se não houver sprints
            }

            // Abre o explorador de arquivos para a escolha do arquivo
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File selectFile = fileChooser.showOpenDialog(button_carregarDocumento.getScene().getWindow());

            if (selectFile != null) {
                try (Scanner scanner = new Scanner(selectFile)) {
                    listaAluno.clear();  // Limpa a lista antes de adicionar novos dados

                    // Ignora a linha do cabeçalho (caso tenha)
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }

                    // Variável para gerar IDs fictícios
                    int idTemporario = 1;  // Inicia o contador do ID temporário

                    boolean arquivoValido = true; // Flag para indicar se o arquivo está no formato correto

                    // Lê o arquivo linha por linha e valida cada linha
                    while (scanner.hasNextLine()) {
                        String linha = scanner.nextLine().trim();

                        // Se a linha estiver vazia, pula para a próxima
                        if (linha.isEmpty()) {
                            continue;
                        }

                        String[] dados = linha.split(",");

                        // Verifica se a linha tem exatamente 3 colunas
                        if (dados.length != 3) {
                            // Exibe um alerta caso o formato da linha esteja errado
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Erro de Formato");
                            alert.setHeaderText("Formato Incorreto");
                            alert.setContentText("A linha não está no formato esperado. \nCada linha deve ter 'nome,email,grupo'. \nLinha ignorada: " + linha);
                            alert.showAndWait();
                            arquivoValido = false; // Marca que o arquivo está inválido
                            break; // Interrompe a leitura do arquivo
                        }

                        // Se a linha estiver correta, adiciona o aluno à lista
                        String nome = dados[0];
                        String email = dados[1];
                        String grupo = dados[2];
                        AreaDoAluno aluno = new AreaDoAluno(idTemporario, nome, email, grupo, "fatec2024", "Aluno");
                        listaAluno.add(aluno);

                        // Incrementa o ID temporário para o próximo aluno
                        idTemporario++;
                    }

                    // Se o arquivo não for válido, exibe um alerta e não salva nenhum aluno
                    if (!arquivoValido) {
                        listaAluno.clear();  // Limpa a lista caso o arquivo seja inválido
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Erro no Arquivo");
                        alert.setHeaderText("Arquivo Inválido");
                        alert.setContentText("O arquivo contém erro(s) de formatação. Nenhum aluno foi adicionado.");
                        alert.showAndWait();
                    } else if (listaAluno.isEmpty()) {
                        // Caso o arquivo esteja vazio ou não tenha alunos válidos, exibe um alerta
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Aviso");
                        alert.setHeaderText("Nenhum Aluno Encontrado");
                        alert.setContentText("O arquivo CSV não contém alunos válidos.");
                        alert.showAndWait();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void onClickSair(ActionEvent event) throws IOException {
        Main.setRoot("TelaMenu-view");
    }


}







