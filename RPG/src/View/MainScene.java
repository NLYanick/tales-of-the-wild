package View;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MainScene extends Scene {

	public final static int SCENEWIDTH = 600;
	public final static int SCENEHEIGHT = 600;
	
	public MainScene() {
		super(new Pane());
		setUpRoot();
		setOnKeyTyped(e -> handleInput(e));
	}
	
	private void setUpRoot() {
		Pane root = new Pane();
		
		root.setPrefSize(SCENEWIDTH, SCENEHEIGHT);
		
		// TODO remove
		BorderPane pane = new BorderPane();
		Image img = new Image("Images/AngryPear.png");
		ImageView imgView = new ImageView(img);
		imgView.setFitWidth(64);
		imgView.setFitHeight(64);
		pane.setCenter(imgView);
		pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
		
		root.getChildren().add(pane);
		//
		
		setRoot(root);
	}
	
	public void setAsRoot(Pane root) {
		setRoot(root);
	}
	
	private void handleInput(KeyEvent e) {
		if(e.getCode().equals(KeyCode.UP)) {
			
		}
		if(e.getCode().equals(KeyCode.DOWN)) {
			
		}
		if(e.getCode().equals(KeyCode.RIGHT)) {
			
		}
		if(e.getCode().equals(KeyCode.LEFT)) {
			
		}
	}

}
