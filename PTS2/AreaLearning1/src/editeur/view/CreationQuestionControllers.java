package editeur.view;

import java.sql.SQLException;
import java.util.Optional;

import editeur.H2;
import editeur.Main;
import editeur.Theme;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class CreationQuestionControllers {

	@FXML
	private Label theme;
	@FXML
	private ListView<Label> listeQuestion;
	@FXML
	private Button btnRetour;
	@FXML
	private Button btnAjouter;
	@FXML
	private Button btnSupprimer;
	@FXML
	private Button btnGererZones;
	@FXML
	private ImageView imageFond;
	@FXML
	private TextField question;

	private Alert alert;

	@FXML
	private void initialize() {
		try {
			H2.connexion();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String nomTheme = H2
				.separer(H2.lire("SELECT Nom_Theme FROM THEME WHERE Id_Theme=" + Theme.getModifTheme() + ";"))[0];
		theme.setText(H2.nonApos(theme.getText()) + " " + nomTheme);
		String elements[] = H2
				.separer(H2.lire("SELECT Int_Question FROM QUESTION WHERE Id_Theme=" + Theme.getModifTheme() + ";"));
		if (elements[0] != "") {
			for (int i = 0; i < elements.length; i++) {
				Label unLabel = new Label(H2.nonApos(elements[i]));
				unLabel.setPrefWidth(listeQuestion.getPrefWidth());
				unLabel.setAlignment(Pos.CENTER);
				unLabel.setFont(Font.font("Lucida Sans Unicode", 15));
				listeQuestion.getItems().add(unLabel);
			}
		}
	}

	@FXML
	public void tailleMaxTextField() {
		int max = 50;
		if (question.getText().length() > max) {
			question.deleteText(max, question.getText().length());
		}
		if (question.getText().contains("~")) question.setText(question.getText().replaceAll("~", ""));
		if (question.getText().contains("\"")) question.setText(question.getText().replaceAll("\"", ""));
	}

	@FXML
	public void retour() {
		if (listeQuestion.getItems().size() >= 5 && checkReponse()) {
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("En ligne ?");
			alert.setHeaderText("Toutes les données ont été sauvegardé.");
			alert.setContentText("Voulez vous que le theme soit disponible dans le jeu ?");
			ButtonType btnOui = new ButtonType("Oui");
			ButtonType btnNon = new ButtonType("Non");
			alert.getButtonTypes().setAll(btnOui, btnNon);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == btnOui) {
				try {
					H2.connexion();
					H2.modifier("UPDATE THEME SET Actif_Theme='true', Nb_Question=" + listeQuestion.getItems().size()
							+ "  WHERE Id_Theme=" + Theme.getModifTheme() + ";");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					H2.connexion();
					H2.modifier("UPDATE THEME SET Actif_Theme='false', Nb_Question=" + listeQuestion.getItems().size()
							+ "  WHERE Id_Theme=" + Theme.getModifTheme() + ";");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		else {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sauvegardé");
			alert.setContentText("Les éventuelles modifications ont été sauvegardé.");
			alert.showAndWait();
		}
		try {
			Main.loadView("view/Accueil_Editeur.fxml");
			Main.primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean checkReponse() {
		boolean ok = true;
		try {
			H2.connexion();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String elements[] = H2
				.separer(H2.lire("SELECT Id_Question FROM QUESTION WHERE Id_Theme=" + Theme.getModifTheme() + ";"));
		String zone;
		for (int i = 0; i < elements.length; i++) {
			zone = H2.separer(H2.lire("SELECT Id_Zone FROM COORDONNEE WHERE Id_Question=" + elements[i] + ";"))[0];
			if (zone == "")
				ok = false;
		}
		return ok;
	}

	@FXML
	public void ajouterQuestion() {
		question.setText(question.getText().trim());
		boolean existant = false;
		for (int i = 0; i <= listeQuestion.getItems().size() - 1; i++) {
			if (question.getText().equals(listeQuestion.getItems().get(i).getText()))
				existant = true;
		}
		if (question.getText().length()!=0 && !existant) {
			Label unLabel = new Label(question.getText());
			unLabel.setPrefWidth(listeQuestion.getPrefWidth());
			unLabel.setAlignment(Pos.CENTER);
			unLabel.setFont(Font.font("Lucida Sans Unicode", 15));
			listeQuestion.getItems().add(unLabel);

			int[] numQuestion = new int[1];
			numQuestion[0] = 0;
			if (H2.lire(
					"SELECT MAX(Num_Question) FROM QUESTION WHERE Id_Theme = " + Theme.getModifTheme() + ";") != "") {

				numQuestion[0] = Integer.parseInt(H2.separer(H2.lire(
						"SELECT MAX(Num_Question) FROM QUESTION WHERE Id_Theme = " + Theme.getModifTheme() + ";"))[0]);
			}
			try {
				H2.connexion();
				H2.modifier("INSERT INTO QUESTION(Num_Question, Int_Question, Id_THEME) VALUES (" + (++numQuestion[0])
						+ ", '" + H2.apos(question.getText()) + "', " + Theme.getModifTheme() + ");");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
			alert.setTitle("Erreur, intitulé de question");
			alert.setContentText("Intitulé de question existant ou nul.");
			alert.showAndWait();
		}
		question.setText("");
		listeQuestion.getSelectionModel().select(-1); // tout déselectionner
	}

	@FXML
	public void detectList() {
		if (listeQuestion.getSelectionModel().getSelectedItem() != null) {
			question.setText(listeQuestion.getSelectionModel().getSelectedItem().getText());
		}
	}

	@FXML
	public void modifierQuestion() {
		boolean existant = false;
		for (int i = 0; i <= listeQuestion.getItems().size() - 1; i++) {
			if (question.getText().equals(listeQuestion.getItems().get(i).getText()))
				existant = true;
		}
		if (listeQuestion.getSelectionModel().getSelectedItem() != null && !existant && !question.getText().isEmpty()) {
			try {
				H2.connexion();
				int idQ = Integer.parseInt(H2.separer(H2.lire("SELECT Id_Question FROM QUESTION WHERE Id_Theme = "
						+ Theme.getModifTheme() + " AND Int_Question ='"
						+ H2.apos(listeQuestion.getSelectionModel().getSelectedItem().getText()) + "';"))[0]);
				H2.modifier("UPDATE QUESTION SET Int_Question='" + H2.apos(question.getText()) + "' WHERE Id_THEME = "
						+ Theme.getModifTheme() + " AND Id_Question = " + idQ + ";");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listeQuestion.getItems().remove(listeQuestion.getSelectionModel().getSelectedItem());

			Label unLabel = new Label(question.getText());
			unLabel.setPrefWidth(listeQuestion.getPrefWidth());
			unLabel.setAlignment(Pos.CENTER);
			unLabel.setFont(Font.font("Lucida Sans Unicode", 15));
			listeQuestion.getItems().add(unLabel);
		}

		listeQuestion.getSelectionModel().select(-1); // tout déselectionner
		question.setText(null);
	}

	@FXML
	public void supprimerQuestion() {
		if (listeQuestion.getSelectionModel().getSelectedItem() != null) {
			try {
				H2.connexion();
				int idQ = Integer.parseInt(H2.separer(H2.lire("SELECT Id_Question FROM QUESTION WHERE Id_Theme = "
						+ Theme.getModifTheme() + " AND Int_Question ='"
						+ H2.apos(listeQuestion.getSelectionModel().getSelectedItem().getText()) + "';"))[0]);
				H2.modifier("DELETE QUESTION WHERE Id_THEME = " + Theme.getModifTheme() + " AND Id_Question = " + idQ
						+ ";");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			listeQuestion.getItems().remove(listeQuestion.getSelectionModel().getSelectedItem());
			question.setText(null);
		}
		listeQuestion.getSelectionModel().select(-1); // tout déselectionner
	}

	@FXML
	public void gererZonesReponse() {
		if (!listeQuestion.getItems().isEmpty()) {
			try {
				Main.loadView("view/Modification_Reponse.fxml");
				Main.primaryStage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
			alert.setTitle("Erreur Liste Questions");
			alert.setContentText("Vous devez ajouter au moins une question pour continuer");
			alert.showAndWait();
		}
	}
}
