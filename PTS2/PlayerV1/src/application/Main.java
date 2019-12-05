package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage primaryStage;

	private static BorderPane root;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader rootFXML = new FXMLLoader(getClass().getResource("view/PageAccueil.fxml"));
			root = rootFXML.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("Player.css").toExternalForm());
			Main.primaryStage = primaryStage;
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static BorderPane getRoot() {
		return root;
	}

}