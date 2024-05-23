package com.empresa.hitojavafxmongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private TableView<Cliente> tv_datos;
    @FXML
    private TableColumn<Cliente, String> tc_nombre;
    @FXML
    private TableColumn<Cliente, String> tc_email;
    @FXML
    private TableColumn<Cliente, String> tc_telefono;
    @FXML
    private TableColumn<Cliente, String> tc_direccion;
    @FXML
    private TextField tf_buscarNombre;

    private MongoClient mongoClient;
    private MongoDatabase database;

    @FXML
    public void initialize() {
        tc_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tc_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tc_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        tc_direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Añadir ContextMenu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Editar");
        MenuItem deleteItem = new MenuItem("Eliminar");

        editItem.setOnAction(event -> editarCliente());
        deleteItem.setOnAction(event -> eliminar());

        contextMenu.getItems().addAll(editItem, deleteItem);

        tv_datos.setContextMenu(contextMenu);

        // Conectar a la base de datos y mostrar datos al iniciar la pantalla
        conectarBaseDatos();
        mostrar();
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Bienvenido a Mi aplicacion de JavaFX con MongoDB");
    }

    @FXML
    protected void mostrar() {
        if (database == null) {
            welcomeText.setText("No hay conexión a la base de datos.");
            return;
        }
        MongoCollection<Document> collection = database.getCollection("clientes");

        ObservableList<Cliente> clientes = FXCollections.observableArrayList();
        for (Document doc : collection.find()) {
            clientes.add(new Cliente(
                    doc.getObjectId("_id"),
                    doc.getString("nombre"),
                    doc.getString("email"),
                    doc.getString("telefono"),
                    doc.getString("direccion")
            ));
        }
        tv_datos.setItems(clientes);
    }

    @FXML
    protected void leer() {
        if (database == null) {
            welcomeText.setText("No hay conexión a la base de datos.");
            return;
        }
        String searchText = tf_buscarNombre.getText();
        MongoCollection<Document> collection = database.getCollection("clientes");
        ObservableList<Cliente> clientes = FXCollections.observableArrayList();

        for (Document doc : collection.find(new Document("nombre", new Document("$regex", "^" + searchText)))) {
            clientes.add(new Cliente(
                    doc.getObjectId("_id"),
                    doc.getString("nombre"),
                    doc.getString("email"),
                    doc.getString("telefono"),
                    doc.getString("direccion")
            ));
        }
        tv_datos.setItems(clientes);
    }

    @FXML
    protected void crear() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Crear Cliente");

        TextField tf_nombre = new TextField();
        TextField tf_email = new TextField();
        TextField tf_telefono = new TextField();
        TextField tf_direccion = new TextField();

        tf_nombre.setPromptText("Nombre");
        tf_email.setPromptText("Email");
        tf_telefono.setPromptText("Teléfono");
        tf_direccion.setPromptText("Dirección");

        Button saveButton = new Button("Guardar");
        saveButton.setOnAction(event -> {
            if (database == null) {
                welcomeText.setText("No hay conexión a la base de datos.");
                return;
            }
            if (tf_nombre.getText().isEmpty() || tf_email.getText().isEmpty() || tf_telefono.getText().isEmpty() || tf_direccion.getText().isEmpty()) {
                welcomeText.setText("Por favor, complete todos los campos del cliente.");
                return;
            }

            MongoCollection<Document> collection = database.getCollection("clientes");

            // Verificar si el cliente ya existe
            Document existingClient = collection.find(new Document("nombre", tf_nombre.getText())).first();
            if (existingClient != null) {
                welcomeText.setText("El cliente ya existe en la base de datos.");
                stage.close();
                return;
            }

            Document newClient = new Document("nombre", tf_nombre.getText())
                    .append("email", tf_email.getText())
                    .append("telefono", tf_telefono.getText())
                    .append("direccion", tf_direccion.getText());
            collection.insertOne(newClient);
            stage.close();
            mostrar();

            // Mostrar ventana emergente de éxito
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cliente Registrado");
            alert.setHeaderText(null);
            alert.setContentText("El cliente ha sido registrado correctamente.");
            alert.showAndWait();
        });

        VBox vbox = new VBox(tf_nombre, tf_email, tf_telefono, tf_direccion, saveButton);
        vbox.setSpacing(10);
        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    protected void eliminar() {
        if (database == null) {
            welcomeText.setText("No hay conexión a la base de datos.");
            return;
        }

        Cliente selectedClient = tv_datos.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            welcomeText.setText("Por favor, seleccione un cliente de la lista.");
            return;
        }

        MongoCollection<Document> collection = database.getCollection("clientes");
        Document query = new Document("_id", selectedClient.getId());
        collection.deleteOne(query);
        mostrar();
    }

    private void editarCliente() {
        Cliente selectedClient = tv_datos.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            welcomeText.setText("Por favor, seleccione un cliente de la lista.");
            return;
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Editar Cliente");

        TextField tf_nombre = new TextField(selectedClient.getNombre());
        TextField tf_email = new TextField(selectedClient.getEmail());
        TextField tf_telefono = new TextField(selectedClient.getTelefono());
        TextField tf_direccion = new TextField(selectedClient.getDireccion());

        Button saveButton = new Button("Guardar");
        saveButton.setOnAction(event -> {
            selectedClient.setNombre(tf_nombre.getText());
            selectedClient.setEmail(tf_email.getText());
            selectedClient.setTelefono(tf_telefono.getText());
            selectedClient.setDireccion(tf_direccion.getText());

            MongoCollection<Document> collection = database.getCollection("clientes");
            Document query = new Document("_id", selectedClient.getId());
            Document update = new Document("$set", new Document("nombre", tf_nombre.getText())
                    .append("email", tf_email.getText())
                    .append("telefono", tf_telefono.getText())
                    .append("direccion", tf_direccion.getText()));
            collection.updateOne(query, update);

            stage.close();
            mostrar();
        });

        VBox vbox = new VBox(tf_nombre, tf_email, tf_telefono, tf_direccion, saveButton);
        vbox.setSpacing(10);
        Scene scene = new Scene(vbox, 600, 480);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    protected void cerrarSesion() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 200);
            scene.getStylesheets().add(getClass().getResource("/com/empresa/hitojavafxmongo/styles.css").toExternalForm());
            Stage stage = (Stage) tv_datos.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void conectarBaseDatos() {
        if (database == null) {
            String url = "mongodb+srv://administrador:Abc123456@mariobot02.loctnzs.mongodb.net/";
            mongoClient = MongoClients.create(url);
            database = mongoClient.getDatabase("hito");
        }
    }

    public static class Cliente {
        private final ObjectId id;
        private String nombre;
        private String email;
        private String telefono;
        private String direccion;

        public Cliente(ObjectId id, String nombre, String email, String telefono, String direccion) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.telefono = telefono;
            this.direccion = direccion;
        }

        public ObjectId getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }
    }
}
