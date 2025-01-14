package UI;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
        loadScene(event,"DetailTransaksi.fxml");
    }
    
    public void sceneProfile(ActionEvent event) throws IOException {
        loadScene(event,"Profile.fxml");
    }
    
    public void sceneProfileAdmin(ActionEvent event) throws IOException {
        loadScene(event,"ProfileAdmin.fxml");
    }
    
    public void sceneVerifikasi(ActionEvent event) throws IOException {
        loadScene(event,"VerifikasiPinjam.fxml");
    }

    public void sceneRegister(ActionEvent event) throws IOException {
        loadScene(event,"Register.fxml");
    }
    
    public void sceneLogin(ActionEvent event) throws IOException {
        loadScene(event,"Login.fxml");
    }
    
    // Method to load and change the scene (simplified for easier calls)
    private void loadScene(ActionEvent event, String fxmlFile) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
