package es.juliogtrenard.proyectoolimpiadas.controladores;

import es.juliogtrenard.proyectoolimpiadas.dao.DaoDeporte;
import es.juliogtrenard.proyectoolimpiadas.modelos.Deporte;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Clase que controla los eventos de la ventana deportes
 */
public class ControladorDeporte implements Initializable {
    /**
     * El deporte
     */
    private Deporte deporte;

    /**
     * El deporte a crear
     */
    private Deporte crear;

    /**
     * Botón para eliminar un deporte
     */
    @FXML
    private Button btnEliminar;

    /**
     * ComboBox para seleccionar el deporte
     */
    @FXML
    private ComboBox<Deporte> cbDeporte;

    /**
     * TextField para introducir el nombre del deporte
     */
    @FXML
    private TextField txtNombre;

    /**
     * Recursos a usar
     */
    @FXML
    private ResourceBundle resources;

    /**
     * Se ejecuta cuando se inicia la ventana
     *
     * @param url url
     * @param resourceBundle Recursos
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        this.deporte = null;
        btnEliminar.setDisable(true);
        crear = new Deporte();
        crear.setId_deporte(0);
        crear.setNombre(resources.getString("cb.nuevo"));
        cargarDeportes();
        cbDeporte.getSelectionModel().selectedItemProperty().addListener(this::cambioDeporte);
    }

    /**
     * Carga los deportes de la base de datos al ComboBox
     */
    public void cargarDeportes() {
        cbDeporte.getItems().clear();
        cbDeporte.getItems().add(crear);
        ObservableList<Deporte> deportes = DaoDeporte.cargarListado();
        cbDeporte.getItems().addAll(deportes);
        cbDeporte.getSelectionModel().select(0);
    }

    /**
     * Listener del cambio del ComboBox
     *
     * @param observable observable
     * @param oldValue valor antiguo
     * @param newValue valor nuevo
     */
    public void cambioDeporte(ObservableValue<? extends Deporte> observable, Deporte oldValue, Deporte newValue) {
        if (newValue != null) {
            btnEliminar.setDisable(true);
            if (newValue.equals(crear)) {
                deporte = null;
                txtNombre.setText(null);
            } else {
                deporte = newValue;
                txtNombre.setText(deporte.getNombre());
                if (DaoDeporte.esEliminable(deporte)) {
                    btnEliminar.setDisable(false);
                }
            }
        }
    }

    /**
     * Cierra la ventana
     *
     * @param event El evento
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage)txtNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Valida y procesa los datos
     *
     * @param event El evento
     */
    @FXML
    void guardar(ActionEvent event) {
        if (txtNombre.getText().isEmpty()) {
            alerta(resources.getString("validar.deporte.nombre"));
        } else {
            Deporte nuevo = new Deporte();
            nuevo.setNombre(txtNombre.getText());
            if (this.deporte == null) {
                int id = DaoDeporte.insertar(nuevo);
                if (id == -1) {
                    alerta(resources.getString("guardar.error"));
                } else {
                    confirmacion(resources.getString("guardar.deporte"));
                    cargarDeportes();
                }
            } else {
                if (DaoDeporte.modificar(this.deporte, nuevo)) {
                    confirmacion(resources.getString("actualizar.deporte"));
                    cargarDeportes();
                } else {
                    alerta(resources.getString("guardar.error"));
                }
            }
        }
    }

    /**
     * Elimina un deporte
     *
     * @param event El evento
     */
    @FXML
    void eliminar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(txtNombre.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle(resources.getString("ventana.confirmar"));
        alert.setContentText(resources.getString("borrar.deporte"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (DaoDeporte.eliminar(deporte)) {
                confirmacion(resources.getString("borrar.deporte.bien"));
                cargarDeportes();
            } else {
                alerta(resources.getString("borrar.deporte.fallo"));
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
