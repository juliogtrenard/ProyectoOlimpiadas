package es.juliogtrenard.proyectoolimpiadas.lenguaje;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Clase que se encarga de manejar los idiomas
 */
public class LenguajeManager {
    private static LenguajeManager instance;
    private Locale locale = new Locale.Builder().setLanguage(LenguajeManager.getLanguage()).build();
    private ResourceBundle bundle;

    /**
     * Obtiene el idioma
     *
     * @return String con el idioma del fichero
     */
    public static String getLanguage() {
        HashMap<String,String> map = new HashMap<>();
        File f = new File("lang.properties");
        Properties properties;
        try {
            FileInputStream configFileReader=new FileInputStream(f);
            properties = new Properties();
            try {
                properties.load(configFileReader);
                configFileReader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("lang.properties not found at config file path " + f.getPath());
        }
        return properties.getProperty("language");
    }

    /**
     * Constructor de la clase que carga el bundle
     */
    private LenguajeManager() {
        loadResourceBundle();
    }

    /**
     * Crea una instancia de LenguajeManager y la devuelve
     *
     * @return instancia de LenguajeManager
     */
    public static LenguajeManager getInstance() {
        if (instance == null) {
            instance = new LenguajeManager();
        }
        return instance;
    }

    /**
     * Función que carga el bundle
     */
    private void loadResourceBundle() {
        bundle = ResourceBundle.getBundle("/lenguaje/lang", locale);
    }

    /**
     * Setter de locale
     *
     * @param locale nuevo
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        loadResourceBundle();
    }

    /**
     * Getter de bundle
     *
     * @return bundle
     */
    public ResourceBundle getBundle() {
        return bundle;
    }

    /**
     * Getter de locale
     *
     * @return locale
     */
    public Locale getLocale() {
        return locale;
    }

}
