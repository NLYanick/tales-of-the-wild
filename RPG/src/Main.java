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
		
		BorderPane pane = new BorderPane();
		Image img = new Image("Images/AngryPear.png");
		Image imgTwo = new Image("Images/New Piskel.gif");
		ImageView imgView = new ImageView(img);
		ImageView imgViewTwo = new ImageView(imgTwo);
		pane.setCenter(imgView);
		pane.setBottom(imgViewTwo);
		Scene scene = new Scene(pane);
		
		stage.setTitle("RPG-Project");
		stage.setScene(scene);
		stage.show();
	}

}
