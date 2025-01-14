package UI;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import KoneksiDatabase.DatabaseManager;
import Auth.AuthManager;
import Model.UserData;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class sceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Connection con;
    private AuthManager authManager;

    @FXML private TextField UsernameLogin;
    @FXML private PasswordField PasswordLogin;
    @FXML private Label ProfileNamaAnggota;
    @FXML private Label ProfileAlamatAnggota;
    @FXML private Label ProfileTanggalLahirAnggota;
    @FXML private Label ProfileNamaAdmin;
    @FXML private Label ProfileAlamatAdmin;
    @FXML private Label ProfileTanggalLahirAdmin;
    @FXML private TextField namaField;
    @FXML private DatePicker tanggalLahirField;
    @FXML private TextArea alamatField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField IDNasabahTransfer; 
    @FXML private TextField NamaNasabahTransfer; 
    @FXML private TextField NominalTransfer; 


    public void initialize() {
        try {
            con = DatabaseManager.getConnection();
            authManager = new AuthManager(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void transferAdmin(ActionEvent event) {
        String anggotaID = IDNasabahTransfer.getText();
        double nominal = Double.parseDouble(NominalTransfer.getText());

        String checkQuery = "SELECT COUNT(*) FROM fact_simpanan WHERE Anggota_ID = ?";
        String insertQuery = "INSERT INTO fact_simpanan (Anggota_ID, Total_Simpanan) VALUES (?, ?)";
        String updateQuery = "UPDATE fact_simpanan SET Total_Simpanan = Total_Simpanan + ? WHERE Anggota_ID = ?";

        try (PreparedStatement checkPst = con.prepareStatement(checkQuery)) {
            checkPst.setString(1, anggotaID);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // Jika ID tidak ditemukan, lakukan INSERT
                try (PreparedStatement insertPst = con.prepareStatement(insertQuery)) {
                    insertPst.setString(1, anggotaID);
                    insertPst.setDouble(2, nominal);
                    insertPst.executeUpdate();
                    System.out.println("Data anggota baru ditambahkan ke fact_simpanan.");
                }
            } else {
                // Jika ID ditemukan, lakukan UPDATE
                try (PreparedStatement updatePst = con.prepareStatement(updateQuery)) {
                    updatePst.setDouble(1, nominal);
                    updatePst.setString(2, anggotaID);
                    updatePst.executeUpdate();
                    System.out.println("Nominal simpanan berhasil diperbarui.");
                }
            }
            IDNasabahTransfer.setText(""); 
            NominalTransfer.setText("");
            NamaNasabahTransfer.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void registerUser(ActionEvent event) {
        String insertAnggotaQuery = "INSERT INTO dim_anggota (Nama, Tanggal_Lahir, Alamat, Username, Password) VALUES (?, ?, ?, ?, ?)";
        String insertSimpananQuery = "INSERT INTO fact_simpanan (Anggota_ID, Total_Simpanan) VALUES (?, 0)";
        
        try {
            // Masukkan data ke dim_anggota
            PreparedStatement anggotaPst = con.prepareStatement(insertAnggotaQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            anggotaPst.setString(1, namaField.getText());
            anggotaPst.setDate(2, java.sql.Date.valueOf(tanggalLahirField.getValue()));
            anggotaPst.setString(3, alamatField.getText());
            anggotaPst.setString(4, usernameField.getText());
            anggotaPst.setString(5, passwordField.getText());
            anggotaPst.executeUpdate();

            // Ambil Anggota_ID yang baru saja dimasukkan
            ResultSet rs = anggotaPst.getGeneratedKeys();
            if (rs.next()) {
                int anggotaID = rs.getInt(1);

                // Masukkan entri awal ke fact_simpanan
                PreparedStatement simpananPst = con.prepareStatement(insertSimpananQuery);
                simpananPst.setInt(1, anggotaID);
                simpananPst.executeUpdate();
                System.out.println("Entri awal fact_simpanan dibuat.");
            }
            
            // Pindah ke halaman login
            sceneLogin(event);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    
    @FXML
    private void loginUser(ActionEvent event) {
        String username = UsernameLogin.getText();
        String password = PasswordLogin.getText();

        try {
            ResultSet rs;
            if ((rs = authManager.loginAdmin(username, password)) != null && rs.next()) {
                System.out.println("Admin login berhasil!");
                // Menyimpan data admin yang login ke UserData
                UserData userData = UserData.getInstance();
                userData.setNama(rs.getString("Nama"));
                userData.setAlamat(rs.getString("Alamat"));
                userData.setTanggalLahir(rs.getString("Tanggal_Lahir"));
                sceneProfileAdmin(event);
            } else if ((rs = authManager.loginUser(username, password)) != null && rs.next()) {
                System.out.println("Login anggota berhasil!");
                // Menyimpan data pengguna yang login ke UserData
                UserData userData = UserData.getInstance();
                userData.setNama(rs.getString("nama"));
                userData.setAlamat(rs.getString("alamat"));
                userData.setTanggalLahir(rs.getString("tanggal_lahir"));
                sceneProfile(event);
            } else {
                System.out.println("Username atau password salah.");
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

        // Mengambil data pengguna dari UserData dan memperbarui UI
        UserData userData = UserData.getInstance();
        profileController.setProfileData(userData.getNama(), userData.getAlamat(), userData.getTanggalLahir());

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Method untuk mengubah scene ke profile admin dan meneruskan data admin
    public void sceneProfileAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileAdmin.fxml"));
        root = loader.load();

        // Mendapatkan controller dari scene profile admin
        sceneController profileAdminController = loader.getController();

        // Mengambil data admin dari UserData dan memperbarui UI
        UserData userData = UserData.getInstance();
        profileAdminController.setProfileAdminData(userData.getNama(), userData.getAlamat(), userData.getTanggalLahir());

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Method untuk menyetel data profil anggota
    public void setProfileData(String nama, String alamat, String tanggalLahir) {
        ProfileNamaAnggota.setText(nama);
        ProfileAlamatAnggota.setText(alamat);
        ProfileTanggalLahirAnggota.setText(tanggalLahir);
    }

    // Method untuk menyetel data profil admin
    public void setProfileAdminData(String nama, String alamat, String tanggalLahir) {
        ProfileNamaAdmin.setText(nama);
        ProfileAlamatAdmin.setText(alamat);
        ProfileTanggalLahirAdmin.setText(tanggalLahir);
    }

    @FXML
    private void logoutUser(ActionEvent event) throws IOException {
        // Menghapus data pengguna yang sedang login
        UserData userData = UserData.getInstance();
        userData.setNama(null);
        userData.setAlamat(null);
        userData.setTanggalLahir(null);

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
