package editeur.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import editeur.H2;
import editeur.Main;
import editeur.Theme;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;

public class SelectionThemeControllers {

	@FXML
	private Button btnRetour;
	@FXML
	private Button btnSupprimer;
	@FXML
	private Button btnModifier;
	@FXML
	private ListView<Label> listeTheme;

	@FXML
	private void initialize() {
		try {
			H2.connexion();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String contenu[] = H2.separer(H2.lire("SELECT Nom_Theme FROM THEME;"));
		if (contenu[0] != "") {
			for (int i = 0; i <= contenu.length - 1; i++) {
				Label unLabel = new Label(H2.nonApos(contenu[i]));
				unLabel.setPrefWidth(listeTheme.getPrefWidth() - 20);
				unLabel.setAlignment(Pos.CENTER);
				unLabel.setFont(Font.font("Lucida Sans Unicode", 20));
				listeTheme.getItems().add(unLabel);
			}
		}
	}

	@FXML
	public void retour() {
		try {
			Main.loadView("view/Accueil_Editeur.fxml");
			Main.primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void supprimer() throws SQLException {
		if (listeTheme.getSelectionModel().getSelectedItem() != null) {
			H2.connexion();
			int idTheme = Integer.parseInt(H2.separer(H2.lire("SELECT Id_Theme FROM THEME WHERE Nom_Theme='"
					+ listeTheme.getSelectionModel().getSelectedItem().getText() + "';"))[0]);
			String path[] = H2
					.separer(H2.lire("SELECT Adr_Image, Adr_Logo FROM THEME WHERE Id_Theme=" + idTheme + ";"));
			for (int i = 0; i < path.length; i++) {
				File src = new File(path[i]);
				if (src.isFile()) { // si le fichier existe
					try {
						Files.delete(Paths.get(path[i]));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			String idQuestion[] = H2
					.separer(H2.lire("SELECT Id_Question FROM QUESTION WHERE Id_Theme=" + idTheme + ";"));
			for (int i = 0; i < idQuestion.length; i++) {
				if (idQuestion[i] == "")
					idQuestion[i] = "0";
				H2.modifier("DELETE FROM COORDONNEE WHERE Id_Question=" + Integer.parseInt(idQuestion[i]) + ";");
			}
			H2.modifier("DELETE FROM QUESTION WHERE Id_Theme=" + idTheme);
			H2.modifier("DELETE FROM THEME WHERE Id_Theme=" + idTheme);
		}
		listeTheme.getItems().clear();
		initialize();
	}

	@FXML
	public void modifier() throws SQLException {
		if (listeTheme.getSelectionModel().getSelectedItem() != null) {
			H2.connexion();
			String contenu[] = H2.separer(H2.lire("SELECT Id_Theme FROM THEME WHERE Nom_Theme='"
					+ H2.apos(listeTheme.getSelectionModel().getSelectedItem().getText()) + "';"));
			int idTheme = Integer.parseInt(contenu[0]);
			Theme.setModifTheme(idTheme);

			try {
				Main.loadView("view/Modification_Theme.fxml");
				Main.primaryStage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			System.out.println("Aucun theme selectionné");
	}
}
