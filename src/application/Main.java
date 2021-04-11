package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("Main.fxml"));
			Parent root = fxmlLoader.load();
			
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			// controller init
			Controller controller = fxmlLoader.getController();
			controller.init();
			
			// load title and icon
			primaryStage.setTitle("MyFiveChess");
			primaryStage.getIcons().add(new Image("/resource/icon.png"));
			primaryStage.setMaxHeight(720);
			primaryStage.setMaxWidth(1000);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}	 
}
