package es.juliogtrenard.proyectoolimpiadas.controladores;

import es.juliogtrenard.proyectoolimpiadas.dao.DaoDeportista;
import es.juliogtrenard.proyectoolimpiadas.dao.DaoEquipo;
import es.juliogtrenard.proyectoolimpiadas.dao.DaoEvento;
import es.juliogtrenard.proyectoolimpiadas.dao.DaoParticipacion;
import es.juliogtrenard.proyectoolimpiadas.modelos.Deportista;
import es.juliogtrenard.proyectoolimpiadas.modelos.Equipo;
import es.juliogtrenard.proyectoolimpiadas.modelos.Evento;
import es.juliogtrenard.proyectoolimpiadas.modelos.Participacion;
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
 * Clase que controla la ventana participación
 */
public class ControladorParticipacion implements Initializable {
    /**
     * Una participacion
     */
    private Participacion participacion;

    /**
     * Listado de deportistas
     */
    @FXML
    private ListView<Deportista> lstDeportista;

    /**
     * Listado de equipos
     */
    @FXML
    private ListView<Equipo> lstEquipo;

    /**
     * Listado de eventos
     */
    @FXML
    private ListView<Evento> lstEvento;

    /**
     * Campo de texto para la edad del deportista
     */
    @FXML
    private TextField txtEdad;

    /**
     * Campo de texto para la medalla de la participacion
     */
    @FXML
    private TextField txtMedalla;

    /**
     * Recursos a usar
     */
    @FXML
    private ResourceBundle resources;

    /**
     * Función que se ejecuta cuando se inicia la ventana
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        cargarListas();
        if (this.participacion != null) {
            lstDeportista.getSelectionModel().select(participacion.getDeportista());
            lstDeportista.setDisable(true);
            lstEvento.getSelectionModel().select(participacion.getEvento());
            lstEvento.setDisable(true);
            lstEquipo.getSelectionModel().select(participacion.getEquipo());
            txtEdad.setText(participacion.getEdad() + "");
            txtMedalla.setText(participacion.getMedalla());
        }
    }

    /**
     * Carga las listas
     */
    public void cargarListas() {
        ObservableList<Deportista> deportistas = DaoDeportista.cargarListado();
        lstDeportista.getItems().addAll(deportistas);
        ObservableList<Evento> eventos = DaoEvento.cargarListado();
        lstEvento.getItems().addAll(eventos);
        ObservableList<Equipo> equipos = DaoEquipo.cargarListado();
        lstEquipo.getItems().addAll(equipos);
    }

    /**
     * Cierra la ventana
     *
     * @param event El evento que activa el metodo
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage)txtEdad.getScene().getWindow();
        stage.close();
    }

    /**
     * Valida y procesa los datos
     *
     * @param event El evento que activa el metodo
     */
    @FXML
    void guardar(ActionEvent event) {
        String error = validar();
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Participacion nuevo = new Participacion();
            nuevo.setDeportista(lstDeportista.getSelectionModel().getSelectedItem());
            nuevo.setEvento(lstEvento.getSelectionModel().getSelectedItem());
            nuevo.setEquipo(lstEquipo.getSelectionModel().getSelectedItem());
            nuevo.setEdad(Integer.parseInt(txtEdad.getText()));
            nuevo.setMedalla(txtMedalla.getText());
            if (this.participacion == null) {
                if (DaoParticipacion.insertar(nuevo)) {
                    confirmacion(resources.getString("guardar.participacion"));
                    Stage stage = (Stage)txtEdad.getScene().getWindow();
                    stage.close();
                } else {
                    alerta(resources.getString("guardar.error"));
                }
            } else {
                if (DaoParticipacion.modificar(participacion, nuevo)) {
                    confirmacion(resources.getString("actualizar.participacion"));
                    Stage stage = (Stage)txtEdad.getScene().getWindow();
                    stage.close();
                } else {
                    alerta(resources.getString("guardar.error"));
                }
            }
        }
    }

    /**
     * Valida los datos introducidos por el usuario
     *
     * @return String Posibles errores
     */
    public String validar() {
        String error = "";
        if (lstDeportista.getSelectionModel().getSelectedItem() == null) {
            error = resources.getString("validar.participacion.deportista") + "\n";
        }
        if (lstEvento.getSelectionModel().getSelectedItem() == null) {
            error += resources.getString("validar.participacion.evento") + "\n";
        }
        if (lstEquipo.getSelectionModel().getSelectedItem() == null) {
            error += resources.getString("validar.participacion.equipo") + "\n";
        }
        if (txtEdad.getText().isEmpty()) {
            error += resources.getString("validar.participacion.edad") + "\n";
        } else {
            try {
                Integer.parseInt(txtEdad.getText());
            } catch (NumberFormatException e) {
                error += resources.getString("validar.participacion.edad.num") + "\n";
            }
        }
        if (txtMedalla.getText().isEmpty()) {
            error += resources.getString("validar.participacion.medalla") + "\n";
        } else {
            if (txtMedalla.getText().length() > 6) {
                error += resources.getString("validar.participacion.medalla.num") + "\n";
            }
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