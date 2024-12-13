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
		setOnKeyPressed(e -> handleInputKeyPressed(e));
		setOnKeyReleased(e -> handleInputKeyReleased(e));
	}
	
	private void setUpRoot() {
		root = new BorderPane();
		
		root.setPrefSize(SCENEWIDTH, SCENEHEIGHT);
		
		createBackground();
		createPlayerView();
		
		setRoot(root);
	}
	
	public void changePlayerImage() {
		playerView.setImageURL(controller.getPlayerURL());
		playerView.refreshImage();
	}

	public void setAsRoot(Pane root) {
		setRoot(root);
	}
	
	public void moveBackground(int x, int y) {
		background.move(x, y);
	}
	
	private void handleInputKeyPressed(KeyEvent e) {
		switch(e.getCode()) {
			case UP:
				controller.moveBackground(Direction.SOUTH);
				break;
			case DOWN:
				controller.moveBackground(Direction.NORTH);
				break;
			case RIGHT:
				controller.moveBackground(Direction.WEST);
				break;
			case LEFT:
				controller.moveBackground(Direction.EAST);
				break;
			default: return;
		}
	}
	
	private void handleInputKeyReleased(KeyEvent e) {
//		switch(e.getCode()) {
//			case UP:
//				controller.setPlayerStandingStillAnimation(Direction.SOUTH);
//				break;
//			case DOWN:
//				controller.setPlayerStandingStillAnimation(Direction.NORTH);
//				break;
//			case RIGHT:
//				controller.setPlayerStandingStillAnimation(Direction.WEST);
//				break;
//			case LEFT:
//				controller.setPlayerStandingStillAnimation(Direction.EAST);
//				break;
//			default: return;
//		}
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
	
}
