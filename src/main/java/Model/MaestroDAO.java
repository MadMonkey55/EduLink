package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaestroDAO {

    public User validarMaestro(String correo, String password) {
        String query = "SELECT * FROM maestro WHERE correo = ? AND password = ?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, correo);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("idMaestro"));
                    user.setNombre(rs.getString("nombre"));
                    user.setApellido(rs.getString("apellido"));
                    user.setCorreo(rs.getString("correo"));
                    user.setPassword(rs.getString("password"));
                    user.setRol(AuthDAO.ROL_MAESTRO);
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean registrar(Maestro m) {
        String query = "INSERT INTO maestro (nombre, apellido, correo, password, domicilio, localidad, telefono, edad) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, m.getNombre());
            pst.setString(2, m.getApellido());
            pst.setString(3, m.getCorreo());
            pst.setString(4, m.getPassword());
            pst.setString(5, m.getDomicilio());
            pst.setString(6, m.getLocalidad());
            pst.setString(7, m.getTelefono());
            pst.setInt(8, m.getEdad());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean actualizar(Maestro m) {
        String query = "UPDATE maestro SET nombre=?, apellido=?, correo=?, domicilio=?, localidad=?, telefono=?, edad=? WHERE idMaestro=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, m.getNombre());
            pst.setString(2, m.getApellido());
            pst.setString(3, m.getCorreo());
            pst.setString(4, m.getDomicilio());
            pst.setString(5, m.getLocalidad());
            pst.setString(6, m.getTelefono());
            pst.setInt(7, m.getEdad());
            pst.setInt(8, m.getIdMaestro());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminar(int idMaestro) {
        String query = "DELETE FROM maestro WHERE idMaestro=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idMaestro);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Maestro> listarTodos() {
        List<Maestro> lista = new ArrayList<>();
        String query = "SELECT * FROM maestro ORDER BY nombre";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public Maestro buscarPorId(int idMaestro) {
        String query = "SELECT * FROM maestro WHERE idMaestro = ?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idMaestro);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public int contarMaestros() {
        String query = "SELECT COUNT(*) FROM maestro";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private Maestro mapRow(ResultSet rs) throws SQLException {
        Maestro m = new Maestro();
        m.setIdMaestro(rs.getInt("idMaestro"));
        m.setNombre(rs.getString("nombre"));
        m.setApellido(rs.getString("apellido"));
        m.setCorreo(rs.getString("correo"));
        m.setPassword(rs.getString("password"));
        m.setDomicilio(rs.getString("domicilio"));
        m.setLocalidad(rs.getString("localidad"));
        m.setTelefono(rs.getString("telefono"));
        m.setEdad(rs.getInt("edad"));
        return m;
    }
}
