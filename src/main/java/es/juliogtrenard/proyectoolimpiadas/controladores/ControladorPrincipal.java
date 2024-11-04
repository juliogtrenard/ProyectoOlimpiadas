package es.juliogtrenard.proyectoolimpiadas.controladores;

import es.juliogtrenard.proyectoolimpiadas.dao.*;
import es.juliogtrenard.proyectoolimpiadas.lenguaje.LenguajeSwitcher;
import es.juliogtrenard.proyectoolimpiadas.modelos.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
     * Mirar tabla deportistas
     */
    @FXML
    private MenuItem tDeportistas;

    /**
     * Mirar tabla equipos
     */
    @FXML
    private MenuItem tEquipos;

    /**
     * Mirar tabla deportes
     */
    @FXML
    private MenuItem tDeportes;

    /**
     * Mirar tabla olimpiadas
     */
    @FXML
    private MenuItem tOlimpiadas;

    /**
     * Mirar tabla eventos
     */
    @FXML
    private MenuItem tEventos;

    /**
     * Mirar tabla participaciones
     */
    @FXML
    private MenuItem tParticipaciones;

    /**
     * Controlar qué tabla se está visualizando
     */
    private String controladorTabla;

    /**
     * Label del titulo de la tabla
     */
    @FXML
    private Label lblTitulo;

    /**
     * ResourceBundle de la aplicación
     */
    @FXML
    private ResourceBundle resources;

    /**
     * Guardar los datos con los que se trabaja
     */
    private ObservableList masterData = FXCollections.observableArrayList();
    private ObservableList filteredData = FXCollections.observableArrayList();

    /**
     * Se ejecuta cuando se inicia la aplicación
     *
     * @param url url
     * @param resourceBundle recursos
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        controladorTabla = "deportistas";
        lblTitulo.setText(resources.getString("titulo.tabla.deportistas"));

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
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && controladorTabla.equals("deportistas")) {
                editar(null);
            }
        });

        tDeportistas.setOnAction(_ -> {
            controladorTabla = "deportistas";
            lblTitulo.setText(resources.getString("titulo.tabla.deportistas"));
            cargarDeportistas();
        });

        tEventos.setOnAction(_ -> {
            controladorTabla = "eventos";
            lblTitulo.setText(resources.getString("titulo.tabla.eventos"));
            cargarEventos();
        });

        tParticipaciones.setOnAction(_ -> {
            controladorTabla = "participaciones";
            lblTitulo.setText(resources.getString("titulo.tabla.participaciones"));
            cargarParticipaciones();
        });

        tEquipos.setOnAction(_ -> {
            controladorTabla = "equipos";
            lblTitulo.setText(resources.getString("titulo.tabla.equipos"));
            cargarEquipos();
        });

        tDeportes.setOnAction(_ -> {
            controladorTabla = "deportes";
            lblTitulo.setText(resources.getString("titulo.tabla.deportes"));
            cargarDeportes();
        });

        tOlimpiadas.setOnAction(_ -> {
            controladorTabla = "olimpiadas";
            lblTitulo.setText(resources.getString("titulo.tabla.olimpiadas"));
            cargarOlimpiadas();
        });

        cargarDeportistas();
    }

    /**
     * Agrega en la tabla las columnas de las olimpiadas
     */
    private void cargarOlimpiadas() {
        tabla.getSelectionModel().clearSelection();
        filtroNombre.setText(null);
        masterData.clear();
        filteredData.clear();
        tabla.getItems().clear();
        tabla.getColumns().clear();

        TableColumn<Olimpiada, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id_olimpiada"));
        TableColumn<Olimpiada, String> colNombre = new TableColumn<>(resources.getString("tabla.olimpiada.nombre"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Olimpiada, Integer> colAnio = new TableColumn<>(resources.getString("tabla.olimpiada.anio"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        TableColumn<Olimpiada, String> colTemporada = new TableColumn<>(resources.getString("tabla.olimpiada.temporada"));
        colTemporada.setCellValueFactory(new PropertyValueFactory<>("temporada"));
        TableColumn<Olimpiada, String> colCiudad = new TableColumn<>(resources.getString("tabla.olimpiada.ciudad"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        tabla.getColumns().addAll(colId,colNombre,colAnio,colTemporada,colCiudad);

        ObservableList<Olimpiada> olimpiadas = DaoOlimpiada.cargarListado();
        masterData.addAll(olimpiadas);
        tabla.setItems(olimpiadas);
    }

    /**
     * Agrega en la tabla las columnas de los deportes
     */
    private void cargarDeportes() {
        tabla.getSelectionModel().clearSelection();
        filtroNombre.setText(null);
        masterData.clear();
        filteredData.clear();
        tabla.getItems().clear();
        tabla.getColumns().clear();

        TableColumn<Deporte, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id_deporte"));
        TableColumn<Deporte, String> colNombre = new TableColumn<>(resources.getString("label.nombre"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tabla.getColumns().addAll(colId,colNombre);

        ObservableList<Deporte> deportes = DaoDeporte.cargarListado();
        masterData.setAll(deportes);
        tabla.setItems(deportes);
    }

    /**
     * Agrega en la tabla las columnas de los equipos
     */
    private void cargarEquipos() {
        tabla.getSelectionModel().clearSelection();
        filtroNombre.setText(null);
        masterData.clear();
        filteredData.clear();
        tabla.getItems().clear();
        tabla.getColumns().clear();

        TableColumn<Equipo, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id_equipo"));
        TableColumn<Equipo, String> colNombre = new TableColumn<>(resources.getString("label.nombre"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Equipo, String> colIniciales = new TableColumn<>(resources.getString("label.iniciales"));
        colIniciales.setCellValueFactory(new PropertyValueFactory<>("iniciales"));
        tabla.getColumns().addAll(colId,colNombre,colIniciales);

        ObservableList<Equipo> equipos = DaoEquipo.cargarListado();
        masterData.setAll(equipos);
        tabla.setItems(equipos);
    }

    /**
     * Abre la ventana de deportistas
     *
     * @param event El evento
     */
    @FXML
    void deportistas(ActionEvent event) {
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
     * Abre la ventana de equipos
     *
     * @param event El evento
     */
    @FXML
    void equipos(ActionEvent event) {
        try {
            Window ventana = tabla.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/equipo.fxml"),resources);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(resources.getString("app.nombre"));
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            alerta(resources.getString("error.ventana"));
        }
    }

    /**
     * Abre la ventana de deportes
     *
     * @param event El evento
     */
    @FXML
    void deportes(ActionEvent event) {
        try {
            Window ventana = tabla.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/deporte.fxml"),resources);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(resources.getString("app.nombre"));
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            alerta(resources.getString("error.ventana"));
        }
    }

    /**
     * Abre la ventana de olimpiadas
     *
     * @param event El evento
     */
    @FXML
    void olimpiadas(ActionEvent event) {
        try {
            Window ventana = tabla.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/olimpiada.fxml"),resources);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(resources.getString("app.nombre"));
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            alerta(resources.getString("error.ventana"));
        }
    }

    /**
     * Abre la ventana de Eventos
     *
     * @param event El evento
     */
    @FXML
    void eventos(ActionEvent event) {
        try {
            Window ventana = tabla.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/evento.fxml"),resources);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(resources.getString("app.nombre"));
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            alerta(resources.getString("error.ventana"));
        }
    }

    /**
     * Abre la ventana de Eventos
     *
     * @param event El evento
     */
    @FXML
    void participaciones(ActionEvent event) {
        try {
            Window ventana = tabla.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/participacion.fxml"),resources);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(resources.getString("app.nombre"));
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            alerta(resources.getString("error.ventana"));
        }
    }

    /**
     * Añadir datos a una tabla
     * @param actionEvent El evento de la acción
     */
    @FXML
    public void aniadir(ActionEvent actionEvent) {
        switch(controladorTabla) {
            case "deportistas":
                deportistas(actionEvent);
                cargarDeportistas();
                break;
            case "eventos":
                eventos(actionEvent);
                cargarEventos();
                break;
            case "participaciones":
                participaciones(actionEvent);
                cargarParticipaciones();
                break;
            case "olimpiadas":
                olimpiadas(actionEvent);
                cargarOlimpiadas();
                break;
            case "deportes":
                deportes(actionEvent);
                cargarDeportes();
                break;
            case "equipos":
                equipos(actionEvent);
                cargarEquipos();
                break;
        }
    }

    /**
     * Eliminar datos de una tabla
     *
     * @param event El evento del metodo
     */
    @FXML
    void eliminar(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setHeaderText(null);

        if(tabla.getSelectionModel().getSelectedItem() != null) {
            alerta.setContentText((resources.getString("eliminar.confirmar")));
            alerta.showAndWait();
            if(alerta.getResult().getButtonData().name().equals("OK_DONE")) {
                switch (controladorTabla) {
                    case "deportistas":
                        DaoDeportista.eliminar((Deportista) tabla.getSelectionModel().getSelectedItem());
                        cargarDeportistas();
                        tabla.refresh();
                        break;
                    case "eventos":
                        DaoEvento.eliminar((Evento) tabla.getSelectionModel().getSelectedItem());
                        cargarEventos();
                        tabla.refresh();
                        break;
                    case "participaciones":
                        DaoParticipacion.eliminar((Participacion) tabla.getSelectionModel().getSelectedItem());
                        cargarParticipaciones();
                        tabla.refresh();
                        break;
                    case "olimpiadas":
                        DaoOlimpiada.eliminar((Olimpiada) tabla.getSelectionModel().getSelectedItem());
                        cargarOlimpiadas();
                        tabla.refresh();
                        break;
                    case "deportes":
                        DaoDeporte.eliminar((Deporte) tabla.getSelectionModel().getSelectedItem());
                        cargarDeportes();
                        tabla.refresh();
                        break;
                    case "equipos":
                        DaoEquipo.eliminar((Equipo) tabla.getSelectionModel().getSelectedItem());
                        cargarEquipos();
                        tabla.refresh();
                        break;
                }
            }
        } else {
            alerta.setAlertType(Alert.AlertType.ERROR);
            alerta.setContentText((resources.getString("eliminar.error")));
            alerta.showAndWait();
        }

        tabla.getSelectionModel().clearSelection();
    }

    /**
     * Agrega en la tabla las columnas del deportista
     */
    private void cargarDeportistas() {
        tabla.getSelectionModel().clearSelection();
        filtroNombre.setText(null);
        filtroNombre.setDisable(false);
        masterData.clear();
        filteredData.clear();
        tabla.getItems().clear();
        tabla.getColumns().clear();

        TableColumn<Deportista, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id_deportista"));
        TableColumn<Deportista, String> colNombre = new TableColumn<>(resources.getString("label.nombre"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Deportista, Deportista.SexCategory> colSexo = new TableColumn<>(resources.getString("label.sexo"));
        colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        TableColumn<Deportista, Integer> colPeso = new TableColumn<>(resources.getString("label.peso"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        TableColumn<Deportista, Integer> colAltura = new TableColumn<>(resources.getString("label.altura"));
        colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        tabla.getColumns().addAll(colId,colNombre,colSexo,colPeso,colAltura);

        ObservableList<Deportista> deportistas = DaoDeportista.cargarListado();
        masterData.setAll(deportistas);
        tabla.setItems(deportistas);
    }

    /**
     * Agrega en la tabla las columnas de eventos
     */
    private void cargarEventos() {
        tabla.getSelectionModel().clearSelection();
        filtroNombre.setText(null);
        masterData.clear();
        filteredData.clear();
        tabla.getItems().clear();
        tabla.getColumns().clear();

        TableColumn<Evento, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id_evento"));
        TableColumn<Evento, String> colNombre = new TableColumn<>(resources.getString("tabla.evento.nombre"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Evento, String> colOlimpiada = new TableColumn<>(resources.getString("tabla.evento.olimpiada"));
        colOlimpiada.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getOlimpiada().getNombre()));
        TableColumn<Evento, String> colDeporte = new TableColumn<>(resources.getString("tabla.evento.deporte"));
        colDeporte.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getDeporte().getNombre()));
        tabla.getColumns().addAll(colId,colNombre,colOlimpiada,colDeporte);

        ObservableList<Evento> eventos = DaoEvento.cargarListado();
        masterData.setAll(eventos);
        tabla.setItems(eventos);
    }

    /**
     * Agrega en la tabla las columnas de participaciones
     */
    private void cargarParticipaciones() {
        tabla.getSelectionModel().clearSelection();
        filtroNombre.setText(null);
        filtroNombre.setDisable(true);
        masterData.clear();
        filteredData.clear();
        tabla.getItems().clear();
        tabla.getColumns().clear();

        TableColumn<Participacion, String> colDeportista = new TableColumn<>(resources.getString("tabla.participacion.deportista"));
        colDeportista.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getDeportista().getNombre()));
        TableColumn<Participacion, String> colEvento = new TableColumn<>(resources.getString("tabla.participacion.evento"));
        colEvento.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getEvento().getNombre()));
        TableColumn<Participacion, String> colEquipo = new TableColumn<>(resources.getString("tabla.participacion.equipo"));
        colEquipo.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getEquipo().getNombre()));
        TableColumn<Participacion, Integer> colEdad = new TableColumn<>(resources.getString("tabla.participacion.edad"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        TableColumn<Participacion, String> colMedalla = new TableColumn<>(resources.getString("tabla.participacion.medalla"));
        colMedalla.setCellValueFactory(new PropertyValueFactory<>("medalla"));
        tabla.getColumns().addAll(colDeportista,colEvento,colEquipo,colEdad,colMedalla);

        ObservableList<Participacion> participaciones = DaoParticipacion.cargarListado();
        masterData.addAll(participaciones);
        tabla.setItems(participaciones);
    }

    /**
     * Abre la ventana para la edición del deportista cuando se hace doble clic
     *
     * @param event El evento
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
                elegirFiltro(valor);
                tabla.setItems(filteredData);
            }
        }
    }

    /**
     * Determina el filtro de la tabla
     *
     * @param valor El texto escrito
     */
    private void elegirFiltro(String valor) {
        String valorBuscado = valor.toLowerCase();
        for (Object obj : masterData) {
            if (obj instanceof Deportista) {
                filtrarDeportista((Deportista) obj, valorBuscado);
            } else if (obj instanceof Evento) {
                filtrarEvento((Evento) obj, valorBuscado);
            } else if (obj instanceof Olimpiada) {
                filtrarOlimpiada((Olimpiada) obj, valorBuscado);
            } else if (obj instanceof Deporte) {
                filtrarDeporte((Deporte) obj, valorBuscado);
            } else if (obj instanceof Equipo) {
                filtrarEquipo((Equipo) obj, valorBuscado);
            }
        }
    }

    /**
     * Filtrar los deportistas
     *
     * @param deportista Deportista
     * @param valor El valor a buscar
     */
    private void filtrarDeportista(Deportista deportista, String valor) {
        if (deportista.getNombre().toLowerCase().contains(valor)) {
            filteredData.add(deportista);
        }
    }

    /**
     * Filtrar los eventos
     *
     * @param evento Evento
     * @param valor El valor a buscar
     */
    private void filtrarEvento(Evento evento, String valor) {
        if (evento.getNombre().toLowerCase().contains(valor)) {
            filteredData.add(evento);
        }
    }

    /**
     * Filtrar las olimpiadas
     *
     * @param olimpiada Olimpiada
     * @param valor El valor a buscar
     */
    private void filtrarOlimpiada(Olimpiada olimpiada, String valor) {
        if (olimpiada.getNombre().toLowerCase().contains(valor)) {
            filteredData.add(olimpiada);
        }
    }

    /**
     * Filtrar los deportes
     *
     * @param deporte Deporte
     * @param valor El valor a buscar
     */
    private void filtrarDeporte(Deporte deporte, String valor) {
        if (deporte.getNombre().toLowerCase().contains(valor)) {
            filteredData.add(deporte);
        }
    }

    /**
     * Filtrar los equipos
     *
     * @param equipo Equipo
     * @param valor El valor a buscar
     */
    private void filtrarEquipo(Equipo equipo, String valor) {
        if (equipo.getNombre().toLowerCase().contains(valor)) {
            filteredData.add(equipo);
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