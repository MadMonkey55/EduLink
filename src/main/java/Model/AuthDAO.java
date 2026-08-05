package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Autenticación única para las 3 tablas de usuarios de EduLink.
 * El identificador puede ser: correo, matrícula (solo alumno) o nombre (solo admin).
 */
public class AuthDAO {

    public static final String ROL_ADMIN = "admin";
    public static final String ROL_MAESTRO = "maestro";
    public static final String ROL_ALUMNO = "alumno";

    public User login(String identificador, String password) {
        User user;

        user = loginAlumno(identificador, password);
        if (user != null) return user;

        user = loginMaestro(identificador, password);
        if (user != null) return user;

        user = loginAdministrador(identificador, password);
        return user;
    }

    private User loginAlumno(String identificador, String password) {
        String query = "SELECT * FROM alumno WHERE (correo = ? OR matricula = ?) AND password = ?";
        Integer matriculaNum = parseIntSafe(identificador);
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, identificador);
            pst.setInt(2, matriculaNum != null ? matriculaNum : -1);
            pst.setString(3, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("idAlumno"));
                    user.setNombre(rs.getString("nombre"));
                    user.setApellido(rs.getString("apellido"));
                    user.setCorreo(rs.getString("correo"));
                    user.setMatricula(rs.getInt("matricula"));
                    user.setPassword(rs.getString("password"));
                    user.setDomicilio(rs.getString("domicilio"));
                    user.setLocalidad(rs.getString("localidad"));
                    user.setTelefono(rs.getString("telefono"));
                    user.setEdad(rs.getInt("edad"));
                    user.setRol(ROL_ALUMNO);
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private User loginMaestro(String identificador, String password) {
        String query = "SELECT * FROM maestro WHERE correo = ? AND password = ?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, identificador);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("idMaestro"));
                    user.setNombre(rs.getString("nombre"));
                    user.setApellido(rs.getString("apellido"));
                    user.setCorreo(rs.getString("correo"));
                    user.setPassword(rs.getString("password"));
                    user.setDomicilio(rs.getString("domicilio"));
                    user.setLocalidad(rs.getString("localidad"));
                    user.setTelefono(rs.getString("telefono"));
                    user.setEdad(rs.getInt("edad"));
                    user.setRol(ROL_MAESTRO);
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private User loginAdministrador(String identificador, String password) {
        String query = "SELECT * FROM administrador WHERE (correo = ? OR nombre = ?) AND password = ?";
        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, identificador);
            pst.setString(2, identificador);
            pst.setString(3, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("idAdministrador"));
                    user.setNombre(rs.getString("nombre"));
                    user.setCorreo(rs.getString("correo"));
                    user.setPassword(rs.getString("password"));
                    user.setRol(ROL_ADMIN);
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Integer parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return null;
        }
    }
}
