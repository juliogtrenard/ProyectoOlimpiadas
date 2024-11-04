package es.juliogtrenard.proyectoolimpiadas.modelos;

/**
 * Clase para una participación
 */
public class Participacion {
    /**
     * Un deportista
     */
    private Deportista deportista;

    /**
     * Un evento
     */
    private Evento evento;

    /**
     * Un Equipo
     */
    private Equipo equipo;

    /**
     * La edad del deportista
     */
    private int edad;

    /**
     * La medalla de la participacion
     */
    private String medalla;

    /**
     * Constructor con parámetros para una participación
     *
     * @param deportista Deportista que participa
     * @param evento Evento de la participación
     * @param equipo Equipo que participa
     * @param edad Edad del deportista
     * @param medalla Medalla de la participación
     */
    public Participacion(Deportista deportista, Evento evento, Equipo equipo, int edad, String medalla) {
        this.deportista = deportista;
        this.evento = evento;
        this.equipo = equipo;
        this.edad = edad;
        this.medalla = medalla;
    }

    /**
     * Constructor vacío
     */
    public Participacion() {}

    /**
     * Getter para el deportista que participa
     *
     * @return deportista
     */
    public Deportista getDeportista() {
        return deportista;
    }

    /**
     * Setter para el deportista que participa
     *
     * @param deportista nuevo deportista que participa
     */
    public void setDeportista(Deportista deportista) {
        this.deportista = deportista;
    }

    /**
     * Getter para el evento de la participación
     *
     * @return evento
     */
    public Evento getEvento() {
        return evento;
    }

    /**
     * Setter para el evento de la participación
     *
     * @param evento nuevo evento de la participación
     */
    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    /**
     * Getter para el equipo que participa
     *
     * @return equipo
     */
    public Equipo getEquipo() {
        return equipo;
    }

    /**
     * Setter para el equipo que participa
     *
     * @param equipo nuevo equipo que participa
     */
    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    /**
     * Getter para la edad del deportista
     *
     * @return edad
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Setter para la edad del deportista
     *
     * @param edad nueva edad del deportista
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Getter para la medalla de la participación
     *
     * @return medalla
     */
    public String getMedalla() {
        return medalla;
    }

    /**
     * Setter para la medalla de la participación
     *
     * @param medalla nueva medalla de la participación
     */
    public void setMedalla(String medalla) {
        this.medalla = medalla;
    }

}
