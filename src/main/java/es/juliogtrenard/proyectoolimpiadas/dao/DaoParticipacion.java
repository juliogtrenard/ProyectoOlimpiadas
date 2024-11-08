package es.juliogtrenard.proyectoolimpiadas.dao;

import es.juliogtrenard.proyectoolimpiadas.db.DBConnect;
import es.juliogtrenard.proyectoolimpiadas.modelos.Deportista;
import es.juliogtrenard.proyectoolimpiadas.modelos.Equipo;
import es.juliogtrenard.proyectoolimpiadas.modelos.Evento;
import es.juliogtrenard.proyectoolimpiadas.modelos.Participacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase donde se ejecuta las consultas para la tabla Participacion
 */
public class DaoParticipacion {
    /**
     * Carga los datos de la tabla Participacion y los devuelve para usarlos en un listado de participaciones
     *
     * @return listado Lista de participaciones para cargar en un tableview
     */
    public static ObservableList<Participacion> cargarListado() {
        DBConnect connection;
        ObservableList<Participacion> participaciones = FXCollections.observableArrayList();
        try{
            connection = new DBConnect();
            String consulta = "SELECT id_deportista,id_evento,id_equipo,edad,medalla FROM Participacion";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_deportista = rs.getInt("id_deportista");
                Deportista deportista = DaoDeportista.getDeportista(id_deportista);
                int id_evento = rs.getInt("id_evento");
                Evento evento = DaoEvento.getEvento(id_evento);
                int id_equipo = rs.getInt("id_equipo");
                Equipo equipo = DaoEquipo.getEquipo(id_equipo);
                int edad = rs.getInt("edad");
                String medalla = rs.getString("medalla");
                Participacion participacion = new Participacion(deportista,evento,equipo,edad,medalla);
                participaciones.add(participacion);
            }
            rs.close();
            connection.closeConnection();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return participaciones;
    }

    /**
     * Modifica los datos de una participacion
     *
     * @param participacion Participacion con sus datos
     * @param participacionNuevo Nuevos datos de la participacion a modificar
     * @return True -> modificada, False -> no modificada
     */
    public static boolean modificar(Participacion participacion, Participacion participacionNuevo) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "UPDATE Participacion SET id_deportista = ?,id_evento = ?,id_equipo = ?,edad = ?,medalla = ? WHERE id_deportista = ? AND id_evento = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, participacionNuevo.getDeportista().getId_deportista());
            pstmt.setInt(2, participacionNuevo.getEvento().getId_evento());
            pstmt.setInt(3, participacionNuevo.getEquipo().getId_equipo());
            pstmt.setInt(4, participacionNuevo.getEdad());
            pstmt.setString(5, participacionNuevo.getMedalla());
            pstmt.setInt(6, participacion.getDeportista().getId_deportista());
            pstmt.setInt(7, participacion.getEvento().getId_evento());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Crea una nueva participacion
     *
     * @param participacion Participacion con datos nuevos
     * @return True -> Se inserta, False -> No se inserta
     */
    public static boolean insertar(Participacion participacion) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "INSERT INTO Participacion (id_deportista,id_evento,id_equipo,edad,medalla) VALUES (?,?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, participacion.getDeportista().getId_deportista());
            pstmt.setInt(2, participacion.getEvento().getId_evento());
            pstmt.setInt(3, participacion.getEquipo().getId_equipo());
            pstmt.setInt(4, participacion.getEdad());
            pstmt.setString(5, participacion.getMedalla());
            int filasAfectadas = pstmt.executeUpdate();
            return (filasAfectadas > 0);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una participacion
     *
     * @param participacion Participacion a eliminar
     * @return True -> Se elimina, False -> No se elimina
     */
    public static boolean eliminar(Participacion participacion) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "DELETE FROM Participacion WHERE id_deportista = ? AND id_evento = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, participacion.getDeportista().getId_deportista());
            pstmt.setInt(2, participacion.getEvento().getId_evento());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}