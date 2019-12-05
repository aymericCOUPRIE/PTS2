package editeur.view;

import editeur.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainControllers {


	@FXML
	private Button btnQuitter;
	@FXML
	private Button btnAjouter;
	@FXML
	private Button btnModifier;

	@FXML
	private void initialize(){
		// si dans la bdd boolean didacticiel = true
		// faire méthode didacticiel()
	}
	
	@FXML 
	public void ajouterTheme() {
		try {
			Main.loadView("view/Creation_Theme.fxml");
			Main.primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML 
	public void modifierTheme() {
		try {
			Main.loadView("view/Selection_Theme.fxml");
			Main.primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML 
	public void quitter() {
		Main.primaryStage.close();
	}
}
