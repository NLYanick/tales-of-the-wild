package View;

import Controller.MainController;
import Model.Direction;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MainScene extends Scene {

	public final static int SCENEWIDTH = 700;
	public final static int SCENEHEIGHT = 700;
	
	private MainController controller;
	
	private PlayerView playerView;
	private Background background;
	
	private BorderPane root;
	
	public MainScene(MainController controller) {
		super(new Pane());
		
		this.controller = controller;
		
		setUpRoot();
		setUpListeners();
	}
	
	private void setUpRoot() {
		root = new BorderPane();
		
		root.setPrefSize(SCENEWIDTH, SCENEHEIGHT);
		
		createBackground();
		createPlayerView();
		
		setRoot(root);
	}
	
	private void setUpListeners() {
		setOnKeyPressed(e -> handleInputKeyPressed(e));
		setOnKeyReleased(e -> handleInputKeyReleased(e));
	}
	
	private void createBackground() {
		background = new Background();
		root.getChildren().add(background);
	}
	
	private void createPlayerView()
	{
		playerView = new PlayerView(controller.getPlayerURL());
		root.setCenter(playerView);
	}
	
	public void setAsRoot(Pane root) {
		setRoot(root);
	}
	
	public void changePlayerImage() {
		playerView.setImageURL(controller.getPlayerURL());
		playerView.refreshImage();
	}

	public void moveBackground(int x, int y) {
		background.move(x, y);
	}
	
	private void handleInputKeyPressed(KeyEvent e) {
		switch(e.getCode()) {
			case UP:
				controller.setMovingDirection(Direction.SOUTH);
				controller.getUpPressed().set(true);
				break;
			case DOWN:
				controller.setMovingDirection(Direction.NORTH);
				controller.getDownPressed().set(true);
				break;
			case RIGHT:
				controller.setMovingDirection(Direction.WEST);
				controller.getRightPressed().set(true);
				break;
			case LEFT:
				controller.setMovingDirection(Direction.EAST);
				controller.getLeftPressed().set(true);
				break;
			default: return;
		}
	}
	
	private void handleInputKeyReleased(KeyEvent e) {
		switch(e.getCode()) {
			case UP:
				controller.getUpPressed().set(false);
				break;
			case DOWN:
				controller.getDownPressed().set(false);
				break;
			case RIGHT:
				controller.getRightPressed().set(false);
				break;
			case LEFT:
				controller.getLeftPressed().set(false);
				break;
			default: return;
		}
	}
}
