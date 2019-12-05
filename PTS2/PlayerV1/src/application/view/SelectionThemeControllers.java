package application.view;

import java.io.IOException;
import java.sql.SQLException;

import application.Main;
import application.Theme;
import bdd.H2.H2;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SelectionThemeControllers {

	@FXML
	private Button btnRetour;

	@FXML
	private Button btnConfirmer;

	@FXML
	private ListView<Label> lvThemes;

	@FXML
	private RadioButton rbDiff;

	@FXML
	private TextArea taDescThemeSelectionne;

	private static final String TEXTE_DIFFICULTE_MOYEN = "\n\nMoyen :\nIl suffit de selectionnez une unique réponse\n "
			+ "correcte pour obtenir le point de la question.";
	private static final String TEXTE_DIFFICULTE_DIFFICILE = "\n\nDifficile :\nIl faut selectionnez toutes les reponses\n "
			+ "correctes pour obtenir le point de la question.";
	private int idTheme;
	private Label themeTri;
	private String diffTheme;
	private String[] listeLogoTheme;
	private String[] listeNomTheme;
	private String[] listeNbQuestionTheme;
	private String[] listeDescriptionTheme;

	@FXML
	private void retour() {
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
		// Affichage des themes dans la ListView
		listeLogoTheme =
				H2.nonApos(H2.lire("SELECT adr_logo FROM Theme WHERE Actif_Theme = 'true' ORDER BY Nom_Theme ASC;")).split(";");

		listeNomTheme =
				H2.lire("SELECT Nom_Theme FROM Theme WHERE Actif_Theme = 'true' ORDER BY Nom_Theme ASC;").split(";");

		listeNbQuestionTheme =
				H2.lire("SELECT Nb_Question FROM Theme WHERE Actif_Theme = 'true' ORDER BY Nom_Theme ASC;").split(";");

		listeDescriptionTheme =
				H2.nonApos(H2.lire("SELECT Desc_Theme FROM Theme WHERE Actif_Theme = 'true' ORDER BY Nom_Theme ASC;")).split(";");

		for (int i = 0; i < listeNomTheme.length; i++) {
			themeTri = new Label();
			themeTri.setText(listeNomTheme[i] + "\t\t\t\t" + listeNbQuestionTheme[i]);
			lvThemes.getItems().add(themeTri);
		}

		themeTri.setPrefWidth(lvThemes.getPrefWidth() - 20);
		themeTri.setTextAlignment(TextAlignment.CENTER);
		themeTri.setFont(Font.font("Lucida Sans Unicode", 10));

		lvThemes.addEventHandler(MouseEvent.MOUSE_CLICKED, majDescTheme);
		//rbDiff.getToggleGroup().getSelectedToggle()
	}

	EventHandler<MouseEvent> majDescTheme = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			int index = lvThemes.getSelectionModel().getSelectedIndex();
			if(((RadioButton) rbDiff.getToggleGroup().getSelectedToggle()).getText().equals("Moyen")) {
				taDescThemeSelectionne.setText(listeDescriptionTheme[index].toString() + TEXTE_DIFFICULTE_MOYEN);
			}else if(((RadioButton) rbDiff.getToggleGroup().getSelectedToggle()).getText().equals("Difficile")) {
				taDescThemeSelectionne.setText(listeDescriptionTheme[index].toString() + TEXTE_DIFFICULTE_DIFFICILE);
			}
		}
	};

	@FXML
	private void confirmer() {
		String[] id = null;
		try {
			id = H2.lire("SELECT Id_Theme FROM THEME WHERE Actif_Theme = 'true' ORDER BY Nom_Theme ASC;").split(";");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		idTheme = Integer.parseInt(id[lvThemes.getSelectionModel().getSelectedIndex()]);
		diffTheme = ((RadioButton) rbDiff.getToggleGroup().getSelectedToggle()).getText();
		System.out.println("difficulté: " + diffTheme);
		Theme.themeEnCours = new Theme(idTheme, diffTheme);
		Theme.themeEnCours.info();
		try {
			FXMLLoader rootFXML = new FXMLLoader(getClass().getResource("../view/PageJeuFacileIHM.fxml"));
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
