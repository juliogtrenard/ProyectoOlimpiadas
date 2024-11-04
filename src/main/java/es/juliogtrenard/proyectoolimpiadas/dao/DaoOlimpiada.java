package es.juliogtrenard.proyectoolimpiadas.dao;

import es.juliogtrenard.proyectoolimpiadas.db.DBConnect;
import es.juliogtrenard.proyectoolimpiadas.modelos.Olimpiada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase donde se ejecuta las consultas para la tabla Olimpiada
 */
public class DaoOlimpiada {
    /**
     * Carga los datos de la tabla Olimpiadas y los devuelve para usarlos en un listado de olimpiadas
     *
     * @return listado Lista para cargar en un tableview
     */
    public static ObservableList<Olimpiada> cargarListado() {
        DBConnect connection;
        ObservableList<Olimpiada> olimpiadas = FXCollections.observableArrayList();
        try{
            connection = new DBConnect();
            String consulta = "SELECT id_olimpiada,nombre,anio,temporada,ciudad FROM Olimpiada";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_olimpiada = rs.getInt("id_olimpiada");
                String nombre = rs.getString("nombre");
                int anio = rs.getInt("anio");
                String temporada = rs.getString("temporada");
                String ciudad = rs.getString("ciudad");
                Olimpiada olimpiada = new Olimpiada(id_olimpiada,nombre,anio,temporada,ciudad);
                olimpiadas.add(olimpiada);
            }
            rs.close();
            connection.closeConnection();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return olimpiadas;
    }

    /**
     * Busca una olimpiada y mira si se puede eliminar
     *
     * @param olimpiada Olimpiada a eliminar
     * @return True -> Se puede, False -> No se puede
     */
    public static boolean esEliminable(Olimpiada olimpiada) {
        DBConnect connection;
        try {
            connection = new DBConnect();
            String consulta = "SELECT count(*) as cont FROM Evento WHERE id_olimpiada = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, olimpiada.getId_olimpiada());
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
     * Modifica los datos de una olimpiada
     *
     * @param olimpiada Olimpiada
     * @param olimpiadaNuevo Nuevos datos de la olimpiada a modificar
     * @return True -> Se puede, False -> No se puede
     */
    public static boolean modificar(Olimpiada olimpiada, Olimpiada olimpiadaNuevo) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "UPDATE Olimpiada SET nombre = ?,anio = ?,temporada = ?,ciudad = ? WHERE id_olimpiada = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, olimpiadaNuevo.getNombre());
            pstmt.setInt(2, olimpiadaNuevo.getAnio());
            pstmt.setString(3, olimpiadaNuevo.getTemporada().toString());
            pstmt.setString(4, olimpiadaNuevo.getCiudad());
            pstmt.setInt(5, olimpiada.getId_olimpiada());
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
     * Crea una nueva olimpiada
     *
     * @param olimpiada Olimpiada
     * @return id/-1
     */
    public  static int insertar(Olimpiada olimpiada) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "INSERT INTO Olimpiada (nombre,anio,temporada,ciudad) VALUES (?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, olimpiada.getNombre());
            pstmt.setInt(2, olimpiada.getAnio());
            pstmt.setString(3, olimpiada.getTemporada().toString());
            pstmt.setString(4, olimpiada.getCiudad());
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
     * Elimina una olimpiada
     *
     * @param olimpiada Olimpiada a eliminar
     * @return True -> Se puede, False -> No se puede
     */
    public static boolean eliminar(Olimpiada olimpiada) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "DELETE FROM Olimpiada WHERE id_olimpiada = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, olimpiada.getId_olimpiada());
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
     * Busca una olimpiada por su id
     *
     * @param id ID de la olimpiada
     * @return olimpiada o null
     */
    public static Olimpiada getOlimpiada(int id) {
        DBConnect connection;
        Olimpiada olimpiada = null;
        try {
            connection = new DBConnect();
            String consulta = "SELECT id_olimpiada,nombre,anio,temporada,ciudad FROM Olimpiada WHERE id_olimpiada = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_olimpiada = rs.getInt("id_olimpiada");
                String nombre = rs.getString("nombre");
                int anio = rs.getInt("anio");
                String temporada = rs.getString("temporada");
                String ciudad = rs.getString("ciudad");
                olimpiada = new Olimpiada(id_olimpiada,nombre,anio,temporada,ciudad);
            }
            rs.close();
            connection.closeConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return olimpiada;
    }
}