package Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    public List<Curso> getAllCourses() {
        List<Curso> cursos = new ArrayList<>();
        String query = "SELECT c.*, m.nombre AS nombreMaestro, m.apellido AS apellidoMaestro " +
                "FROM curso c LEFT JOIN maestro m ON c.idMaestro = m.idMaestro ORDER BY c.nivel ASC";

        try (Connection conn = ConexionBD.connection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                cursos.add(extracCourseFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener cursos: " + e.getMessage());
            e.printStackTrace();
        }
        return cursos;
    }

    public List<Curso> getCursosPorMaestro(int idMaestro) {
        List<Curso> cursos = new ArrayList<>();
        String query = "SELECT c.*, m.nombre AS nombreMaestro, m.apellido AS apellidoMaestro " +
                "FROM curso c LEFT JOIN maestro m ON c.idMaestro = m.idMaestro WHERE c.idMaestro = ? ORDER BY c.nivel";
        try (Connection conn = ConexionBD.connection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, idMaestro);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    cursos.add(extracCourseFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cursos;
    }

    public Curso buscarPorId(int idCurso) {
        String query = "SELECT c.*, m.nombre AS nombreMaestro, m.apellido AS apellidoMaestro " +
                "FROM curso c LEFT JOIN maestro m ON c.idMaestro = m.idMaestro WHERE c.idCurso = ?";
        try (Connection conn = ConexionBD.connection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, idCurso);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return extracCourseFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean crear(Curso c) {
        String query = "INSERT INTO curso (nombre, nivel, descripcion, idMaestro, fDeInicio, fDeFinalizacion, imgCurso) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = ConexionBD.connection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, c.getNombre());
            pst.setString(2, c.getNivel());
            pst.setString(3, c.getDescripcion());
            if (c.getIdMaestro() > 0) pst.setInt(4, c.getIdMaestro()); else pst.setNull(4, java.sql.Types.INTEGER);
            pst.setDate(5, c.getfDeInicio());
            pst.setDate(6, c.getfDeFinalizacion());
            pst.setBytes(7, c.getImgCurso());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean actualizar(Curso c) {
        String query = "UPDATE curso SET nombre=?, nivel=?, descripcion=?, idMaestro=?, fDeInicio=?, fDeFinalizacion=? WHERE idCurso=?";
        try (Connection conn = ConexionBD.connection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, c.getNombre());
            pst.setString(2, c.getNivel());
            pst.setString(3, c.getDescripcion());
            if (c.getIdMaestro() > 0) pst.setInt(4, c.getIdMaestro()); else pst.setNull(4, java.sql.Types.INTEGER);
            pst.setDate(5, c.getfDeInicio());
            pst.setDate(6, c.getfDeFinalizacion());
            pst.setInt(7, c.getIdCurso());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminar(int idCurso) {
        String query = "DELETE FROM curso WHERE idCurso=?";
        try (Connection conn = ConexionBD.connection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, idCurso);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int contarCursos() {
        String query = "SELECT COUNT(*) FROM curso";
        try (Connection conn = ConexionBD.connection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    //Metodo Auxiliar para obtener cursos de ResultSet
    private Curso extracCourseFromResultSet(ResultSet rs) throws SQLException {
        Curso curso = new Curso();
        curso.setIdCurso(rs.getInt("idCurso"));
        curso.setNombre(rs.getString("nombre"));
        curso.setNivel(rs.getString("nivel"));
        curso.setDescripcion(rs.getString("descripcion"));
        curso.setIdMaestro(rs.getInt("idMaestro"));
        curso.setfDeInicio(rs.getDate("fDeInicio"));
        curso.setfDeFinalizacion(rs.getDate("fDeFinalizacion"));
        curso.setImgCurso(rs.getBytes("imgCurso"));
        try {
            String nombreM = rs.getString("nombreMaestro");
            String apellidoM = rs.getString("apellidoMaestro");
            if (nombreM != null) curso.setNombreMaestro(nombreM + " " + (apellidoM != null ? apellidoM : ""));
        } catch (SQLException ignored) { }
        return curso;
    }
}
