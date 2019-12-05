package application.view;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class AccueilControllers {

	@FXML
	private Button btnJouer;

	@FXML
	private Button btnQuitter;

	@FXML
	private Button btnCredits;

	@FXML
	private Button btnParametres;

	@FXML
	private Button btnScore;

	@FXML
	private void jouer() {
		try {
			FXMLLoader rootFXML = new FXMLLoader(getClass().getResource("../view/PageSelectionTheme.fxml"));
			BorderPane root = rootFXML.load();
			Scene scene = new Scene(root);
			Main.primaryStage.setScene(scene);
			Main.primaryStage.centerOnScreen();
			Main.primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void score() {
		try {
			FXMLLoader rootFXML = new FXMLLoader(getClass().getResource("../view/PageRecords.fxml"));
			BorderPane root = rootFXML.load();
			Scene scene = new Scene(root);
			Main.primaryStage.setScene(scene);
			Main.primaryStage.centerOnScreen();
			Main.primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void quitter() {
		Main.primaryStage.close();
	}
}
