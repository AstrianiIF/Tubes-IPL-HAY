package Auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthManager {
    private Connection con;

    public AuthManager(Connection con) {
        this.con = con;
    }

    public ResultSet loginAdmin(String username, String password) throws SQLException {
        String query = "SELECT * FROM dim_admin WHERE Username = ? AND Password = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, username);
        pst.setString(2, password);
        return pst.executeQuery();
    }

    public ResultSet loginUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM dim_anggota WHERE username = ? AND password = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, username);
        pst.setString(2, password);
        return pst.executeQuery();
    }
}
