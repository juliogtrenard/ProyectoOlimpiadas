package es.juliogtrenard.proyectoolimpiadas.lenguaje;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Clase dedicada al cambio de idioma
 */
public class LenguajeSwitcher {
    private Stage stage;

    /**
     * Constructor de la clase
     *
     * @param stage de la aplicación
     */
    public LenguajeSwitcher(Stage stage) {
        this.stage = stage;
    }

    /**
     * Cambia el idioma de la aplicación
     *
     * @param locale nuevo locale
     */
    public void switchLenguaje(Locale locale) {
        // Update the locale in the LenguajeManager
        LenguajeManager.getInstance().setLocale(locale);
        // Get the updated ResourceBundle
        ResourceBundle bundle = LenguajeManager.getInstance().getBundle();
        try {
            // Reload the FXML with the new ResourceBundle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/principal.fxml"), bundle);
            Parent root = loader.load();
            stage.setTitle(bundle.getString("app.nombre"));
            // Update the scene with the new root (new language)
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
