package UI;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import KoneksiDatabase.DatabaseManager;
import Auth.AuthManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class sceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Connection con;
    private AuthManager authManager;

    // Variabel untuk menyimpan data pengguna yang login
    private String loggedInNama;
    private String loggedInAlamat;
    private String loggedInTanggalLahir;

    @FXML private TextField UsernameLogin;
    @FXML private PasswordField PasswordLogin;
    @FXML private Label ProfileNamaAnggota;
    @FXML private Label ProfileAlamatAnggota;
    @FXML private Label ProfileTanggalLahirAnggota;

    public void initialize() {
        try {
            con = DatabaseManager.getConnection();
            authManager = new AuthManager(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loginUser(ActionEvent event) {
        String username = UsernameLogin.getText();
        String password = PasswordLogin.getText();

        try {
            if (authManager.loginAdmin(username, password)) {
                System.out.println("Admin login berhasil!");
                sceneVerifikasi(event);
            } else {
                ResultSet rs = authManager.loginUser(username, password);
                if (rs.next()) {
                    System.out.println("Login anggota berhasil!");
                    // Menyimpan data pengguna yang login
                    loggedInNama = rs.getString("nama");
                    loggedInAlamat = rs.getString("alamat");
                    loggedInTanggalLahir = rs.getString("tanggal_lahir");
                    sceneProfile(event);
                } else {
                    System.out.println("Username atau password salah.");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Method untuk mengubah scene ke profile dan meneruskan data pengguna
    public void sceneProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
        root = loader.load();

        // Mendapatkan controller dari scene profile
        sceneController profileController = loader.getController();

        // Meneruskan data pengguna ke controller scene profile
        profileController.setProfileData(loggedInNama, loggedInAlamat, loggedInTanggalLahir);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Method untuk menyetel data profil
    public void setProfileData(String nama, String alamat, String tanggalLahir) {
        ProfileNamaAnggota.setText(nama);
        ProfileAlamatAnggota.setText(alamat);
        ProfileTanggalLahirAnggota.setText(tanggalLahir);
    }

    @FXML
    private void logoutUser(ActionEvent event) throws IOException {
        // Menghapus data pengguna yang sedang login
        loggedInNama = null;
        loggedInAlamat = null;
        loggedInTanggalLahir = null;

        // Kembali ke halaman login
        sceneLogin(event);
    }

    // Method untuk mengubah scene ke BayarPinjaman.fxml
    public void sceneBayarPinjaman(ActionEvent event) throws IOException {
        loadScene(event, "BayarPinjaman.fxml");
    }

    // Method untuk mengubah scene ke Pinjaman.fxml
    public void scenePinjaman(ActionEvent event) throws IOException {
        loadScene(event, "Pinjaman.fxml");
    }

    // Method untuk mengubah scene ke Transaksi.fxml
    public void sceneTransaksi(ActionEvent event) throws IOException {
        loadScene(event, "Transaksi.fxml");
    }

    // Method untuk mengubah scene ke Simpanan.fxml
    public void sceneSimpanan(ActionEvent event) throws IOException {
        loadScene(event, "Simpanan.fxml");
    }

    // Method untuk mengubah scene ke Transfer.fxml
    public void sceneTransfer(ActionEvent event) throws IOException {
        loadScene(event, "Transfer.fxml");
    }

    // Method untuk mengubah scene ke DetailTransaksi.fxml
    public void sceneDetailTransaksi(ActionEvent event) throws IOException {
        loadScene(event, "DetailTransaksi.fxml");
    }

    public void sceneProfileAdmin(ActionEvent event) throws IOException {
        loadScene(event, "ProfileAdmin.fxml");
    }

    public void sceneVerifikasi(ActionEvent event) throws IOException {
        loadScene(event, "VerifikasiPinjam.fxml");
    }

    public void sceneRegister(ActionEvent event) throws IOException {
        loadScene(event, "Register.fxml");
    }

    public void sceneLogin(ActionEvent event) throws IOException {
        loadScene(event, "Login.fxml");
    }

    // Method untuk memuat dan mengubah scene (disederhanakan untuk kemudahan panggilan)
    private void loadScene(ActionEvent event, String fxmlFile) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
