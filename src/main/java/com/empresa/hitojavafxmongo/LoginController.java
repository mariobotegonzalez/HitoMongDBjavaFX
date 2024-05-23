package com.empresa.hitojavafxmongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField pf_password;
    @FXML
    private Label lbl_status;
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_register;

    private MongoClient mongoClient;
    private MongoDatabase database;

    @FXML
    public void initialize() {
        conectarBaseDatos();
    }

    private void conectarBaseDatos() {
        if (database == null) {
            String url = "mongodb+srv://administrador:Abc123456@mariobot02.loctnzs.mongodb.net/";
            mongoClient = MongoClients.create(url);
            database = mongoClient.getDatabase("hito");
        }
    }

    @FXML
    protected void onLoginButtonClick() throws IOException {
        if (!validarCampos()) {
            lbl_status.setText("Por favor, complete todos los campos.");
            return;
        }

        MongoCollection<Document> collection = database.getCollection("users");
        Document query = new Document("username", tf_username.getText()).append("password", pf_password.getText());
        Document user = collection.find(query).first();

        if (user != null) {
            lbl_status.setText("Login exitoso!");

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 480);
            scene.getStylesheets().add(getClass().getResource("/com/empresa/hitojavafxmongo/styles.css").toExternalForm());
            Stage stage = (Stage) btn_login.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            lbl_status.setText("Usuario o contraseña incorrecta.");
        }
    }

    @FXML
    protected void onGoToRegisterButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 300);
        scene.getStylesheets().add(getClass().getResource("/com/empresa/hitojavafxmongo/styles.css").toExternalForm());
        Stage stage = (Stage) btn_register.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Crear Usuario");
        stage.show();
    }

    private boolean validarCampos() {
        return !(tf_username.getText().isEmpty() || pf_password.getText().isEmpty());
    }
}
