package es.juliogtrenard.proyectoolimpiadas.dao;

import es.juliogtrenard.proyectoolimpiadas.db.DBConnect;
import es.juliogtrenard.proyectoolimpiadas.modelos.Equipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase donde se ejecutan las consultas para la tabla Equipo
 */
public class DaoEquipo {
    /**
     * Carga los datos y los devuelve para usarlos en un listado de equipos
     *
     * @return equipos Listado de equipos para cargar en un tableview
     */
    public static ObservableList<Equipo> cargarListado() {
        DBConnect connection;
        ObservableList<Equipo> equipos = FXCollections.observableArrayList();
        try{
            connection = new DBConnect();
            String consulta = "SELECT id_equipo,nombre,iniciales FROM Equipo";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_equipo = rs.getInt("id_equipo");
                String nombre = rs.getString("nombre");
                String iniciales = rs.getString("iniciales");
                Equipo equipo = new Equipo(id_equipo,nombre,iniciales);
                equipos.add(equipo);
            }
            rs.close();
            connection.closeConnection();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return equipos;
    }

    /**
     * Comprueba si el equipo se puede eliminar
     *
     * @param equipo Equipo a comprobar
     * @return true -> Se puede, false -> No se puede
     */
    public static boolean esEliminable(Equipo equipo) {
        DBConnect connection;
        try {
            connection = new DBConnect();
            String consulta = "SELECT count(*) as cont FROM Participacion WHERE id_equipo = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, equipo.getId_equipo());
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
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Modifica los datos de un equipo
     *
     * @param equipo Equipo a modificar
     * @param equipoNuevo Nuevos datos del equipo
     * @return true -> Modificado, false -> No modificado
     */
    public static boolean modificar(Equipo equipo, Equipo equipoNuevo) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "UPDATE Equipo SET nombre = ?,iniciales = ? WHERE id_equipo = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, equipoNuevo.getNombre());
            pstmt.setString(2, equipoNuevo.getIniciales());
            pstmt.setInt(3, equipo.getId_equipo());
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
     * Crea un nuevo equipo
     *
     * @param equipo Equipo a crear
     * @return id/-1
     */
    public  static int insertar(Equipo equipo) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "INSERT INTO Equipo (nombre,iniciales) VALUES (?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, equipo.getNombre());
            pstmt.setString(2, equipo.getIniciales());
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
     * Elimina una equipo
     *
     * @param equipo Equipo a eliminar
     * @return true -> Eliminado, false -> No eliminado
     */
    public static boolean eliminar(Equipo equipo) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "DELETE FROM Equipo WHERE id_equipo = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, equipo.getId_equipo());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Busca un equipo por su id
     *
     * @param id ID del equipo a buscar
     * @return equipo o null
     */
    public static Equipo getEquipo(int id) {
        DBConnect connection;
        Equipo equipo = null;
        try {
            connection = new DBConnect();
            String consulta = "SELECT id_equipo,nombre,iniciales FROM Equipo WHERE id_equipo = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_equipo = rs.getInt("id_equipo");
                String nombre = rs.getString("nombre");
                String iniciales = rs.getString("iniciales");
                equipo = new Equipo(id_equipo,nombre,iniciales);
            }
            rs.close();
            connection.closeConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return equipo;
    }
}

