package editeur;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	public static Stage primaryStage;
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Main.primaryStage = primaryStage;
			loadView("view/Accueil_Editeur.fxml");
			primaryStage.setResizable(false); // empecher redimensionnement 
			primaryStage.setTitle("Area Learning Editeur");	
			primaryStage.getIcons().add(new Image("./imagesSources/logoEditeur.png"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadView(String location) throws IOException {
		FXMLLoader rootFXML = new FXMLLoader(Main.class.getResource(location)); // chargement de la scene
		BorderPane root = rootFXML.load();
		Scene scene = new Scene(root);
		Main.primaryStage.setScene(scene);
	}
	
}