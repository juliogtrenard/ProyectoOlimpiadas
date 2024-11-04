package es.juliogtrenard.proyectoolimpiadas.controladores;

import es.juliogtrenard.proyectoolimpiadas.dao.DaoOlimpiada;
import es.juliogtrenard.proyectoolimpiadas.modelos.Olimpiada;
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
 * Clase que controla los eventos de la ventana olimpiadas
 */
public class ControladorOlimpiada implements Initializable {
    /**
     * La olimpiada
     */
    private Olimpiada olimpiada;

    /**
     * La olimpiada a crear
     */
    private Olimpiada crear;

    /**
     * Botón para eliminar
     */
    @FXML
    private Button btnEliminar;

    /**
     * ComboBox de olimpiadas
     */
    @FXML
    private ComboBox<Olimpiada> cbOlimpiada;

    /**
     * RadioButton para la temporada de invierno
     */
    @FXML
    private RadioButton rbInvierno;

    /**
     * RadioButton para la temporada de verano
     */
    @FXML
    private RadioButton rbVerano;

    /**
     * TextField para el año
     */
    @FXML
    private TextField txtAnio;

    /**
     * TextField para la ciudad
     */
    @FXML
    private TextField txtCiudad;

    /**
     * TextField para el nombre de la olimpiada
     */
    @FXML
    private TextField txtNombre;

    /**
     * Recursos a utilizar
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
        this.olimpiada = null;
        btnEliminar.setDisable(true);
        crear = new Olimpiada();
        crear.setId_olimpiada(0);
        crear.setNombre(resources.getString("cb.nuevo"));
        cargarOlimpiadas();
        cbOlimpiada.getSelectionModel().selectedItemProperty().addListener(this::cambioOlimpiada);
    }

    /**
     * Carga las olimpiadas de la base de datos al ComboBox
     */
    public void cargarOlimpiadas() {
        cbOlimpiada.getItems().clear();
        cbOlimpiada.getItems().add(crear);
        ObservableList<Olimpiada> olimpiadas = DaoOlimpiada.cargarListado();
        cbOlimpiada.getItems().addAll(olimpiadas);
        cbOlimpiada.getSelectionModel().select(0);
    }

    /**
     * Listener del cambio del ComboBox
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    public void cambioOlimpiada(ObservableValue<? extends Olimpiada> observable, Olimpiada oldValue, Olimpiada newValue) {
        if (newValue != null) {
            btnEliminar.setDisable(true);
            if (newValue.equals(crear)) {
                olimpiada = null;
                txtNombre.setText(null);
                txtAnio.setText(null);
                rbInvierno.setSelected(true);
                rbVerano.setSelected(false);
                txtCiudad.setText(null);
            } else {
                olimpiada = newValue;
                txtNombre.setText(olimpiada.getNombre());
                txtAnio.setText(olimpiada.getAnio() + "");
                if (olimpiada.getTemporada().equals("Winter")) {
                    rbInvierno.setSelected(true);
                    rbVerano.setSelected(false);
                } else {
                    rbVerano.setSelected(true);
                    rbInvierno.setSelected(false);
                }
                txtCiudad.setText(olimpiada.getCiudad());
                if (DaoOlimpiada.esEliminable(olimpiada)) {
                    btnEliminar.setDisable(false);
                }
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
     * Valida y procesa los datos
     *
     * @param event
     */
    @FXML
    void guardar(ActionEvent event) {
        String error = validar();
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Olimpiada nuevo = new Olimpiada();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setAnio(Integer.parseInt(txtAnio.getText()));
            if (rbInvierno.isSelected()) {
                nuevo.setTemporada(nuevo.getSeasonCategory("Winter"));
            } else {
                nuevo.setTemporada(nuevo.getSeasonCategory("Summer"));
            }
            nuevo.setCiudad(txtCiudad.getText());
            if (this.olimpiada == null) {
                int id = DaoOlimpiada.insertar(nuevo);
                if (id == -1) {
                    alerta(resources.getString("guardar.error"));
                } else {
                    confirmacion(resources.getString("guardar.olimpiada"));
                    cargarOlimpiadas();
                }
            } else {
                if (DaoOlimpiada.modificar(this.olimpiada, nuevo)) {
                    confirmacion(resources.getString("actualizar.olimpiada"));
                    cargarOlimpiadas();
                } else {
                    alerta(resources.getString("guardar.error"));
                }
            }
        }
    }

    /**
     * Elimina una olimpiada
     *
     * @param event
     */
    @FXML
    void eliminar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(txtNombre.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle(resources.getString("ventana.confirmar"));
        alert.setContentText(resources.getString("borrar.olimpiada"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (DaoOlimpiada.eliminar(olimpiada)) {
                confirmacion(resources.getString("borrar.olimpiada.bien"));
                cargarOlimpiadas();
            } else {
                alerta(resources.getString("borrar.olimpiada.fallo"));
            }
        }
    }

    /**
     * Valida los datos introducidos por el usuario
     *
     * @return String Los errores
     */
    public String validar() {
        String error = "";
        if (txtNombre.getText().isEmpty()) {
            error = resources.getString("validar.olimpiada.nombre") + "\n";
        }
        if (txtAnio.getText().isEmpty()) {
            error += resources.getString("validar.olimpiada.anio") + "\n";
        } else {
            try {
                Integer.parseInt(txtAnio.getText());
            } catch (NumberFormatException e) {
                error += resources.getString("validar.olimpiada.anio.num") + "\n";
            }
        }
        if (txtCiudad.getText().isEmpty()) {
            error += resources.getString("validar.olimpiada.ciudad") + "\n";
        }
        return error;
    }

    /**
     * Muestra un mensaje de alerta
     *
     * @param texto Contenido de la alerta
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