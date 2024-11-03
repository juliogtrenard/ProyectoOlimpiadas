package es.juliogtrenard.proyectoolimpiadas.controladores;

import es.juliogtrenard.proyectoolimpiadas.dao.DaoDeportista;
import es.juliogtrenard.proyectoolimpiadas.lenguaje.LenguajeSwitcher;
import es.juliogtrenard.proyectoolimpiadas.modelos.Deportista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ControladorPrincipal implements Initializable {
    /**
     * ComboBox de las tablas
     */
    @FXML
    private ComboBox<String> cbTabla;

    /**
     * TextField para filtrar por nombre
     */
    @FXML
    private TextField filtroNombre;

    /**
     * RadioMenuItem para el idioma Inglés
     */
    @FXML
    private RadioMenuItem langEN;

    /**
     * RadioMenuItem para el idioma Español
     */
    @FXML
    private RadioMenuItem langES;

    /**
     * Grupo de ToggleButton para el idioma
     */
    @FXML
    private ToggleGroup tgIdioma;

    /**
     * Tabla
     */
    @FXML
    private TableView tabla;

    /**
     * ResourceBundle de la aplicación
     */
    @FXML
    private ResourceBundle resources;

    private ObservableList masterData = FXCollections.observableArrayList();
    private ObservableList filteredData = FXCollections.observableArrayList();

    /**
     * Se ejecuta cuando se inicia la apliación
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;

        if (resources.getLocale().equals(new Locale("es"))) {
            langES.setSelected(true);
        } else {
            langEN.setSelected(true);
        }

        tgIdioma.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            Locale locale;
            if (langES.isSelected()) {
                locale = new Locale("es");
            } else {
                locale = new Locale("en");
            }
            new LenguajeSwitcher((Stage) tabla.getScene().getWindow()).switchLenguaje(locale);
        });

        filtroNombre.setOnKeyTyped(keyEvent -> filtrar());

        tabla.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                editar(null);
            }
        });

        cargarDeportistas();
    }

    public void aniadirDeporte(ActionEvent actionEvent) {
    }

    public void modificarDeporte(ActionEvent actionEvent) {
    }

    public void eliminarDeporte(ActionEvent actionEvent) {
    }

    public void aniadirOlimpiada(ActionEvent actionEvent) {
    }

    public void modificarOlimpiada(ActionEvent actionEvent) {
    }

    public void eliminarOlimpiada(ActionEvent actionEvent) {
    }

    public void aniadirEvento(ActionEvent actionEvent) {
    }

    public void modificarEvento(ActionEvent actionEvent) {
    }

    public void eliminarEvento(ActionEvent actionEvent) {
    }

    public void aniadirEquipo(ActionEvent actionEvent) {
    }

    public void modificarEquipo(ActionEvent actionEvent) {
    }

    public void eliminarEquipo(ActionEvent actionEvent) {
    }

    /**
     * Añadir un deportista
     * @param actionEvent El evento de la acción
     */
    @FXML
    public void aniadirDeportista(ActionEvent actionEvent) {
        try {
            Window ventana = tabla.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/deportista.fxml"),resources);
            ControladorDeportista controlador = new ControladorDeportista();
            fxmlLoader.setController(controlador);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(resources.getString("app.nombre"));
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarDeportistas();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            alerta(resources.getString("error.ventana"));
        }
    }

    /**
     * Agrega en la tabla las columnas del deportista
     */
    private void cargarDeportistas() {
        // Vaciar tabla
        tabla.getSelectionModel().clearSelection();
        filtroNombre.setText(null);
        filtroNombre.setDisable(false);
        masterData.clear();
        filteredData.clear();
        tabla.getItems().clear();
        tabla.getColumns().clear();
        // Cargar columnas
        TableColumn<Deportista, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory("id_deportista"));
        TableColumn<Deportista, String> colNombre = new TableColumn<>(resources.getString("label.nombre"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        TableColumn<Deportista, Deportista.SexCategory> colSexo = new TableColumn<>(resources.getString("label.sexo"));
        colSexo.setCellValueFactory(new PropertyValueFactory("sexo"));
        TableColumn<Deportista, Integer> colPeso = new TableColumn<>(resources.getString("label.peso"));
        colPeso.setCellValueFactory(new PropertyValueFactory("peso"));
        TableColumn<Deportista, Integer> colAltura = new TableColumn<>(resources.getString("label.altura"));
        colAltura.setCellValueFactory(new PropertyValueFactory("altura"));
        tabla.getColumns().addAll(colId,colNombre,colSexo,colPeso,colAltura);
        // Cargar deportistas
        ObservableList<Deportista> deportistas = DaoDeportista.cargarListado();
        masterData.setAll(deportistas);
        tabla.setItems(deportistas);
    }

    /**
     * Abre la ventana para la edición del deportista cuando se hace doble clic
     *
     * @param event
     */
    @FXML
    void editar(ActionEvent event) {
        Object seleccion = tabla.getSelectionModel().getSelectedItem();

        if (seleccion != null) {
            Deportista deportista = (Deportista) seleccion;
            try {
                Window ventana = tabla.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/deportista.fxml"),resources);
                ControladorDeportista controlador = new ControladorDeportista(deportista);
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setTitle(resources.getString("app.nombre"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                cargarDeportistas();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                alerta(resources.getString("ventana.error"));
            }
        }
    }

    /**
     * Filtra la tabla por nombre
     */
    public void filtrar() {
        String valor = filtroNombre.getText();

        if (valor != null) {
            valor = valor.toLowerCase();

            if (valor.isEmpty()) {
                tabla.setItems(masterData);
            } else {
                filteredData.clear();
                for (Object obj : masterData) {
                    Deportista deportista = (Deportista) obj;
                    String nombre = deportista.getNombre();
                    nombre = nombre.toLowerCase();
                    if (nombre.contains(valor)) {
                        filteredData.add(deportista);
                    }
                }
                tabla.setItems(filteredData);
            }
        }
    }

    /**
     * Función que muestra un mensaje de alerta al usuario
     *
     * @param texto contenido de la alerta
     */
    public void alerta(String texto) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("Error");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

    /**
     * Función que muestra un mensaje de confirmación al usuario
     *
     * @param texto contenido del mensaje
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}