package es.juliogtrenard.proyectoolimpiadas.dao;

import es.juliogtrenard.proyectoolimpiadas.db.DBConnect;
import es.juliogtrenard.proyectoolimpiadas.modelos.Deportista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

/**
 * Clase donde se ejecuta las consultas para la tabla Deportista
 */
public class DaoDeportista {
    /**
     * Busca un deportista por medio de su id
     *
     * @param id ID del deportista a buscar
     * @return deportista o null
     */
    public static Deportista getDeportista(int id) {
        DBConnect connection;
        Deportista deportista = null;
        try {
            connection = new DBConnect();
            String consulta = "SELECT id_deportista,nombre,sexo,peso,altura,foto FROM Deportista WHERE id_deportista = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_deportista = rs.getInt("id_deportista");
                String nombre = rs.getString("nombre");
                char sexo = rs.getString("sexo").charAt(0);
                int peso = rs.getInt("peso");
                int altura = rs.getInt("altura");
                Blob foto = rs.getBlob("foto");
                deportista = new Deportista(id_deportista,nombre,sexo,peso,altura,foto);
            }
            rs.close();
            connection.closeConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return deportista;
    }

    /**
     * Convierte un objeto File a Blob
     *
     * @param file fichero imagen
     * @return blob
     * @throws SQLException
     * @throws IOException
     */
    public static Blob convertFileToBlob(File file) throws SQLException, IOException {
        DBConnect connection = new DBConnect();
        // Open a connection to the database
        try (Connection conn = connection.getConnection();
             FileInputStream inputStream = new FileInputStream(file)) {

            // Create Blob
            Blob blob = conn.createBlob();
            // Write the file's bytes to the Blob
            byte[] buffer = new byte[1024];
            int bytesRead;

            try (var outputStream = blob.setBinaryStream(1)) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return blob;
        }
    }

    /**
     * Carga los datos de la tabla Deportistas y los devuelve para usarlos en un listado de deportistas
     *
     * @return listado de deportistas para cargar en un tableview
     */
    public static ObservableList<Deportista> cargarListado() {
        DBConnect connection;
        ObservableList<Deportista> deportistas = FXCollections.observableArrayList();
        try{
            connection = new DBConnect();
            String consulta = "SELECT id_deportista,nombre,sexo,peso,altura,foto FROM Deportista";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_deportista = rs.getInt("id_deportista");
                String nombre = rs.getString("nombre");
                char sexo = rs.getString("sexo").charAt(0);
                int peso = rs.getInt("peso");
                int altura = rs.getInt("altura");
                Blob foto = rs.getBlob("foto");
                Deportista deportista = new Deportista(id_deportista,nombre,sexo,peso,altura,foto);
                deportistas.add(deportista);
            }
            rs.close();
            connection.closeConnection();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return deportistas;
    }

    /**
     * Busca un deportista y mira si se puede eliminar
     *
     * @param deportista Deportista a buscar
     * @return True -> se puede eliminar, False -> no se puede eliminar
     */
    public static boolean esEliminable(Deportista deportista) {
        DBConnect connection;
        try {
            connection = new DBConnect();
            String consulta = "SELECT count(*) as cont FROM Participacion WHERE id_deportista = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, deportista.getId_deportista());
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
     * Modifica los datos de un deportista
     *
     * @param deportista Deportista con sus datos
     * @param deportistaNuevo Nuevos datos del deportista a modificar
     * @return true -> modificado, false -> no modificado
     */
    public static boolean modificar(Deportista deportista, Deportista deportistaNuevo) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "UPDATE Deportista SET nombre = ?,sexo = ?,peso = ?,altura = ?,foto = ? WHERE id_deportista = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, deportistaNuevo.getNombre());
            pstmt.setString(2, deportistaNuevo.getSexo() + "");
            pstmt.setInt(3, deportistaNuevo.getPeso());
            pstmt.setInt(4, deportistaNuevo.getAltura());
            pstmt.setBlob(5, deportistaNuevo.getFoto());
            pstmt.setInt(6, deportista.getId_deportista());
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
     * Inserta un nuevo deportista en la base de datos
     *
     * @param deportista Deportista con datos nuevos
     * @return id/-1
     */
    public  static int insertar(Deportista deportista) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "INSERT INTO Deportista (nombre,sexo,peso,altura,foto) VALUES (?,?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, deportista.getNombre());
            pstmt.setString(2, deportista.getSexo() + "");
            pstmt.setInt(3, deportista.getPeso());
            pstmt.setInt(4, deportista.getAltura());
            pstmt.setBlob(5, deportista.getFoto());
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
     * Elimina un deportista
     *
     * @param deportista Deportista a eliminar
     * @return true -> eliminado, false -> error
     */
    public static boolean eliminar(Deportista deportista) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "DELETE FROM Deportista WHERE id_deportista = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, deportista.getId_deportista());
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