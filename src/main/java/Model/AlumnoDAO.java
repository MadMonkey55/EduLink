package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {

    public User validarAlumno(String correo, String password) {
        String query = "SELECT * FROM alumno WHERE correo = ? AND password = ?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, correo);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /** Registra un nuevo alumno (pantalla RECORD). Retorna true si se pudo registrar. */
    public boolean registrar(Alumno a) {
        String query = "INSERT INTO alumno (nombre, apellido, correo, matricula, password, domicilio, localidad, telefono, edad) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, a.getNombre());
            pst.setString(2, a.getApellido());
            pst.setString(3, a.getCorreo());
            pst.setInt(4, a.getMatricula());
            pst.setString(5, a.getPassword());
            pst.setString(6, a.getDomicilio());
            pst.setString(7, a.getLocalidad());
            pst.setString(8, a.getTelefono());
            pst.setInt(9, a.getEdad());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean actualizar(Alumno a) {
        String query = "UPDATE alumno SET nombre=?, apellido=?, correo=?, matricula=?, domicilio=?, localidad=?, telefono=?, edad=? WHERE idAlumno=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, a.getNombre());
            pst.setString(2, a.getApellido());
            pst.setString(3, a.getCorreo());
            pst.setInt(4, a.getMatricula());
            pst.setString(5, a.getDomicilio());
            pst.setString(6, a.getLocalidad());
            pst.setString(7, a.getTelefono());
            pst.setInt(8, a.getEdad());
            pst.setInt(9, a.getIdAlumno());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminar(int idAlumno) {
        String query = "DELETE FROM alumno WHERE idAlumno=?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idAlumno);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Alumno> listarTodos() {
        List<Alumno> lista = new ArrayList<>();
        String query = "SELECT * FROM alumno ORDER BY nombre";
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

    public Alumno buscarPorId(int idAlumno) {
        String query = "SELECT * FROM alumno WHERE idAlumno = ?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, idAlumno);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean existeCorreoOMatricula(String correo, int matricula) {
        String query = "SELECT COUNT(*) FROM alumno WHERE correo = ? OR matricula = ?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, correo);
            pst.setInt(2, matricula);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public int contarAlumnos() {
        String query = "SELECT COUNT(*) FROM alumno";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private Alumno mapRow(ResultSet rs) throws SQLException {
        Alumno a = new Alumno();
        a.setIdAlumno(rs.getInt("idAlumno"));
        a.setNombre(rs.getString("nombre"));
        a.setApellido(rs.getString("apellido"));
        a.setCorreo(rs.getString("correo"));
        a.setMatricula(rs.getInt("matricula"));
        a.setPassword(rs.getString("password"));
        a.setDomicilio(rs.getString("domicilio"));
        a.setLocalidad(rs.getString("localidad"));
        a.setTelefono(rs.getString("telefono"));
        a.setEdad(rs.getInt("edad"));
        return a;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("idAlumno"));
        user.setNombre(rs.getString("nombre"));
        user.setApellido(rs.getString("apellido"));
        user.setCorreo(rs.getString("correo"));
        user.setMatricula(rs.getInt("matricula"));
        user.setPassword(rs.getString("password"));
        user.setRol(AuthDAO.ROL_ALUMNO);
        return user;
    }
}
