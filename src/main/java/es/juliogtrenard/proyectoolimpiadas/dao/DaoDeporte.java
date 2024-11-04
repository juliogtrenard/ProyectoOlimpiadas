package es.juliogtrenard.proyectoolimpiadas.dao;

import es.juliogtrenard.proyectoolimpiadas.db.DBConnect;
import es.juliogtrenard.proyectoolimpiadas.modelos.Deporte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase donde se ejecuta las consultas para la tabla Deporte
 */
public class DaoDeporte {
    /**
     * Carga los datos de la tabla Deportes y los devuelve para usarlos en un listado de deportes
     *
     * @return listado Lista de deportes para cargar en un tableview
     */
    public static ObservableList<Deporte> cargarListado() {
        DBConnect connection;
        ObservableList<Deporte> deportes = FXCollections.observableArrayList();
        try{
            connection = new DBConnect();
            String consulta = "SELECT id_deporte,nombre FROM Deporte";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_deporte = rs.getInt("id_deporte");
                String nombre = rs.getString("nombre");
                Deporte deporte = new Deporte(id_deporte,nombre);
                deportes.add(deporte);
            }
            rs.close();
            connection.closeConnection();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return deportes;
    }

    /**
     * Busca un deporte y verifica si se puede eliminar
     *
     * @param deporte Deporte a eliminar
     * @return true -> Se puede, false -> No se puede
     */
    public static boolean esEliminable(Deporte deporte) {
        DBConnect connection;
        try {
            connection = new DBConnect();
            String consulta = "SELECT count(*) as cont FROM Evento WHERE id_deporte = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, deporte.getId_deporte());
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
     * Modifica los datos de un deporte
     *
     * @param deporte Deporte
     * @param deporteNuevo Nuevos datos del deporte
     * @return  true -> Se puede, false -> No se puede
     */
    public static boolean modificar(Deporte deporte, Deporte deporteNuevo) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "UPDATE Deporte SET nombre = ? WHERE id_deporte = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, deporteNuevo.getNombre());
            pstmt.setInt(2, deporte.getId_deporte());
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
     * Crea un nuevo deporte
     *
     * @param deporte Deporte
     * @return id/-1
     */
    public  static int insertar(Deporte deporte) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "INSERT INTO Deporte (nombre) VALUES (?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, deporte.getNombre());
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
            System.err.println(e.getMessage());
            return -1;
        }
    }

    /**
     * Elimina un deporte
     *
     * @param deporte Deporte a eliminar
     * @return true -> Eliminado con éxito, false -> No eliminado
     */
    public static boolean eliminar(Deporte deporte) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "DELETE FROM Deporte WHERE id_deporte = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, deporte.getId_deporte());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
