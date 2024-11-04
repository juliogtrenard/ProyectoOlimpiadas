package es.juliogtrenard.proyectoolimpiadas.controladores;

import es.juliogtrenard.proyectoolimpiadas.dao.DaoDeportista;
import es.juliogtrenard.proyectoolimpiadas.modelos.Deportista;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Clase que controla los eventos de la ventana deportista
 */
public class ControladorDeportista implements Initializable {
    /**
     * El deportista de la ventana
     */
    private Deportista deportista;

    /**
     * La imagen
     */
    private Blob imagen;

    /**
     * La propiedad de la imagen
     */
    @FXML
    private ImageView foto;

    /**
     * RadioButton para sexo Mujer
     */
    @FXML
    private RadioButton rbMujer;

    /**
     * RadioButton para sexo Hombre
     */
    @FXML
    private RadioButton rbHombre;

    /**
     * ToggleGroup para sexo
     */
    @FXML
    private ToggleGroup tgSexo;

    /**
     * Texto para ingresar la altura
     */
    @FXML
    private TextField txtAltura;

    /**
     * Texto para ingresar el nombre
     */
    @FXML
    private TextField txtNombre;

    /**
     * Texto para ingresar el peso
     */
    @FXML
    private TextField txtPeso;

    @FXML
    private ResourceBundle resources;

    /**
     * Constructor al que se pasa el deportista a editar
     *
     * @param deportista a editar
     */
    public ControladorDeportista(Deportista deportista) {
        this.deportista = deportista;
    }

    /**
     * Constructor vacío para añadir un nuevo deportista
     */
    public ControladorDeportista() {
        this.deportista = null;
    }

    /**
     * Función que se ejecuta cuando se inicia la ventana
     *
     * @param url url
     * @param resourceBundle Recursos
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        this.imagen = null;
        if (deportista != null) {
            txtNombre.setText(deportista.getNombre());
            if (deportista.getSexo() == 'F') {
                rbMujer.setSelected(true);
                rbHombre.setSelected(false);
            } else {
                rbHombre.setSelected(true);
                rbMujer.setSelected(false);
            }
            txtPeso.setText(deportista.getPeso() + "");
            txtAltura.setText(deportista.getAltura() + "");
            if (deportista.getFoto() != null) {
                this.imagen = deportista.getFoto();
                try {
                    InputStream imagen = deportista.getFoto().getBinaryStream();
                    foto.setImage(new Image(imagen));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
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
     * Guarda los datos del deportista.
     *
     * @param event El evento
     */
    @FXML
    void guardar(ActionEvent event) {
        String error = validar();
        if (!error.isEmpty()) {
            alerta(error);
            return;
        }

        Deportista deportistaNuevo = crearDeportista();

        if (this.deportista == null) {
            procesarNuevo(deportistaNuevo);
        } else {
            procesarExistente(deportistaNuevo);
        }
    }

    /**
     * Crea un nuevo Deportista con los datos actuales de la ventana
     *
     * @return Deportista nuevo
     */
    private Deportista crearDeportista() {
        Deportista deportistaNuevo = new Deportista();
        deportistaNuevo.setNombre(txtNombre.getText());
        deportistaNuevo.setSexo(rbMujer.isSelected() ? deportistaNuevo.getSexCategory('F') : deportistaNuevo.getSexCategory('M'));
        deportistaNuevo.setPeso(Integer.parseInt(txtPeso.getText()));
        deportistaNuevo.setAltura(Integer.parseInt(txtAltura.getText()));
        deportistaNuevo.setFoto(this.imagen);
        return deportistaNuevo;
    }

    /**
     * Procesa el nuevo deportista y lo inserta en la base de datos.
     *
     * @param deportistaNuevo Deportista nuevo a insertar
     */
    private void procesarNuevo(Deportista deportistaNuevo) {
        int id = DaoDeportista.insertar(deportistaNuevo);
        if (id == -1) {
            alerta(resources.getString("guardar.deportista.error"));
        } else {
            confirmacion(resources.getString("guardar.deportista"));
            cancelar(null);
        }
    }

    /**
     * Procesa el deportista existente y lo modifica en la base de datos.
     *
     * @param deportistaNuevo Deportista nuevo a modificar
     */
    private void procesarExistente(Deportista deportistaNuevo) {
        if (DaoDeportista.modificar(this.deportista, deportistaNuevo)) {
            confirmacion(resources.getString("actualizar.deportista"));
            cancelar(null);
        } else {
            alerta(resources.getString("guardar.deportista.error"));
        }
    }

    /**
     * Valida los datos del deportista a añadir
     *
     * @return string con posibles errores
     */
    private String validar() {
        String error = "";
        if (txtNombre.getText().isEmpty()) {
            error = resources.getString("validar.deportista.nombre") + "\n";
        }
        if (txtPeso.getText().isEmpty()) {
            error += resources.getString("validar.deportista.peso") + "\n";
        } else {
            try {
                Integer.parseInt(txtPeso.getText());
            } catch (NumberFormatException e) {
                error += resources.getString("validar.deportista.peso.nonum") + "\n";
            }
        }
        if (txtAltura.getText().isEmpty()) {
            error += resources.getString("validar.deportista.altura") + "\n";
        } else {
            try {
                Integer.parseInt(txtAltura.getText());
            } catch (NumberFormatException e) {
                error += resources.getString("validar.deportista.altura.nonum") + "\n";
            }
        }
        return error;
    }

    /**
     * Abre un FileChooser para seleccionar una imagen
     *
     * @param event El evento
     */
    @FXML
    void seleccionImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resources.getString("seleccionar.foto"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg", "*.jpeg","*.png"));
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(null);
        try {
            double kbs = (double) file.length() / 1024;
            if (kbs > 64) {
                alerta(resources.getString("seleccionar.foto.tamanio"));
            } else {
                InputStream imagen = new FileInputStream(file);
                Blob blob = DaoDeportista.convertFileToBlob(file);
                this.imagen = blob;
                foto.setImage(new Image(imagen));
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            alerta(resources.getString("seleccionar.foto.error"));
        }
    }

    /**
     * Muestra un mensaje de fallo
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
     * @param texto contenido de la alerta
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

}