package es.juliogtrenard.proyectoolimpiadas.dao;

import es.juliogtrenard.proyectoolimpiadas.db.DBConnect;
import es.juliogtrenard.proyectoolimpiadas.modelos.Deporte;
import es.juliogtrenard.proyectoolimpiadas.modelos.Evento;
import es.juliogtrenard.proyectoolimpiadas.modelos.Olimpiada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase donde se ejecutan las consultas para la tabla Evento
 */
public class DaoEvento {
    /**
     * Busca un evento por su id
     *
     * @param id ID del evento a buscar
     * @return evento o null
     */
    public static Evento getEvento(int id) {
        DBConnect connection;
        Evento evento = null;
        try {
            connection = new DBConnect();
            String consulta = "SELECT id_evento,nombre,id_olimpiada,id_deporte FROM Evento WHERE id_evento = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_evento = rs.getInt("id_evento");
                String nombre = rs.getString("nombre");
                int id_olimpiada = rs.getInt("id_olimpiada");
                Olimpiada olimpiada = DaoOlimpiada.getOlimpiada(id_olimpiada);
                int id_deporte = rs.getInt("id_deporte");
                Deporte deporte = DaoDeporte.getDeporte(id_deporte);
                evento = new Evento(id_evento,nombre,olimpiada,deporte);
            }
            rs.close();
            connection.closeConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return evento;
    }

    /**
     * Busca un evento y verifica si se puede eliminar
     *
     * @param evento Evento a buscar
     * @return True -> Se puede eliminar, False -> No se puede eliminar
     */
    public static boolean esEliminable(Evento evento) {
        DBConnect connection;
        try {
            connection = new DBConnect();
            String consulta = "SELECT count(*) as cont FROM Participacion WHERE id_evento = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, evento.getId_evento());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int cont = rs.getInt("cont");
                rs.close();
                connection.closeConnection();
                return (cont==0);
            }
            rs.close();
            connection.closeConnection();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**
     * Carga los datos de la tabla Eventos y los devuelve para usarlos en un listado de eventos
     *
     * @return listado Lista de eventos para cargar en un tableview
     */
    public static ObservableList<Evento> cargarListado() {
        DBConnect connection;
        ObservableList<Evento> eventos = FXCollections.observableArrayList();
        try{
            connection = new DBConnect();
            String consulta = "SELECT id_evento,nombre,id_olimpiada,id_deporte FROM Evento";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_evento = rs.getInt("id_evento");
                String nombre = rs.getString("nombre");
                int id_olimpiada = rs.getInt("id_olimpiada");
                Olimpiada olimpiada = DaoOlimpiada.getOlimpiada(id_olimpiada);
                int id_deporte = rs.getInt("id_deporte");
                Deporte deporte = DaoDeporte.getDeporte(id_deporte);
                Evento evento = new Evento(id_evento,nombre,olimpiada,deporte);
                eventos.add(evento);
            }
            rs.close();
            connection.closeConnection();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return eventos;
    }

    /**
     * Modifica los datos de un evento
     *
     * @param evento Evento y sus datos
     * @param eventoNuevo Nuevos datos del evento a modificar
     * @return True -> Se modificó, False -> No se modificó
     */
    public static boolean modificar(Evento evento, Evento eventoNuevo) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "UPDATE Evento SET nombre = ?,id_olimpiada = ?,id_deporte = ? WHERE id_evento = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, eventoNuevo.getNombre());
            pstmt.setInt(2, eventoNuevo.getOlimpiada().getId_olimpiada());
            pstmt.setInt(3, eventoNuevo.getDeporte().getId_deporte());
            pstmt.setInt(4, evento.getId_evento());
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
     * Crea un nuevo evento
     *
     * @param evento Evento con sus datos
     * @return id/-1
     */
    public  static int insertar(Evento evento) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "INSERT INTO Evento (nombre,id_olimpiada,id_deporte) VALUES (?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, evento.getNombre());
            pstmt.setInt(2, evento.getOlimpiada().getId_olimpiada());
            pstmt.setInt(3, evento.getDeporte().getId_deporte());
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    pstmt.close();
                    connection.closeConnection();
                    return id;
                }
            }
            pstmt.close();
            connection.closeConnection();
            return -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    /**
     * Elimina un evento
     *
     * @param evento Evento a eliminar
     * @return True -> Se puede eliminar, False -> No se puede eliminar
     */
    public static boolean eliminar(Evento evento) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "DELETE FROM Evento WHERE id_evento = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, evento.getId_evento());
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
