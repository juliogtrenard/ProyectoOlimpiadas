package es.juliogtrenard.proyectoolimpiadas;

import es.juliogtrenard.proyectoolimpiadas.lenguaje.LenguajeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Ejecuta la aplicación
 *
 * @author Julio González
 */
public class Olimpiadas extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ResourceBundle bundle = LenguajeManager.getInstance().getBundle();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/principal.fxml"), bundle);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle(bundle.getString("app.nombre"));
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setMaxWidth(800);
        stage.setMaxHeight(600);
        stage.show();
    }

    /**
     * Punto de entrada principal
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch();
    }
}