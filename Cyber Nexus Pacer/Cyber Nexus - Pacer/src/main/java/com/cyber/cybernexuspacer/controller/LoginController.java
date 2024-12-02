package com.cyber.cybernexuspacer.controller;

import com.cyber.cybernexuspacer.dao.LoginDao;
import com.cyber.cybernexuspacer.entity.AreaDoAluno;
import com.cyber.cybernexuspacer.session.AlunoSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {

    public AnchorPane anchorlogin;
    public Label lblTxtSistemaPacer;
    @FXML
    private Button btnEntrar;

    @FXML
    private Text esqueciMinhaSenha;

    @FXML
    private ImageView imgFatec;

    @FXML
    private AnchorPane painelPrincipal;

    @FXML
    private Label txtSistemaPacer;

    @FXML
    private Label lblUsuarioESenhaInvalidos;

    @FXML
    private TextField usuarioLogin;

    @FXML
    private PasswordField usuarioSenha;

    private LoginDao loginDao = new LoginDao();

    @FXML
    void onClickbtnEntrar(ActionEvent event) throws IOException {
        String nome = usuarioLogin.getText();
        String senha = usuarioSenha.getText();
        String tipoUsuario = verificarTipoUsuario(nome);

        try {
            // Verifica se o login e a senha estão corretos no banco de dados
            if (loginDao.verificarLogin(nome, senha, tipoUsuario)) {
                // Se estiverem corretos, muda a tela
                if ("Aluno".equals(tipoUsuario) && "fatec2024".equals(senha)) {
                    redirecionarParaRecuperacaoSenha(nome);
                }
                else if ("Aluno".equals(tipoUsuario)) {
                    boolean podeAcessar = loginDao.verificarAcessoAluno(nome);  // Verifica se o aluno pode acessar
                    if (podeAcessar) {
                        // Se o aluno puder acessar, continua o processo de login normal
                        // Aqui, após a validação, busca os detalhes do aluno e armazena na sessão
                        AreaDoAluno aluno = loginDao.buscarAlunoPorEmail(nome);  // Método para buscar detalhes do aluno
                        AlunoSession.setAlunoLogado(aluno);

                        Main.setRoot("AreaDoAluno-view");
                        System.out.println("email: " + nome);
                    } else {
                        // Se o aluno não puder acessar, exibe um alerta
                        lblUsuarioESenhaInvalidos.setText("Sistema não liberado ainda. Acesso bloqueado.");
                        lblUsuarioESenhaInvalidos.setLayoutX(270);
                    }

                }
                // Lógica para outros tipos de usuários (Admin, Professor)
                else if ("Admin".equals(tipoUsuario)) {
                    Main.setRoot("TelaMenu-view");
                } else if ("Professor".equals(tipoUsuario)) {
                    Main.setRoot("TelaMenu-view");
                }
            } else {
                // Exibe a mensagem de erro se as credenciais estiverem incorretas
                lblUsuarioESenhaInvalidos.setText("Usuário ou senha incorretos!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void redirecionarParaRecuperacaoSenha(String nomeUsuario) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cyber/cybernexuspacer/fxml/recuperacaoSenha-view.fxml"));
        Parent root = loader.load();

        // Obtém o controller da tela de recuperação de senha e define o nome do usuário
        RecuperarSenhaController recuperarSenhaController = loader.getController();
        recuperarSenhaController.setNomeUsuario(nomeUsuario);

        // Configura a nova cena
        Stage stage = (Stage) btnEntrar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private String verificarTipoUsuario(String nome) {
        // Lógica para determinar o tipo de usuário com base no nome ou outra variável
        // Aqui você pode aplicar alguma lógica específica para diferenciar os tipos de usuários
        if (nome.contains("admin")) {
            return "Admin";
        } else if (nome.contains("professor")) {
            return "Professor";
        } else {
            return "Aluno";
        }
    }

    @FXML
    private void esqueciPopup() {
        String email = usuarioLogin.getText();

        if (email.isEmpty()) {
            // Exibe um alerta solicitando o preenchimento do email
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Informação");
            alerta.setHeaderText(null);
            alerta.setContentText("Escreva o email no devido campo e clique novamente neste link para recuperação de senha.");
            alerta.showAndWait();
        } else {
            try {
                // Verifica se o email existe no banco de dados
                if (loginDao.emailExiste(email)) {
                    // Redireciona para a tela de recuperação de senha
                    redirecionarParaRecuperacaoSenha(email);
                } else {
                    // Alerta caso o email não exista
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro");
                    alerta.setHeaderText(null);
                    alerta.setContentText("O email fornecido não foi encontrado.");
                    alerta.showAndWait();
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
