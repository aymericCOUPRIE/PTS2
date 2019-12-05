package application.view;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import application.Main;
import application.Score;
import application.Theme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;

public class FinPartieControllers {

	@FXML
	private Button btnSauvegarderRecords;

	@FXML
	private Label lblPseudo = new Label("Pseudo");

	@FXML
	private Label lblNomTheme;

	@FXML
	private Label lblScore;

	@FXML
	private Button btnMenu;

	@FXML
	private javafx.scene.control.TextField tfdPseudo = new javafx.scene.control.TextField();

	Score scoreDeLaPartie;

	@FXML
	private void sauvegarderRecords() throws SQLException {
		TextInputDialog pseudoDialog = new TextInputDialog("Pseudo");
		pseudoDialog.setTitle("Choisissez un Pseudo");
		pseudoDialog.setContentText("Pseudo:");
		Optional<String> textIn = pseudoDialog.showAndWait();
		if (textIn.isPresent()) {
			JeuControllers.getScoreDeLaPartie().save(textIn.get());
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
	}

	@FXML
	private void Menu() {
		try {
			FXMLLoader rootFXML = new FXMLLoader(getClass().getResource("../view/PageAccueil.fxml"));
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
	private void initialize() {
		lblNomTheme.setText(Theme.themeEnCours.getNom());
		lblScore.setText(String.valueOf(JeuControllers.getScoreDeLaPartie().getScore()));

	}

}
