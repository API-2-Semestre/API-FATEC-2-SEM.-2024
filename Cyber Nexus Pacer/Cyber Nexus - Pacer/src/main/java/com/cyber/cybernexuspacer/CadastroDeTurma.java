package com.cyber.cybernexuspacer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CadastroDeTurma extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CadastroDeTurmaController.class.getResource("cadastroturma.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Pacer");
        stage.setScene(scene);
        stage.show();
    }

    private ImageView imageView;

    public void initialize() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ada.png")));
        imageView.setImage(image);
        Image image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/plane.png")));
        imageView.setImage(image);
    }

    public static void main(String[] args) {
        launch();
    }
}