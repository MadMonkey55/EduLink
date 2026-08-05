package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** DAO para la tabla detalleTareas: entregas de los alumnos para cada tarea. */
public class DetalleTareasDAO {

    public boolean entregar(int idTarea, int idAlumno, byte[] archivo, String comentarios) {
        // Si ya existe una entrega, se actualiza (permite modificar antes de la fecha limite)
        if (yaEntrego(idTarea, idAlumno)) {
            String query = "UPDATE detalleTareas SET fDeEntrega=?, archivoEntrega=?, comentarios=? WHERE idTarea=? AND idAlumno=?";
            try (Connection con = ConexionBD.connection();
                 PreparedStatement pst = con.prepareStatement(query)) {
                pst.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                pst.setBytes(2, archivo);
                pst.setString(3, comentarios);
                pst.setInt(4, idTarea);
                pst.setInt(5, idAlumno);
                return pst.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        String query = "INSERT INTO detalleTareas (idTarea, idAlumno, fDeEntrega, archivoEntrega, comentarios, calificacion) VALUES (?,?,?,?,?,NULL)";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, idTarea);
            pst.setInt(2, idAlumno);
            pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pst.setBytes(4, archivo);
            pst.setString(5, comentarios);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean yaEntrego(int idTarea, int idAlumno) {
        String query = "SELECT COUNT(*) FROM detalleTareas WHERE idTarea=? AND idAlumno=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idTarea);
            pst.setInt(2, idAlumno);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public DetalleTareas buscarEntrega(int idTarea, int idAlumno) {
        String query = "SELECT * FROM detalleTareas WHERE idTarea=? AND idAlumno=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idTarea);
            pst.setInt(2, idAlumno);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<DetalleTareas> listarPorTarea(int idTarea) {
        List<DetalleTareas> lista = new ArrayList<>();
        String query = "SELECT d.*, CONCAT(a.nombre,' ',a.apellido) AS nombreAlumno " +
                "FROM detalleTareas d JOIN alumno a ON d.idAlumno = a.idAlumno WHERE d.idTarea = ? ORDER BY d.fDeEntrega DESC";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idTarea);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) lista.add(mapRowConAlumno(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public List<DetalleTareas> listarPorAlumnoYCurso(int idAlumno, int idCurso) {
        List<DetalleTareas> lista = new ArrayList<>();
        String query = "SELECT d.* FROM detalleTareas d JOIN tarea t ON d.idTarea = t.idTarea " +
                "WHERE d.idAlumno = ? AND t.idCurso = ?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idAlumno);
            pst.setInt(2, idCurso);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public boolean calificar(int idDetalle, double calificacion, String comentarios) {
        String query = "UPDATE detalleTareas SET calificacion=?, comentarios=? WHERE idDetalle=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setDouble(1, calificacion);
            pst.setString(2, comentarios);
            pst.setInt(3, idDetalle);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int contarPendientesPorCalificar() {
        String query = "SELECT COUNT(*) FROM detalleTareas WHERE calificacion IS NULL";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private DetalleTareas mapRow(ResultSet rs) throws SQLException {
        DetalleTareas d = new DetalleTareas();
        d.setIdDetalle(rs.getInt("idDetalle"));
        d.setIdTarea(rs.getInt("idTarea"));
        d.setIdAlumno(rs.getInt("idAlumno"));
        d.setfDeEntrega(rs.getTimestamp("fDeEntrega"));
        d.setArchivoEntrega(rs.getBytes("archivoEntrega"));
        d.setComentarios(rs.getString("comentarios"));
        double cal = rs.getDouble("calificacion");
        d.setCalificacion(rs.wasNull() ? null : cal);
        return d;
    }

    private DetalleTareas mapRowConAlumno(ResultSet rs) throws SQLException {
        DetalleTareas d = mapRow(rs);
        d.setNombreAlumno(rs.getString("nombreAlumno"));
        return d;
    }
}
