package application.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import application.Main;
import application.Question;
import application.Score;
import application.Theme;
import application.Zone;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class JeuControllers {

	@FXML
	private Button btnValider;

	@FXML
	private Button btnQuitter;

	@FXML
	private Button btnplus1;

	@FXML
	private Label lbscore;

	@FXML
	private AnchorPane paneImage;

	@FXML
	private Label intituleQuestion;

	@FXML
	private Label progressionQuestion;

	@FXML
	private Button btnClean;

	private boolean errorNbPin;

	// private static double score;
	private static int comptQuestion;
	private boolean reponseExacte;

	private int comptPin;
	private ImageView imgv;
	private EventHandler<MouseEvent> verif;
	private ImageView[] listePin;

	private Zone[] listeZone;
	private static Score scoreDeLaPartie;
	private int nbBonneReponse;

	@FXML
	private void initialize() throws SQLException, FileNotFoundException, InterruptedException {
		scoreDeLaPartie = new Score();
		comptQuestion = 1;
		FileInputStream src = new FileInputStream(Theme.themeEnCours.getUrlImage());
		Image imageFond = new Image(src);
		try {
			src.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double imageHeight = imageFond.getHeight();
		double imageWidth = imageFond.getWidth();
		BackgroundImage imageBg = new BackgroundImage(imageFond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		paneImage.setBackground(new Background(imageBg));
		paneImage.setPrefWidth(imageWidth);
		paneImage.setPrefHeight(imageHeight);
		paneImage.setMaxWidth(imageWidth);
		paneImage.setMaxHeight(imageHeight);
		paneImage.setBackground(new Background(imageBg));
		afficher();
		paneImage.addEventFilter(MouseEvent.MOUSE_CLICKED, verif);
		btnClean.setDisable(true);

	}

	public void afficher() {
		Question.questionEnCours = new Question(comptQuestion);
		listeZone = new Zone[Question.questionEnCours.getNbZone()];
		for (int i = 1; i <= Question.questionEnCours.getNbZone(); i++) {
			Zone.zoneEnCours = new Zone(i);
			listeZone[i - 1] = Zone.zoneEnCours;
			paneImage.getChildren().add(Zone.zoneEnCours.getZoneReponse());
		}
		intituleQuestion.setText(Question.questionEnCours.getIntitule().replace(";", ""));
		progressionQuestion.setText("Question " + comptQuestion + "/" + Theme.themeEnCours.getNbQuestion() + ": ");
		bonOuPasBon();
	}

	@FXML
	private void valider() {
		if (btnValider.getText().equals("Valider Réponse")) { // si le bouton indique "valider reponse"
			btnClean.setDisable(true);
			errorNbPin = false;
			for (int i = 0; i < listePin.length; i++) {
				if (listePin[i] == null) {
					errorNbPin = true;
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Attention");
					alert.setContentText("Réponse(s) Supplémentaire(s) attendue(s)");
					alert.showAndWait();
				}
			}
			if (errorNbPin == false) {
				nbBonneReponse = 0;
				for (Zone zone : listeZone) {
					boolean zoneTrouvee = false;
					for (int i = 0; i < listePin.length; i++) {
						if (zone.getZoneReponse().contains(listePin[i].getX() + 25, listePin[i].getY() + 45)) {
							zoneTrouvee = true;
						}
					}
					if (zoneTrouvee == true) {
						zone.getZoneReponse().setFill(Color.GREEN);
						nbBonneReponse++;
					}
					else zone.getZoneReponse().setFill(Color.RED);
				}
				if (Theme.themeEnCours.getDifficulté().equals("Difficile")) {
					if (nbBonneReponse == Question.questionEnCours.getNbZone())
						scoreDeLaPartie.setScore((scoreDeLaPartie.getScore() + 1));
				} 
				else if (Theme.themeEnCours.getDifficulté().equals("Moyen")) {
					if (nbBonneReponse > 0) scoreDeLaPartie.setScore(scoreDeLaPartie.getScore() + 1);
				}
				btnValider.setText("Suivant");
			}
		} else
			questionSuivante(); // si le bouton n'indique pas "valider reponse"
	}

	@FXML
	private void questionSuivante() {
		lbscore.setText(String.valueOf(scoreDeLaPartie.getScore()));
		if (comptQuestion < Theme.themeEnCours.getNbQuestion()) {
			comptQuestion++;
			for (int i = 0; i < comptPin + Question.questionEnCours.getNbZone(); i++) {
				paneImage.getChildren().remove(paneImage.getChildren().size() - 1);
			}
			afficher();
			btnValider.setText("Valider Réponse");
		} else {
			try {
				FXMLLoader rootFXML = new FXMLLoader(getClass().getResource("../view/FinPartie.fxml"));
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
	private void quitter() {
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
	private void bonOuPasBon() {
		comptPin = 0;
		listePin = new ImageView[Question.questionEnCours.getNbZone()];
		if (Theme.themeEnCours.getDifficulté().equals("Moyen")) {
			listePin = new ImageView[1];
		}
		verif = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (comptPin < listePin.length) {
					imgv = new ImageView();
					Image img = new Image("./application/images/epingle.png");
					imgv.setImage(img);
					imgv.setFitWidth(50d); // réduire l'imageView en 50*50
					imgv.setFitHeight(50d);
					imgv.setX(e.getX() - 25); // positionX - la moitié de la taille de l'image pour la centré
					imgv.setY(e.getY() - 45); // positionY - la quasi totalité de la hauteur de l'image pour que le bout
												// de l'aiguille soit au bon endroit
					paneImage.getChildren().add(imgv);
					listePin[comptPin] = imgv;
					comptPin++;
					btnClean.setDisable(false);
				}
			}
		};
	}

	@FXML
	private void suppPin() {
		for (int i = 0; i < comptPin; i++) {
			paneImage.getChildren().remove(paneImage.getChildren().size() - 1);
			comptPin--;
			if (comptPin == 0)
				btnClean.setDisable(true);
		}
	}

	public void compterPlus1() {
		scoreDeLaPartie.setScore(scoreDeLaPartie.getScore() + 1);

	}

	/*
	 * public static double getScore() { return score; }
	 */

	public static Score getScoreDeLaPartie() {
		return scoreDeLaPartie;
	}

	public static int getComptQuestion() {
		return comptQuestion;
	}

	public boolean isReponseExacte() {
		return reponseExacte;
	}
}
