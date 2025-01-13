package KoneksiDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Koneksi {
    public static Connection con;
    public static Statement stm;

    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/koperasi"; // Ganti 'namadatabase' dengan nama database Anda
            String user = "root";
            String pass = "";
            Class.forName("com.mysql.cj.jdbc.Driver"); // Perbarui dengan driver baru
            con = DriverManager.getConnection(url, user, pass);
            stm = con.createStatement();
            System.out.println("Koneksi berhasil");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Koneksi gagal: " + e.getMessage());
        }
    }
}