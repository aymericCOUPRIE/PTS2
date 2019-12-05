package application.view;

import java.io.IOException;
import java.sql.SQLException;

import application.Main;
import bdd.H2.H2;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class RecordsControllers {

	@FXML
	public Button btnMenu;

	@FXML
	public ComboBox<String> cbTriTheme;

	private String[] listeScores;

	private String[] listePseudo;
	
	@FXML
	private ListView<Label> lvRecords;
	
	private Label scoreTri;

	private boolean premierPassage = true;
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
	private void initialize() throws SQLException {
		lvRecords.getItems().clear();
		String[] listeNomTheme = H2.lire("SELECT Nom_Theme FROM THEME WHERE Actif_Theme = 'true';").split(";");

		if(premierPassage) {
		cbTriTheme.getItems().addAll(listeNomTheme);
		cbTriTheme.getSelectionModel().selectFirst();
		premierPassage = false;
		}

		listeScores = H2.lire("SELECT score_joueur FROM SCORE NATURAL JOIN THEME DESC WHERE Nom_Theme = '"
				+ cbTriTheme.getSelectionModel().getSelectedItem() + "' ORDER BY score_joueur;").split(";");

		listePseudo = H2.lire("SELECT pseudo_joueur FROM SCORE NATURAL JOIN THEME DESC WHERE Nom_Theme = '"
				+ cbTriTheme.getSelectionModel().getSelectedItem() + "' ORDER BY score_joueur;").split(";");

		for (int i = 0; i < listePseudo.length; i++) {
			scoreTri = new Label();
			scoreTri.setText(listePseudo[i] + "\t\t|\t\t" + listeScores[i]);
			lvRecords.getItems().add(scoreTri);
		}
		//lvRecords.getItems().add(scoreTri);
		scoreTri.setPrefWidth(lvRecords.getPrefWidth() - 20);
		scoreTri.setTextAlignment(TextAlignment.CENTER);
		scoreTri.setAlignment(Pos.CENTER);
		scoreTri.setFont(Font.font("Lucida Sans Unicode", 20));

		cbTriTheme.addEventHandler(MouseEvent.ANY, majListeRecords);
	}
	
	EventHandler<MouseEvent> majListeRecords = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			try {
				initialize();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	};
}
