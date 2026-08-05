package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    public List<Material> listarPorCurso(int idCurso) {
        List<Material> lista = new ArrayList<>();
        String query = "SELECT * FROM material WHERE idCurso = ? ORDER BY leccion";
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

    public List<Material> listarPorMaestro(int idMaestro) {
        List<Material> lista = new ArrayList<>();
        String query = "SELECT * FROM material WHERE idMaestro = ? ORDER BY idCurso, leccion";
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

    public boolean crear(Material m) {
        String query = "INSERT INTO material (idCurso, idMaestro, nombre, archivo, curso, descripcion, leccion) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, m.getIdCurso());
            pst.setInt(2, m.getIdMaestro());
            pst.setString(3, m.getNombre());
            pst.setBytes(4, m.getArchivo());
            pst.setString(5, m.getCurso());
            pst.setString(6, m.getDescripcion());
            pst.setInt(7, m.getLeccion());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminar(int idMaterial) {
        String query = "DELETE FROM material WHERE idMaterial=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idMaterial);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int contarMateriales() {
        String query = "SELECT COUNT(*) FROM material";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private Material mapRow(ResultSet rs) throws SQLException {
        Material m = new Material();
        m.setIdMaterial(rs.getInt("idMaterial"));
        m.setIdCurso(rs.getInt("idCurso"));
        m.setIdMaestro(rs.getInt("idMaestro"));
        m.setNombre(rs.getString("nombre"));
        m.setArchivo(rs.getBytes("archivo"));
        m.setCurso(rs.getString("curso"));
        m.setDescripcion(rs.getString("descripcion"));
        m.setLeccion(rs.getInt("leccion"));
        return m;
    }
}
