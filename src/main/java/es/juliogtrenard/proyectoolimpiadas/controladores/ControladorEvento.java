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
     * @param url url
     * @param resourceBundle Recursos
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
     * Valida y procesa los datos del evento
     *
     * @param event El evento
     */
    @FXML
    void guardar(ActionEvent event) {
        String error = validarDatos();
        if (!error.isEmpty()) {
            alerta(error);
            return;
        }

        Evento nuevo = crearEvento();

        if (this.evento == null) {
            procesarNuevo(nuevo);
        } else {
            procesarExistente(nuevo);
        }
    }

    /**
     * Valida los datos del evento
     *
     * @return String con los errores encontrados
     */
    private String validarDatos() {
        StringBuilder error = new StringBuilder();
        if (txtNombre.getText().isEmpty()) {
            error.append(resources.getString("validar.evento.nombre")).append("\n");
        }
        if (lstOlimpiada.getSelectionModel().getSelectedItem() == null) {
            error.append(resources.getString("validar.evento.olimpiada")).append("\n");
        }
        if (lstDeporte.getSelectionModel().getSelectedItem() == null) {
            error.append(resources.getString("validar.evento.deporte")).append("\n");
        }
        return error.toString();
    }

    /**
     * Crea un nuevo evento a partir de los datos de la interfaz gráfica
     *
     * @return Evento con los datos del evento
     */
    private Evento crearEvento() {
        Evento nuevo = new Evento();
        nuevo.setNombre(txtNombre.getText());
        nuevo.setOlimpiada(lstOlimpiada.getSelectionModel().getSelectedItem());
        nuevo.setDeporte(lstDeporte.getSelectionModel().getSelectedItem());
        return nuevo;
    }

    /**
     * Procesa el evento nuevo y lo inserta en la base de datos, mostrando un mensaje de confirmacion o error
     *
     * @param nuevo Evento a insertar
     */
    private void procesarNuevo(Evento nuevo) {
        int id = DaoEvento.insertar(nuevo);
        if (id == -1) {
            alerta(resources.getString("guardar.error"));
        } else {
            confirmacion(resources.getString("guardar.evento"));
            cancelar(null);
        }
    }

    /**
     * Procesa el evento existente y lo actualiza en la base de datos, mostrando un mensaje de confirmacion o error
     *
     * @param nuevo Evento con los datos actualizados
     */
    private void procesarExistente(Evento nuevo) {
        if (DaoEvento.modificar(evento, nuevo)) {
            confirmacion(resources.getString("actualizar.evento"));
            cancelar(null);
        } else {
            alerta(resources.getString("guardar.error"));
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