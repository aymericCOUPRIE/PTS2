package editeur.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import editeur.H2;
import editeur.Main;
import editeur.Question;
import editeur.Theme;
import editeur.Zone;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class ModificationReponseControllers {

	@FXML
	private ComboBox<String> listeQuestion;
	@FXML
	private Button btnRetour;
	@FXML
	private Button btnAjouter;
	@FXML
	private Button btnSauvegarder;
	@FXML
	private Button btnSupprimer;
	@FXML
	private Button btnConfirmer;
	@FXML
	private Button btnAnnuler;
	@FXML
	private AnchorPane paneImage;

	private Zone uneZone;
	private Image imageFond;
	private double imageWidth;
	private double imageHeight;
	private List<Question> lesQuestions;
	private Question uneQuestion;
	private Alert alert;

	@FXML
	private void initialize() {
		try {
			H2.connexion();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		lesQuestions = new ArrayList<Question>();
		String elements[] = H2.separer(H2.lire("SELECT * FROM QUESTION WHERE Id_Theme=" + Theme.getModifTheme() + ";"));
		for (int i = 0; i < elements.length; i = i + 4) {
			lesQuestions.add(new Question(Integer.parseInt(elements[i]), Integer.parseInt(elements[i + 1]),
					H2.nonApos(elements[i + 2]), Integer.parseInt(elements[i + 3])));
		}
		for (Question uneQuestion : lesQuestions) {
			listeQuestion.getItems().add(uneQuestion.getIntitule());
		}
		listeQuestion.setValue(listeQuestion.getItems().get(0));
		try {
			ajouterImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Erreur dans la réduction de la taille de l'image");
		}
		btnConfirmer.setDisable(true);
		btnAnnuler.setDisable(true);
		for (Question uneQuestion : lesQuestions) {
			String sMaxIdZone = H2.separer(H2.lire(
					"SELECT MAX(Id_Zone) FROM COORDONNEE WHERE Id_Question=" + uneQuestion.getIdQuestion() + ";"))[0];
			int maxIdZone;
			if (sMaxIdZone == "")
				maxIdZone = 0;
			else
				maxIdZone = Integer.parseInt(sMaxIdZone);
			for (int i = 1; i <= maxIdZone; i++) {
				String coor[] = H2
						.separer(H2.lire("SELECT Coor_Zone_X, Coor_Zone_Y FROM COORDONNEE WHERE Id_Question = "
								+ uneQuestion.getIdQuestion() + " AND Id_Zone=" + i + ";"));
				Zone aZone = new Zone(i);
				for (int j = 0; j < coor.length; j++) {
					aZone.getLaZone().getPoints().add(Double.parseDouble(coor[j]));
				}
				uneQuestion.getListeZone().add(aZone);
			}
		}
		changementQuestion();
	}

	@FXML
	public void retour() {
		try {
			Main.loadView("view/Creation_Question.fxml");
			Main.primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void ajouterPoint(MouseEvent coord) { // verifier qu'il n'existe pas
		if (!btnConfirmer.isDisable() && !btnAnnuler.isDisable() && !btnAjouter.isDisable()) {
			uneZone.ajouterPoint(coord.getX(), coord.getY());
			Circle unCercle = new Circle(coord.getX(), coord.getY(), 3d, Color.RED);
			paneImage.getChildren().add(unCercle);
			if (uneZone.getLaZone().getPoints().size() == 6) { // afficher la zone à partir de 3 points
				paneImage.getChildren().add(uneZone.getLaZone());
			}
			for (Zone uneZone : uneQuestion.getListeZone()) {
				Shape intersect = Shape.intersect(unCercle, uneZone.getLaZone());
				if (intersect.getBoundsInLocal().getWidth() != -1) { // si collision
					alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 points.
					alert.setTitle("Impossible de créer la zone");
					alert.setContentText("Les différentes zones ne peuvent pas avoir de points communs.");
					alert.showAndWait();
					annulerZone();
				}
			}
		} else if (!btnConfirmer.isDisable() && !btnAnnuler.isDisable() && !btnSupprimer.isDisable()) {
			for (Zone uneZone : uneQuestion.getListeZone()) {
				Circle unPoint = new Circle(coord.getX(), coord.getY(), 3d, Color.TRANSPARENT);
				paneImage.getChildren().add(unPoint);
				Shape intersect = Shape.intersect(unPoint, uneZone.getLaZone());
				if (intersect.getBoundsInLocal().getWidth() != -1) {
					uneZone.getLaZone().setStroke(Color.RED);
				}
			}
		}
	}

	@FXML
	public void annulerZone() {
		// activer les boutons pour ajouter ou supprimer une zone
		btnAjouter.setDisable(false);
		btnSupprimer.setDisable(false);
		// desactiver les boutons pour confirmer ou annuler la création de la zone
		btnConfirmer.setDisable(true);
		btnAnnuler.setDisable(true);
		dessinerZone();
	}

	private void dessinerZone() {
		paneImage.getChildren().clear();
		for (Zone uneZone : uneQuestion.getListeZone()) {
			uneZone.getLaZone().setStroke(Color.BLACK);
			paneImage.getChildren().add(uneZone.getLaZone());
			for (int i = 0; i < uneZone.getLaZone().getPoints().size(); i = i + 2)
				paneImage.getChildren().add(new Circle(uneZone.getLaZone().getPoints().get(i),
						uneZone.getLaZone().getPoints().get(i + 1), 3d, Color.RED));
		}
	}

	@FXML
	public void ajouterZone() {
		if (!btnSupprimer.isDisable()) {
			// desactiver le bouton pour supprimer une zone
			btnSupprimer.setDisable(true);
			// activer les boutons pour confirmer ou annuler la création de la zone ou
			// ajouter une zone
			btnAjouter.setDisable(false);
			btnConfirmer.setDisable(false);
			btnAnnuler.setDisable(false);
			uneZone = new Zone();
		}
	}

	@FXML
	public void supprimerZone() {
		// desactiver les boutons pour ajouter ou supprimer une zone
		btnAjouter.setDisable(true);
		// activer les boutons pour confirmer ou annuler la suppression de la zone
		btnConfirmer.setDisable(false);
		btnAnnuler.setDisable(false);
		btnSupprimer.setDisable(false);
	}

	@FXML
	public void changementQuestion() {
		for (Question uneAutreQuestion : lesQuestions) {
			if (uneAutreQuestion.getIntitule().equals(listeQuestion.getValue())) {
				uneQuestion = uneAutreQuestion;
				dessinerZone();
			}
		}
	}

	@FXML
	public void confirmerZone() {
		if (!btnAjouter.isDisable()) { // si on ajoute une zone
			if (uneZone.getLaZone().getPoints().size() >= 6) {
				uneZone.save(uneQuestion.getIdQuestion());
				uneQuestion.getListeZone().add(uneZone);
				collision();
			} else {
				alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 points.
				alert.setTitle("Impossible de créer la zone");
				alert.setContentText("La zone doit contenir au moins 3 points.");
				alert.showAndWait();
			}
		} else { // si on supprime une zone
			List<Zone> aSuppr = new ArrayList<Zone>();
			for (Zone laZone : uneQuestion.getListeZone()) {
				if (laZone.getLaZone().getStroke().equals(Color.RED)) {
					try {
						H2.connexion();
						H2.modifier("DELETE COORDONNEE WHERE Id_Zone=" + laZone.getIdZone() + ";");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					aSuppr.add(laZone);
				}
			}
			for (Zone zone : aSuppr) {
				uneQuestion.getListeZone().remove(zone);
			}
			dessinerZone();
		}
		// activer les boutons pour ajouter ou supprimer une zone
		btnAjouter.setDisable(false);
		btnSupprimer.setDisable(false);
		// desactiver les boutons pour confirmer ou annuler la création de la zone
		btnConfirmer.setDisable(true);
		btnAnnuler.setDisable(true);
	}

	private void collision() {
		for (Zone uneAutreZone : uneQuestion.getListeZone()) {
			if (!uneAutreZone.equals(uneZone)) {
				Shape intersect = Shape.intersect(uneZone.getLaZone(), uneAutreZone.getLaZone());
				if (intersect.getBoundsInLocal().getWidth() != -1) { // si collision
					alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 points.
					alert.setTitle("Impossible de créer la zone");
					alert.setContentText("Les différentes zones ne peuvent pas avoir de points communs.");
					alert.showAndWait();
					uneQuestion.getListeZone().remove(uneZone);
					uneZone.remove(listeQuestion.getValue());
					dessinerZone();
				}
			}
		}
	}

	@FXML
	public void sauvegarder() {
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
							+ " WHERE Id_Theme=" + Theme.getModifTheme() + ";");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					H2.connexion();
					H2.modifier("UPDATE THEME SET Actif_Theme='false', Nb_Question=" + listeQuestion.getItems().size()
							+ " WHERE Id_Theme=" + Theme.getModifTheme() + ";");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
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

	private void ajouterImage() throws FileNotFoundException {
		try {
			H2.connexion();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String imgSrc = H2.separer(H2.lire("SELECT Adr_Image FROM THEME WHERE Id_Theme=" + Theme.getModifTheme()))[0];
		FileInputStream src = new FileInputStream(imgSrc);
		imageFond = new Image(src);
		try {
			src.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imageHeight = imageFond.getHeight();
		imageWidth = imageFond.getWidth();
		BackgroundImage imageBg = new BackgroundImage(imageFond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, new BackgroundSize(imageWidth, imageHeight, false, false, false, true));
		paneImage.setPrefWidth(imageWidth);
		paneImage.setPrefHeight(imageHeight);
		paneImage.setMaxWidth(imageWidth);
		paneImage.setMaxHeight(imageHeight);
		paneImage.setBackground(new Background(imageBg));
	}

}
