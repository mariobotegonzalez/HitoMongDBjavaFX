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

public class RegisterController {
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_email;
    @FXML
    private PasswordField pf_password;
    @FXML
    private PasswordField pf_confirmPassword;
    @FXML
    private Label lbl_status;
    @FXML
    private Button btn_register;
    @FXML
    private Button btn_cancel;

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
    protected void onRegisterButtonClick() throws IOException {
        if (!validarCampos()) {
            lbl_status.setText("Por favor, complete todos los campos.");
            return;
        }

        if (!pf_password.getText().equals(pf_confirmPassword.getText())) {
            lbl_status.setText("Las contraseñas no coinciden.");
            return;
        }

        MongoCollection<Document> collection = database.getCollection("users");

        // Verificar si el usuario ya existe
        Document existingUser = collection.find(new Document("username", tf_username.getText())).first();
        if (existingUser != null) {
            lbl_status.setText("El usuario ya existe en la base de datos.");
            return;
        }

        Document newUser = new Document("username", tf_username.getText())
                .append("email", tf_email.getText())
                .append("password", pf_password.getText());
        collection.insertOne(newUser);
        lbl_status.setText("Usuario registrado con éxito!");

        // Load and show login view
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 480);
        scene.getStylesheets().add(getClass().getResource("/com/empresa/hitojavafxmongo/styles.css").toExternalForm());
        Stage stage = (Stage) btn_register.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Iniciar Sesión");
        stage.show();
    }

    @FXML
    protected void onCancelButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 200);
        scene.getStylesheets().add(getClass().getResource("/com/empresa/hitojavafxmongo/styles.css").toExternalForm());
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Iniciar Sesión");
        stage.show();
    }

    private boolean validarCampos() {
        return !(tf_username.getText().isEmpty() || tf_email.getText().isEmpty() || pf_password.getText().isEmpty() || pf_confirmPassword.getText().isEmpty());
    }
}
