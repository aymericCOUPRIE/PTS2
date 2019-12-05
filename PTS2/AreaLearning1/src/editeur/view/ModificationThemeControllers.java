package editeur.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Optional;

import javax.imageio.ImageIO;

import editeur.H2;
import editeur.Main;
import editeur.Theme;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ModificationThemeControllers {

	@FXML
	private ImageView logoDossier;
	@FXML
	private ImageView logoDoss;
	@FXML
	private Button btnRetour;
	@FXML
	private Button btnSauvegarder;
	@FXML
	private Button btnModifierQuestions;
	@FXML
	private Button btnParcourirLogo;
	@FXML
	private Button btnParcourirImage;
	@FXML
	private TextField titre;
	@FXML
	private TextArea description;
	@FXML
	private TextField srcLogo;
	@FXML
	private TextField srcImage;
	@FXML
	private ImageView imageFond;

	private Theme leTheme;
	private FileChooser fileChooser;
	private File homeDir;
	private Alert alert;
	private double widthAnchorPane;
	private double heightAnchorPane;
	private double widthImageReel;
	private double heightImageReel;
	private double imageRapport;
	private String fichierSrcImage;
	private String srcImageBdd;
	private String srcLogoBdd;

	@FXML
	private void initialize() {
		Image imgDossier = new Image("./imagesSources/logoDossierNoir.jpg");
		logoDossier.setImage(imgDossier);
		logoDoss.setImage(imgDossier);
		String contenu[] = H2.separer(H2.lire("SELECT * FROM THEME WHERE Id_Theme = " + Theme.getModifTheme() + ";"));
		leTheme = new Theme(Integer.parseInt(contenu[0]), H2.nonApos(contenu[1]), H2.nonApos(contenu[2]), Integer.parseInt(contenu[3]),
				contenu[4], contenu[5], Boolean.parseBoolean(contenu[6]));
		titre.setText(leTheme.getTitre());
		description.setWrapText(true);
		description.setText(leTheme.getDescription());
		srcImage.setText(leTheme.getSrcImage());
		srcLogo.setText(leTheme.getSrcLogo());

		try {
			FileInputStream inp = new FileInputStream(srcImage.getText());
			imageFond.setImage(new Image(inp));
			inp.close();
		} catch (IOException e) {
		}
		widthImageReel = 0;
		heightImageReel = 0;
		widthAnchorPane = 720;
		heightAnchorPane = 600;
		srcImageBdd = leTheme.getSrcImage();
		srcLogoBdd = leTheme.getSrcLogo();
	}

	@FXML
	public void retour() {
		boolean Existant = false;
		try {
			H2.connexion();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] titresExistant = H2
				.separer(H2.lire("SELECT Nom_Theme FROM THEME WHERE Id_Theme<>" + leTheme.getIdTheme() + ";"));
		for (int i = 0; i <= titresExistant.length - 1; i++) {
			if (titresExistant[i].equals(H2.nonApos(titre.getText()))) {
				Existant = true;
			}
		}

		if (!titre.getText().isEmpty() && !description.getText().isEmpty() && imageFond.getImage() != null
				&& !Existant) {
			String adresse[] = H2.separer(
					H2.lire("SELECT Adr_Logo, Adr_Image FROM THEME WHERE Id_Theme=" + leTheme.getIdTheme() + ";"));
			srcImageBdd = adresse[1];
			srcLogoBdd = adresse[0];
			if (!srcLogo.getText().equals(adresse[0]) || !srcImage.getText().equals(adresse[1]))
				copieImage();
			leTheme.setTitre(titre.getText());
			leTheme.setDescription(description.getText());
			leTheme.setSrcImage(srcImageBdd);
			leTheme.setSrcLogo(srcLogoBdd);
			try {
				leTheme.save();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Main.loadView("view/Selection_Theme.fxml");
				Main.primaryStage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (titre.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
				alert.setTitle("Titre inexistant");
				alert.setContentText("Vous devez ajouter un titre pour continuer");
				alert.showAndWait();
			} else if (Existant) {
				alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
				alert.setTitle("Titre existant");
				alert.setContentText("Ce titre existe deja, changez le pour continuer");
				alert.showAndWait();
			}
			if (description.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
				alert.setTitle("Description inexistante");
				alert.setContentText("Vous devez ajouter une description pour continuer");
				alert.showAndWait();
			}
			if (imageFond.getImage() == null) {
				alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
				alert.setTitle("Image inexistante");
				alert.setContentText("Vous devez ajouter une image pour continuer");
				alert.showAndWait();
			}
		}
	}

	@FXML
	public void modifierQuestions() {
		boolean Existant = false;
		try {
			H2.connexion();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] titresExistant = H2
				.separer(H2.lire("SELECT Nom_Theme FROM THEME WHERE Id_Theme<>" + leTheme.getIdTheme() + ";"));
		for (int i = 0; i <= titresExistant.length - 1; i++) {
			if (H2.nonApos(titresExistant[i]).equals(titre.getText())) {
				Existant = true;
			}
		}

		if (!titre.getText().isEmpty() && !description.getText().isEmpty() && imageFond.getImage() != null
				&& !Existant) {
			String adresse[] = H2.separer(
					H2.lire("SELECT Adr_Logo, Adr_Image FROM THEME WHERE Id_Theme=" + leTheme.getIdTheme() + ";"));
			srcImageBdd = H2.nonApos(adresse[1]);
			srcLogoBdd = H2.nonApos(adresse[0]);
			if (!srcLogo.getText().equals(adresse[0]) || !srcImage.getText().equals(adresse[1]))
				copieImage();
			leTheme.setTitre(titre.getText().trim());
			leTheme.setDescription(description.getText().trim());
			leTheme.setSrcImage(srcImageBdd);
			leTheme.setSrcLogo(srcLogoBdd);
			try {
				leTheme.save();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Main.loadView("view/Creation_Question.fxml");
				Main.primaryStage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (titre.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
				alert.setTitle("Titre inexistant");
				alert.setContentText("Vous devez ajouter un titre pour continuer");
				alert.showAndWait();
			} else if (Existant) {
				alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
				alert.setTitle("Titre existant");
				alert.setContentText("Ce titre existe deja, changez le pour continuer");
				alert.showAndWait();
			}
			if (description.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
				alert.setTitle("Description inexistante");
				alert.setContentText("Vous devez ajouter une description pour continuer");
				alert.showAndWait();
			}
			if (imageFond.getImage() == null) {
				alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
				alert.setTitle("Image inexistante");
				alert.setContentText("Vous devez ajouter une image pour continuer");
				alert.showAndWait();
			}
		}
	}

	public void copieImage() { // copier l'image dans un répertoire hors projet (src)
		try {
			H2.connexion();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// ajouter nouveaux
		if (new File(srcImage.getText()).isFile()) {
			Image newImage = null;
			try {
				FileInputStream inp = new FileInputStream(srcImage.getText());
				newImage = new Image(inp, widthImageReel, heightImageReel, false, true);
				inp.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedImage bImage = SwingFXUtils.fromFXImage(newImage, null);
			File outputFile = new File("..\\Images\\Image-" + titre.getText() + ".jpg"); // l'image enregistré prend le
																							// nom
																							// du theme
			srcImageBdd = outputFile.getAbsolutePath(); // sauvegarde pour enregistrement bdd
			try {
				ImageIO.write(bImage, "jpg", outputFile);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if (new File(srcLogo.getText()).isFile()) { // si c'est un fichier
			try {
				Files.copy(Paths.get(srcLogo.getText()), Paths.get("..\\Images\\Logo-" + titre.getText() + ".jpg"),
						StandardCopyOption.REPLACE_EXISTING);
				srcLogoBdd = Paths.get("..\\Images\\Logo-" + titre.getText() + ".jpg").toAbsolutePath().toString(); // sauvegarde
																													// pour
																													// enregistrement
																													// bdd
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@FXML
	public void parcourirLogo() {
		fileChooser = new FileChooser();
		fileChooser.setTitle("Choisir un logo");
		homeDir = new File(System.getProperty("user.home"));
		fileChooser.setInitialDirectory(homeDir);
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
		File leFichier = fileChooser.showOpenDialog(Main.primaryStage);
		if (leFichier != null) {
			String fichierSrc = leFichier.getAbsolutePath();
			srcLogo.setText(fichierSrc);
		} else {
			alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
			alert.setTitle("Fichier inexistant ou non trouvé");
			alert.setContentText("Le logo n'a pas pu être chargé");
			alert.showAndWait();
		}
	}

	private void redimImage() { // si l'image importé est plus petite que la moitié de l'anchorpane dans
		// laquelle elle va etre affiché on demande à l'utilisateur si il/elle veut que
		// l'on redimensionne l'image
		if (imageFond.getImage().getWidth() <= widthAnchorPane / 2
				|| imageFond.getImage().getHeight() <= heightAnchorPane / 2) {
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation redimensionnement");
			alert.setHeaderText("L'image est petite. Si vous la redimensionnez, vous perderez en qualité.");
			alert.setContentText("Voulez vous la redimensionner ?");
			ButtonType btnOui = new ButtonType("Oui");
			ButtonType btnNon = new ButtonType("Non");
			alert.getButtonTypes().setAll(btnOui, btnNon);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == btnOui) {
				imageRapport = widthAnchorPane / imageFond.getImage().getWidth();
				widthImageReel = imageFond.getImage().getWidth() * imageRapport;
				heightImageReel = imageFond.getImage().getHeight() * imageRapport;
				if (heightImageReel > heightAnchorPane) {
					imageRapport = heightImageReel / 600;
					heightImageReel = heightImageReel / imageRapport;
					widthImageReel = widthImageReel / imageRapport;
				}
			}
		} else if (imageFond.getImage().getWidth() > widthAnchorPane
				|| imageFond.getImage().getHeight() > heightAnchorPane) {// si l'mage est
			// trop grande
			// on l'adapte
			if (imageFond.getImage().getHeight() > heightAnchorPane) {
				imageRapport = 600 / imageFond.getImage().getHeight();
				heightImageReel = imageFond.getImage().getHeight() * imageRapport;
				widthImageReel = imageFond.getImage().getWidth() * imageRapport;
			}
			if (imageFond.getImage().getWidth() > widthAnchorPane) {
				imageRapport = widthAnchorPane / imageFond.getImage().getWidth();
				heightImageReel = imageFond.getImage().getHeight() * imageRapport;
				widthImageReel = imageFond.getImage().getWidth() * imageRapport;
			}
		}
	}

	@FXML
	public void parcourirImage() {
		fileChooser = new FileChooser();
		fileChooser.setTitle("Choisir une image");
		homeDir = new File(System.getProperty("user.home"));
		fileChooser.setInitialDirectory(homeDir);
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
		File leFichier = fileChooser.showOpenDialog(Main.primaryStage);

		if (leFichier != null) {
			fichierSrcImage = leFichier.getAbsolutePath();
			srcImage.setText(fichierSrcImage);
			try {
				FileInputStream inp = new FileInputStream(fichierSrcImage);
				imageFond.setImage(new Image(inp));
				inp.close();
				redimImage();
			} catch (IOException e) {
			}
		} else {
			alert = new Alert(AlertType.ERROR); // message d'alerte s'il n'y a pas minimum 3 couleurs.
			alert.setTitle("Fichier inexistant ou non trouvé");
			alert.setContentText("L'image n'a pas pu être chargé");
			alert.showAndWait();
		}
	}

	@FXML
	public void tailleMaxTextField() {
		int max = 35;
		if (titre.getText().length() > max) {
			titre.deleteText(max, titre.getText().length());
		}
		if (titre.getText().contains("~")) titre.setText(titre.getText().replaceAll("~", ""));
		if (titre.getText().contains("\"")) titre.setText(titre.getText().replaceAll("\"", ""));
		if (titre.getText().contains("'")) titre.setText(titre.getText().replaceAll("'", ""));
	}

	@FXML
	public void tailleMaxTextArea() {
		int max = 150;
		if (description.getText().length() > max) {
			description.deleteText(max, description.getText().length());
		}
		if (description.getText().contains("~")) description.setText(description.getText().replaceAll("~", ""));
		if (description.getText().contains("\"")) description.setText(description.getText().replaceAll("\"", ""));
	}

	@FXML
	public void updateImage() {
		try {
			FileInputStream inp = new FileInputStream(srcImage.getText());
			imageFond.setImage(new Image(inp));
			inp.close();
		} catch (IOException e) {
			imageFond.setImage(null);
		}
	}
}
