import View.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		MainScene scene = new MainScene();
		
		stage.setTitle("RPG-Project");
		stage.setScene(scene);
		stage.show();
	}
	
	@Override
	public void stop() {
		
	}

}
