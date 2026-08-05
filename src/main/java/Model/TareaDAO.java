package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO {

    public List<Tarea> listarPorCurso(int idCurso) {
        List<Tarea> lista = new ArrayList<>();
        String query = "SELECT * FROM tarea WHERE idCurso = ? ORDER BY f_habil";
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

    public List<Tarea> listarPorMaestro(int idMaestro) {
        List<Tarea> lista = new ArrayList<>();
        String query = "SELECT * FROM tarea WHERE idMaestro = ? ORDER BY f_limit";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idMaestro);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public Tarea buscarPorId(int idTarea) {
        String query = "SELECT * FROM tarea WHERE idTarea = ?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idTarea);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean crear(Tarea t) {
        String query = "INSERT INTO tarea (nombre, descripcion, f_habil, f_limit, idCurso, idMaestro, archivoEnunciado) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, t.getNombre());
            pst.setString(2, t.getDescripcion());
            pst.setDate(3, t.getfHabil());
            pst.setDate(4, t.getfLimit());
            pst.setInt(5, t.getIdCurso());
            pst.setInt(6, t.getIdMaestro());
            pst.setBytes(7, t.getArchivoEnunciado());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean actualizar(Tarea t) {
        String query = "UPDATE tarea SET nombre=?, descripcion=?, f_habil=?, f_limit=? WHERE idTarea=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, t.getNombre());
            pst.setString(2, t.getDescripcion());
            pst.setDate(3, t.getfHabil());
            pst.setDate(4, t.getfLimit());
            pst.setInt(5, t.getIdTarea());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminar(int idTarea) {
        String query = "DELETE FROM tarea WHERE idTarea=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idTarea);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Tarea mapRow(ResultSet rs) throws SQLException {
        Tarea t = new Tarea();
        t.setIdTarea(rs.getInt("idTarea"));
        t.setNombre(rs.getString("nombre"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setfHabil(rs.getDate("f_habil"));
        t.setfLimit(rs.getDate("f_limit"));
        t.setIdCurso(rs.getInt("idCurso"));
        t.setIdMaestro(rs.getInt("idMaestro"));
        t.setArchivoEnunciado(rs.getBytes("archivoEnunciado"));
        return t;
    }
}
