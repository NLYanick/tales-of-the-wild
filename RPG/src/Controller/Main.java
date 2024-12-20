package Controller;

import View.MainScene;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage stage;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		this.stage = stage;
		
		MainController controller = new MainController(this);
		MainScene scene = controller.getMainScene();
		
		stage.setTitle("RPG-Project");
		stage.setFullScreen(true);
		stage.setFullScreenExitHint("");
		stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("Alt + F"));
		stage.setScene(scene);
		stage.show();
	}
	
	@Override
	public void stop() {
		
	}
	
	public void setFullScreen() {
		stage.setFullScreen(true);
	}

}
