package View;

import Controller.MainController;
import Model.Direction;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainScene extends Scene {

	public final static int SCENEWIDTH = 700;
	public final static int SCENEHEIGHT = 700;
	
	private MainController controller;
	
	private PlayerView playerView;
	
//	private StackPane root;
	private Pane root;
	
	public MainScene(MainController controller) {
		super(new Pane());
		
		this.controller = controller;
		
		setUpRoot();
		setOnKeyPressed(e -> handleInput(e));
	}
	
	private void setUpRoot() {
//		root = new StackPane();
		root = new Pane();
		
		root.setPrefSize(SCENEWIDTH, SCENEHEIGHT);
		createPlayerView();
		
		setRoot(root);
	}
	
	public void setAsRoot(Pane root) {
		setRoot(root);
	}
	
	public void movePlayerView(int x, int y) {
		playerView.move(x, y);
	}
	
	private void handleInput(KeyEvent e) {
		if(e.getCode().equals(KeyCode.UP)) {
			controller.movePlayer(Direction.NORTH);
		}
		if(e.getCode().equals(KeyCode.DOWN)) {
			controller.movePlayer(Direction.SOUTH);
		}
		if(e.getCode().equals(KeyCode.RIGHT)) {
			controller.movePlayer(Direction.EAST);
		}
		if(e.getCode().equals(KeyCode.LEFT)) {
			controller.movePlayer(Direction.WEST);
		}
	}
	
	private void createPlayerView()
	{
		playerView = new PlayerView(controller);
		root.getChildren().add(playerView);
//		root.setAlignment(Pos.CENTER);
	}

}
