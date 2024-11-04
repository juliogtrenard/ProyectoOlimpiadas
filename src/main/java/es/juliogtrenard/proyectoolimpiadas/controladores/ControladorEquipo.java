package es.juliogtrenard.proyectoolimpiadas.controladores;

import es.juliogtrenard.proyectoolimpiadas.dao.DaoEquipo;
import es.juliogtrenard.proyectoolimpiadas.modelos.Equipo;
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
 * Clase para controlar los equipos
 */
public class ControladorEquipo implements Initializable {
    /**
     * El equipo
     */
    private Equipo equipo;

    /**
     * Crear el equipo
     */
    private Equipo crear;

    /**
     * Boton para eliminar un equipo
     */
    @FXML
    private Button btnEliminar;

    /**
     * ComboBox con los equipos.
     */
    @FXML
    private ComboBox<Equipo> cbEquipo;

    /**
     * Iniciales del equipo.
     */
    @FXML
    private TextField txtIniciales;

    /**
     * Campo de texto para el nombre del equipo.
     */
    @FXML
    private TextField txtNombre;

    /**
     * Recursos a usar
     */
    @FXML
    private ResourceBundle resources;

    /**
     * Cuando se inicia la ventana
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        this.equipo = null;
        crear = new Equipo();
        crear.setId_equipo(0);
        crear.setNombre(resources.getString("cb.nuevo"));
        cargarEquipos();
        cbEquipo.getSelectionModel().selectedItemProperty().addListener(this::cambioEquipo);
    }

    /**
     * Carga los equipos de la base de datos al ComboBox
     */
    public void cargarEquipos() {
        cbEquipo.getItems().clear();
        cbEquipo.getItems().add(crear);
        ObservableList<Equipo> equipos = DaoEquipo.cargarListado();
        cbEquipo.getItems().addAll(equipos);
        cbEquipo.getSelectionModel().select(0);
    }

    /**
     * Listener del cambio del ComboBox
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    public void cambioEquipo(ObservableValue<? extends Equipo> observable, Equipo oldValue, Equipo newValue) {
        if (newValue != null) {
            btnEliminar.setDisable(true);
            if (newValue.equals(crear)) {
                equipo = null;
                txtNombre.setText(null);
                txtIniciales.setText(null);
            } else {
                equipo = newValue;
                txtNombre.setText(equipo.getNombre());
                txtIniciales.setText(equipo.getIniciales());
                if (DaoEquipo.esEliminable(equipo)) {
                    btnEliminar.setDisable(false);
                }
            }
        }
    }

    /**
     * Valida y procesa los datos
     *
     * @param event
     */
    @FXML
    void guardar(ActionEvent event) {
        String error = "";
        if (txtNombre.getText().isEmpty()) {
            error = resources.getString("validar.equipo.nombre") + "\n";
        }
        if (txtIniciales.getText().isEmpty()) {
            error +=  resources.getString("validar.equipo.ini") + "\n";
        } else {
            if (txtIniciales.getText().length() > 3) {
                error +=  resources.getString("validar.equipo.ini.num") +  "\n";
            }
        }
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Equipo nuevo = new Equipo();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setIniciales(txtIniciales.getText());

            if (this.equipo == null) {
                int id = DaoEquipo.insertar(nuevo);

                if (id == -1) {
                    alerta(resources.getString("guardar.error"));
                } else {
                    confirmacion(resources.getString("guardar.equipo"));
                    cargarEquipos();
                }
            } else {
                if (DaoEquipo.modificar(equipo, nuevo)) {
                    confirmacion(resources.getString("actualizar.equipo"));
                    cargarEquipos();
                } else {
                    alerta(resources.getString("guardar.error"));
                }
            }
        }
    }

    /**
     * Elimina un equipo
     *
     * @param event
     */
    @FXML
    void eliminar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(txtNombre.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle(resources.getString("ventana.confirmar"));
        alert.setContentText(resources.getString("borrar.equipo"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            if (DaoEquipo.eliminar(equipo)) {
                confirmacion(resources.getString("borrar.equipo.bien"));
                cargarEquipos();
            } else {
                alerta(resources.getString("borrar.equipo.fallo"));
            }
        }
    }

    /**
     * Cierra la ventana
     *
     * @param event
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage)txtNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra un mensaje de alerta
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
     * Muestra un mensaje de confirmación
     *
     * @param texto Contenido del mensaje
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}
