package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    public User validarAdministrador(String correo, String password) {
        String query = "SELECT * FROM administrador WHERE correo = ? AND password = ?";

        try (Connection con = ConexionBD.connection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, correo);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("idAdministrador"));
                    user.setCorreo(rs.getString("correo"));
                    user.setPassword(rs.getString("password"));
                    user.setNombre(rs.getString("nombre"));
                    user.setRol(AuthDAO.ROL_ADMIN);
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
