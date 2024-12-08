package View;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class MainScene extends Scene {

	public final static int SCENEWIDTH = 600;
	public final static int SCENEHEIGHT = 600;
	
	public MainScene() {
		super(new Pane());
		setUpRoot();
	}
	
	private void setUpRoot() {
		Pane root = new Pane();
		
		root.setPrefSize(SCENEWIDTH, SCENEHEIGHT);
		
		setRoot(root);
	}
	
	public void setAsRoot(Pane root) {
		setRoot(root);
	}

}
