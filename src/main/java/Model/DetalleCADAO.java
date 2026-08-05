package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DAO para la tabla detalleCA: inscripcion de alumnos en cursos + calificacion final. */
public class DetalleCADAO {

    public boolean inscribir(int idCurso, int idAlumno) {
        if (yaInscrito(idCurso, idAlumno)) return false;
        String query = "INSERT INTO detalleCA (idCurso, idAlumno, calificacion) VALUES (?,?,NULL)";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idCurso);
            pst.setInt(2, idAlumno);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean yaInscrito(int idCurso, int idAlumno) {
        String query = "SELECT COUNT(*) FROM detalleCA WHERE idCurso=? AND idAlumno=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idCurso);
            pst.setInt(2, idAlumno);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean darDeBaja(int idCurso, int idAlumno) {
        String query = "DELETE FROM detalleCA WHERE idCurso=? AND idAlumno=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idCurso);
            pst.setInt(2, idAlumno);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean calificar(int idCurso, int idAlumno, double calificacion) {
        String query = "UPDATE detalleCA SET calificacion=? WHERE idCurso=? AND idAlumno=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setDouble(1, calificacion);
            pst.setInt(2, idCurso);
            pst.setInt(3, idAlumno);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DetalleCA> listarPorCurso(int idCurso) {
        List<DetalleCA> lista = new ArrayList<>();
        String query = "SELECT d.*, CONCAT(a.nombre,' ',a.apellido) AS nombreAlumno " +
                "FROM detalleCA d JOIN alumno a ON d.idAlumno = a.idAlumno WHERE d.idCurso = ?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idCurso);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public List<DetalleCA> listarPorAlumno(int idAlumno) {
        List<DetalleCA> lista = new ArrayList<>();
        String query = "SELECT d.*, c.nombre AS nombreCurso " +
                "FROM detalleCA d JOIN curso c ON d.idCurso = c.idCurso WHERE d.idAlumno = ?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idAlumno);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) lista.add(mapRowAlumnoView(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public Double promedioCurso(int idCurso) {
        String query = "SELECT AVG(calificacion) FROM detalleCA WHERE idCurso=? AND calificacion IS NOT NULL";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idCurso);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    double v = rs.getDouble(1);
                    return rs.wasNull() ? null : v;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private DetalleCA mapRow(ResultSet rs) throws SQLException {
        DetalleCA d = new DetalleCA();
        d.setIdCurso(rs.getInt("idCurso"));
        d.setIdAlumno(rs.getInt("idAlumno"));
        double cal = rs.getDouble("calificacion");
        d.setCalificacion(rs.wasNull() ? null : cal);
        d.setNombreAlumno(rs.getString("nombreAlumno"));
        return d;
    }

    private DetalleCA mapRowAlumnoView(ResultSet rs) throws SQLException {
        DetalleCA d = new DetalleCA();
        d.setIdCurso(rs.getInt("idCurso"));
        d.setIdAlumno(rs.getInt("idAlumno"));
        double cal = rs.getDouble("calificacion");
        d.setCalificacion(rs.wasNull() ? null : cal);
        d.setNombreCurso(rs.getString("nombreCurso"));
        return d;
    }
}
