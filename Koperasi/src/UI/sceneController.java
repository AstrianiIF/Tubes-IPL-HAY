package UI;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class sceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Connection con;

    @FXML private TextField namaField;
    @FXML private DatePicker tanggalLahirField;
    @FXML private TextArea alamatField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField UsernameLogin;
    @FXML private PasswordField PasswordLogin;

    public void initialize() {
        try {
            String url = "jdbc:mysql://localhost:3306/koperasi"; 
            String user = "root";
            String pass = "";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loginUser(ActionEvent event) { 
        String username = UsernameLogin.getText(); 
        String password = PasswordLogin.getText(); 

        String adminQuery = "SELECT * FROM dim_admin WHERE Username = ? AND Password = ?";
        String anggotaQuery = "SELECT * FROM dim_anggota WHERE username = ? AND password = ?";

        try (PreparedStatement adminPst = con.prepareStatement(adminQuery)) { 
            adminPst.setString(1, username); 
            adminPst.setString(2, password); 
            ResultSet adminRs = adminPst.executeQuery(); 

            if (adminRs.next()) { 
                System.out.println("Admin login successful!"); 
                // Redirect to the admin main application scene after successful login 
                sceneVerifikasi(event); 
            } else { 
                try (PreparedStatement anggotaPst = con.prepareStatement(anggotaQuery)) { 
                    anggotaPst.setString(1, username); 
                    anggotaPst.setString(2, password); 
                    ResultSet anggotaRs = anggotaPst.executeQuery(); 

                    if (anggotaRs.next()) { 
                        System.out.println("Member login successful!"); 
                        // Redirect to the member main application scene after successful login 
                        sceneSimpanan(event); 
                    } else { 
                        System.out.println("Invalid username or password."); 
                    } 
                }
            } 
        } catch (SQLException | IOException e) { 
            e.printStackTrace(); 
        } 
    }

    @FXML
    private void registerUser(ActionEvent event) {
        String insertQuery = "INSERT INTO dim_anggota (nama, tanggal_lahir, alamat, username, password) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(insertQuery)) {
            pst.setString(1, namaField.getText());
            pst.setDate(2, java.sql.Date.valueOf(tanggalLahirField.getValue()));
            pst.setString(3, alamatField.getText());
            pst.setString(4, usernameField.getText());
            pst.setString(5, passwordField.getText());

            pst.executeUpdate();
            System.out.println("User registered successfully!");
            // After successful registration, redirect to login
            sceneLogin(event);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Method to change scene to BayarPinjaman.fxml
    public void sceneBayarPinjaman(ActionEvent event) throws IOException {
        loadScene(event, "BayarPinjaman.fxml");
    }

    // Method to change scene to Pinjaman.fxml
    public void scenePinjaman(ActionEvent event) throws IOException {
        loadScene(event, "Pinjaman.fxml");
    }

    // Method to change scene to Transaksi.fxml
    public void sceneTransaksi(ActionEvent event) throws IOException {
        loadScene(event, "Transaksi.fxml");
    }

    // Method to change scene to Simpanan.fxml
    public void sceneSimpanan(ActionEvent event) throws IOException {
        loadScene(event, "Simpanan.fxml");
    }

    // Method to change scene to Transfer.fxml
    public void sceneTransfer(ActionEvent event) throws IOException {
        loadScene(event, "Transfer.fxml");
    }

    // Method to change scene to DetailTransaksi.fxml
    public void sceneDetailTransaksi(ActionEvent event) throws IOException {
        loadScene(event, "DetailTransaksi.fxml");
    }

    public void sceneProfile(ActionEvent event) throws IOException {
        loadScene(event, "Profile.fxml");
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

    // Method to load and change the scene (simplified for easier calls)
    private void loadScene(ActionEvent event, String fxmlFile) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
