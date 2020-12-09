package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
//077132ff50c0ebe35cacad57255a78d2a5495584

public class Main extends Application {
	MainModel model = new MainModel();
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/Billing.fxml"));	
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
			primaryStageSetSize(primaryStage,630,1080); //User-defined	
			stageSetPosition(primaryStage,630,1080); //User-defined
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stageSetPosition(Stage primaryStage,int height,int width) {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(primaryScreenBounds.getWidth()/2 - width/2);
		primaryStage.setY(primaryScreenBounds.getHeight()/2 - height/2);
		System.out.println();
	}
	
	public void primaryStageSetSize(Stage primaryStage,int height,int width) {
		primaryStage.setHeight(height);
		primaryStage.setWidth(width);
		primaryStage.setResizable(false);
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}

}
