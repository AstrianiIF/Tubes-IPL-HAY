package UI;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class sceneController { // Ganti nama kelas dengan kapitalisasi yang benar

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Metode untuk mengubah scene ke BayarPinjaman.fxml
    public void sceneBayarPinjaman(ActionEvent event) throws IOException {
        loadScene(event, "BayarPinjaman.fxml");
    }

    // Metode untuk mengubah scene ke Pinjaman.fxml
    public void scenePinjaman(ActionEvent event) throws IOException {
        loadScene(event, "Pinjaman.fxml");
    }

    // Metode untuk mengubah scene ke Transaksi.fxml
    public void sceneTransaksi(ActionEvent event) throws IOException {
        loadScene(event, "Transaksi.fxml");
    }

    // Metode untuk mengubah scene ke Simpanan.fxml
    public void sceneSimpanan(ActionEvent event) throws IOException {
        loadScene(event, "Simpanan.fxml");
    }
    
    // Metode untuk mengubah scene ke Transfer.fxml
    public void sceneTransfer(ActionEvent event) throws IOException {
    	loadScene(event, "Transfer.fxml");
    }

    // Metode untuk memuat dan mengubah scene (aku ubah dikit biar manggilnya gampang)
    private void loadScene(ActionEvent event, String fxmlFile) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
