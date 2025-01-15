package UI;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.sql.ResultSet;
import KoneksiDatabase.DatabaseManager;
import Auth.AuthManager;
import Model.UserData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class sceneController {

    private static final Labeled TextTotalSimpanan = null;
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
    @FXML private Label TotalSimpananAnggota; 
    @FXML private TextField namaField;
    @FXML private DatePicker tanggalLahirField;
    @FXML private TextArea alamatField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField IDNasabahTransfer; 
    @FXML private TextField NamaNasabahTransfer; 
    @FXML private TextField NominalTransfer; 
    @FXML private Label TextTotalPinjaman;
    @FXML private Label TextPadaSimpanan;
    @FXML private TextField pinjamField;
    @FXML private TextField bayarField;
    @FXML private Button Buttonbayar;
    
    public void initialize() {
        try {
            con = DatabaseManager.getConnection();
            authManager = new AuthManager(con);
            updateTotalPinjaman();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void updateTotalPinjaman() {
        UserData userData = UserData.getInstance();
        int anggotaID = userData.getAnggotaID();

        try (Connection con = DatabaseManager.getConnection()) {
            String query = "SELECT COALESCE(SUM(Total_Pinjaman), 0) AS total FROM fact_transaksi_pinjaman WHERE Anggota_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, anggotaID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                BigDecimal totalPinjaman = rs.getBigDecimal("total");

                // Simpan ke UserData
                userData.setTotalPinjaman(totalPinjaman);

                // Update Label di UI Thread
                Platform.runLater(() -> TextTotalPinjaman.setText("" + totalPinjaman));
            } else {
                // Jika tidak ada data, set nilai default di UserData
                userData.setTotalPinjaman(BigDecimal.ZERO);
                Platform.runLater(() -> TextTotalPinjaman.setText("0"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching total loan amount: " + e.getMessage());
            Platform.runLater(() -> TextTotalPinjaman.setText("Error"));
        }
    }

    public void HitungPembayaran(ActionEvent event) {
        try {
            // Ambil Anggota_ID dari UserData singleton
            int anggotaID = UserData.getInstance().getAnggotaID();
            
            // Ambil nilai pembayaran dari TextField
            if (bayarField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Nominal pembayaran tidak boleh kosong");
                alert.showAndWait();
                return;
            }
            
            BigDecimal bayarNominal = new BigDecimal(bayarField.getText());
            
            // Koneksi ke database dan ambil Total_Pinjaman saat ini
            Connection conn = DatabaseManager.getConnection();
            String querySelect = "SELECT Total_Pinjaman FROM fact_transaksi_pinjaman WHERE Anggota_ID = ?";
            PreparedStatement pstSelect = conn.prepareStatement(querySelect);
            pstSelect.setInt(1, anggotaID);
            ResultSet rs = pstSelect.executeQuery();
            
            if (rs.next()) {
                BigDecimal currentLoan = rs.getBigDecimal("Total_Pinjaman");
                
                // Hitung sisa pinjaman
                BigDecimal newLoanAmount = currentLoan.subtract(bayarNominal);
                
                // Update Total_Pinjaman di database
                String queryUpdate = "UPDATE fact_transaksi_pinjaman SET Total_Pinjaman = ? WHERE Anggota_ID = ?";
                PreparedStatement pstUpdate = conn.prepareStatement(queryUpdate);
                pstUpdate.setBigDecimal(1, newLoanAmount);
                pstUpdate.setInt(2, anggotaID);
                pstUpdate.executeUpdate();
                
                // Update UserData singleton
                UserData.getInstance().setTotalPinjaman(newLoanAmount);
                
                // Tampilkan pesan sukses
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Pembayaran Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Pembayaran berhasil!\nSisa pinjaman: Rp " + newLoanAmount);
                alert.showAndWait();
                
                // Bersihkan field
                bayarField.clear();
                
                // Tutup PreparedStatement
                pstUpdate.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Data pinjaman tidak ditemukan");
                alert.showAndWait();
            }
            
            // Tutup resources
            rs.close();
            pstSelect.close();
            conn.close();
            
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Masukkan nominal pembayaran yang valid");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error database: " + e.getMessage());
            alert.showAndWait();
        }
    }

    
    private void updateTotalSimpanan() {
        UserData userData = UserData.getInstance();
        int anggotaID = userData.getAnggotaID();

        try (Connection con = DatabaseManager.getConnection()) {
            String query = "SELECT COALESCE(SUM(Total_Simpan), 0) AS total FROM fact_simpanan WHERE Anggota_ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, anggotaID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                BigDecimal totalSimpanan = rs.getBigDecimal("total");

                // Simpan ke UserData
                userData.setTotalSimpanan(totalSimpanan);

                // Update Label di UI Thread
                Platform.runLater(() -> TextPadaSimpanan.setText("" + totalSimpanan)); // Updated fx:id
            } else {
                // Jika tidak ada data, set nilai default di UserData
                userData.setTotalSimpanan(BigDecimal.ZERO);
                Platform.runLater(() -> TextPadaSimpanan.setText("0")); // Updated fx:id
            }
        } catch (SQLException e) {
            System.err.println("Error fetching total saving amount: " + e.getMessage());
            Platform.runLater(() -> TextPadaSimpanan.setText("Error")); // Updated fx:id
        }
    }


    @FXML
    private void handleButtonPinjam() {
        String nominal = pinjamField.getText();
        if (nominal.isEmpty()) {
            System.out.println("Please enter a nominal amount.");
            return;
        }

        try {
            int amount = Integer.parseInt(nominal);
            saveLoanAmount(amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid nominal amount.");
        }
    }

    private void saveLoanAmount(int amount) {
        UserData userData = UserData.getInstance();
        int anggotaID = userData.getAnggotaID();

        try (Connection con = DatabaseManager.getConnection()) {
            String query = "INSERT INTO fact_transaksi_pinjaman (Anggota_ID, Total_Pinjaman) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, anggotaID);
            pst.setBigDecimal(2, BigDecimal.valueOf(amount));
            pst.executeUpdate();

            System.out.println("Loan amount saved successfully for user ID: " + anggotaID);
        } catch (SQLException e) {
            System.err.println("Error saving loan amount: " + e.getMessage());
        }
    }
    
    public void loadTotalDataSimpanan(int AnggotaID, ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BayarPinjaman.fxml"));
        try {
            Parent root = loader.load();
            sceneController controller = loader.getController();

            if (controller == null) {
                System.out.println("Controller is null");
                return;
            }

            int anggotaID = UserData.getInstance().getAnggotaID();
            String query = "SELECT Total_Simpanan FROM fact_simpanan WHERE Anggota_ID = ?";
            System.out.println("Executing query for Anggota_ID: " + anggotaID);

            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, anggotaID);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    double totalSimpanan = rs.getDouble("Total_Simpanan");
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    String formattedSimpanan = formatter.format(totalSimpanan);

                    Platform.runLater(() -> {
                        if (controller.TextPadaSimpanan != null) {
                            controller.TextPadaSimpanan.setText(formattedSimpanan);
                        } else {
                            System.out.println("TextPadaSimpanan Label is null");
                        }
                    });
                } else {
                    System.out.println("No data found for Anggota_ID: " + anggotaID);
                    Platform.runLater(() -> {
                        if (controller.TextPadaSimpanan != null) {
                            controller.TextPadaSimpanan.setText("0.00");
                        } else {
                            System.out.println("TextPadaSimpanan Label is null");
                        }
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            if (stage != null) {
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("Stage is null!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    public void loadTotalSimpanan(int AnggotaID, ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Simpanan.fxml"));
        try {
            Parent root = loader.load();
            sceneController controller = loader.getController();

            if (controller == null) {
                System.out.println("Controller is null");
                return;
            }

            int anggotaID = UserData.getInstance().getAnggotaID();
            String query = "SELECT Total_Simpanan FROM fact_simpanan WHERE Anggota_ID = ?";
            System.out.println("Executing query for Anggota_ID: " + anggotaID);

            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, anggotaID);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    double totalSimpanan = rs.getDouble("Total_Simpanan");
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    String formattedSimpanan = formatter.format(totalSimpanan);

                    Platform.runLater(() -> {
                        if (controller.TotalSimpananAnggota != null) {
                            controller.TotalSimpananAnggota.setText(formattedSimpanan);
                        } else {
                            System.out.println("TotalSimpananAnggota Label is null");
                        }
                    });
                } else {
                    System.out.println("No data found for Anggota_ID: " + anggotaID);
                    Platform.runLater(() -> {
                        if (controller.TotalSimpananAnggota != null) {
                            controller.TotalSimpananAnggota.setText("0.00");
                        } else {
                            System.out.println("TotalSimpananAnggota Label is null");
                        }
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            if (stage != null) {
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("Stage is null!");
            }

        } catch (IOException e) {
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
                int anggotaID = rs.getInt("Anggota_ID");
                userData.setAnggotaID(anggotaID);
                loadTotalSimpanan(anggotaID,event);
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

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (stage != null) {
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Stage is null!");
        }
    }
    
    public void sceneBayarPinjaman(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BayarPinjaman.fxml"));
        Parent root = loader.load();
        sceneController controller = loader.getController();
        
        int anggotaID = UserData.getInstance().getAnggotaID();
        controller.loadTotalDataSimpanan(anggotaID, event);
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (stage != null) {
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Stage is null!");
        }
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

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (stage != null) {
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Stage is null!");
        }
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
    
    @FXML
    public void sceneSimpanan(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Simpanan.fxml"));
        Parent root = loader.load();
        sceneController controller = loader.getController();

        int anggotaID = UserData.getInstance().getAnggotaID();
        controller.loadTotalSimpanan(anggotaID, event);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (stage != null) {
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Stage is null!");
        }
    }
    

    // Method untuk mengubah scene ke Pinjaman.fxml
    public void scenePinjaman(ActionEvent event) throws IOException {
        loadScene(event, "Pinjaman.fxml");
    }

    // Method untuk mengubah scene ke Transaksi.fxml
    public void sceneTransaksi(ActionEvent event) throws IOException {
        loadScene(event, "Transaksi.fxml");
    }

    // Method untuk mengubah scene ke Transfer.fxml
    public void sceneTransfer(ActionEvent event) throws IOException {
        loadScene(event, "Transfer.fxml");
    }

//    public void sceneVerifikasi(ActionEvent event) throws IOException {
//        loadScene(event, "VerifikasiPinjam.fxml");
//    }

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
