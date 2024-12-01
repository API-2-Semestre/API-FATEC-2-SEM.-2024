package com.cyber.cybernexuspacer.controller;

import com.cyber.cybernexuspacer.dao.LoginDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RecuperarSenhaController {
    @FXML
    private AnchorPane anchorFundo;

    @FXML
    private Button btnClickEnviar;


    @FXML
    private ImageView imgFatec;

    @FXML
    private Label lblTxtFatec;

    @FXML
    private Label lblTxtSistemaPacer;

    @FXML
    private PasswordField fieldNovaSenha;

    @FXML
    private PasswordField fieldRepetirSenha;

    @FXML
    private Text textInsiraEnail;

    @FXML
    private Text txtDigiteNovamente;

    @FXML
    private Text txtDigiteSenhha;

    //@FXML
    //void onClickEnviar(ActionEvent event) throws IOException {
    //
    //    Main.setRoot("AreaDoAluno-view");
    //}

    private LoginDao loginDao = new LoginDao();
    private String nomeUsuario;

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    @FXML
    private Button btnEnviar;
    public void onClickEnviar() throws SQLException, IOException {

        String novaSenha = fieldNovaSenha.getText();
        String repetirSenha = fieldRepetirSenha.getText();

        // Verifica se as senhas coincidem
        if (novaSenha.equals(repetirSenha)) {
            try {
                // Atualiza a senha no banco de dados
                boolean sucesso = loginDao.atualizarSenha(nomeUsuario, novaSenha);
                if (sucesso) {
                    // Carregar nova view
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cyber/cybernexuspacer/fxml/login-view.fxml"));
                    Parent root = loader.load();

                    // Obtém o stage atual
                    Stage stage = (Stage) btnEnviar.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                    //Msg de sucesso no banco
                    System.out.println("Senha atualizada com sucesso!");
                } else {
                    System.out.println("Erro ao atualizar senha!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Erro de conexão com o banco de dados.");
            }
        } else {
            System.out.println("As senhas não coincidem!");
        }
    }

}


