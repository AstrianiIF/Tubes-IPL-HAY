package UI;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class sceneController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void sceneBayarPinjaman(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("BayarPinjaman.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void scenePinjaman(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Pinjaman.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void sceneTransaksi(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Transaksi.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

