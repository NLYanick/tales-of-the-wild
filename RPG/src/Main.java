import Controller.MainController;
import View.MainScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		MainController controller = new MainController();
		MainScene scene = controller.getMainScene();
		
		stage.setTitle("RPG-Project");
		stage.setScene(scene);
		stage.show();
	}
	
	@Override
	public void stop() {
		
	}

}
