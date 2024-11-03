package es.juliogtrenard.proyectoolimpiadas.controladores;

import es.juliogtrenard.proyectoolimpiadas.lenguaje.LenguajeSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
}