module Koperasi {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires java.sql;
	exports UI to javafx.fxml;
	
	opens UI to javafx.fxml;
	opens application to javafx.graphics, javafx.fxml;
}
