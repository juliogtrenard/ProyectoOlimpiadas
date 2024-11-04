package es.juliogtrenard.proyectoolimpiadas.controladores;

import es.juliogtrenard.proyectoolimpiadas.dao.DaoDeporte;
import es.juliogtrenard.proyectoolimpiadas.dao.DaoEvento;
import es.juliogtrenard.proyectoolimpiadas.dao.DaoOlimpiada;
import es.juliogtrenard.proyectoolimpiadas.modelos.Deporte;
import es.juliogtrenard.proyectoolimpiadas.modelos.Evento;
import es.juliogtrenard.proyectoolimpiadas.modelos.Olimpiada;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clase que controla los eventos
 */
public class ControladorEvento implements Initializable {
    /**
     * El evento
     */
    private Evento evento;

    /**
     * Lista de deportes
     */
    @FXML
    private ListView<Deporte> lstDeporte;

    /**
     * Lista de olimpiadas
     */
    @FXML
    private ListView<Olimpiada> lstOlimpiada;

    /**
     * Campo de texto para el nombre de la olimpiada
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
        cargarListas();
        if (this.evento != null) {
            txtNombre.setText(evento.getNombre());
            lstOlimpiada.getSelectionModel().select(evento.getOlimpiada());
            lstDeporte.getSelectionModel().select(evento.getDeporte());
        }
    }

    /**
     * Carga las listas de olimpiadas y deportes
     */
    public void cargarListas() {
        ObservableList<Olimpiada> olimpiadas = DaoOlimpiada.cargarListado();
        lstOlimpiada.getItems().addAll(olimpiadas);
        ObservableList<Deporte> deportes = DaoDeporte.cargarListado();
        lstDeporte.getItems().addAll(deportes);
    }

    /**
     * Cierra la ventana
     *
     * @param event Evento que activa el metodo
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage)txtNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Valida y procesa los datos
     *
     * @param event Evento que activa el metodo
     */
    @FXML
    void guardar(ActionEvent event) {
        String error = "";
        if (txtNombre.getText().isEmpty()) {
            error = resources.getString("validar.evento.nombre") + "\n";
        }
        if (lstOlimpiada.getSelectionModel().getSelectedItem() == null) {
            error += resources.getString("validar.evento.olimpiada") + "\n";
        }
        if (lstDeporte.getSelectionModel().getSelectedItem() == null) {
            error += resources.getString("validar.evento.deporte") + "\n";
        }
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Evento nuevo = new Evento();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setOlimpiada(lstOlimpiada.getSelectionModel().getSelectedItem());
            nuevo.setDeporte(lstDeporte.getSelectionModel().getSelectedItem());
            if (this.evento == null) {
                int id = DaoEvento.insertar(nuevo);
                if (id == -1) {
                    alerta(resources.getString("guardar.error"));
                } else {
                    confirmacion(resources.getString("guardar.evento"));
                    Stage stage = (Stage)txtNombre.getScene().getWindow();
                    stage.close();
                }
            } else {
                if (DaoEvento.modificar(evento, nuevo)) {
                    confirmacion(resources.getString("actualizar.evento"));
                    Stage stage = (Stage)txtNombre.getScene().getWindow();
                    stage.close();
                } else {
                    alerta(resources.getString("guardar.error"));
                }
            }
        }
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