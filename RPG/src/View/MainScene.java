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
	private StartUpView startUpView;
	
	private BorderPane root;
	
	public MainScene(MainController controller) {
		super(new Pane());
		
		this.controller = controller;
		
		setUpRoot();
	}
	
	private void setUpRoot() {
		root = new BorderPane();
		
		root.setPrefSize(SCENEWIDTH, SCENEHEIGHT);
		
		setUpStartUpView();
		
		setRoot(root);
	}
	
	private void setUpListeners() {
		setOnKeyPressed(e -> handleInputKeyPressed(e));
		setOnKeyReleased(e -> handleInputKeyReleased(e));
	}
	
	private void setUpStartUpView() {
		startUpView = new StartUpView(controller.getFileIO(), this);
		root.setCenter(startUpView);
	}
	
	private void addBackground() {
		background = controller.getBackground();
		root.getChildren().add(background);
	}
	
	private void createPlayerView()
	{
		playerView = new PlayerView(controller.getPlayerURL());
		root.setCenter(playerView);
	}
	
	public void loadBackground() {
		setUpListeners();
		root.setCenter(null);
		addBackground();
		createPlayerView();
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
				controller.setMovingDirection(Direction.SOUTH);
				controller.getUpPressed().set(false);
				break;
			case DOWN:
				controller.setMovingDirection(Direction.NORTH);
				controller.getDownPressed().set(false);
				break;
			case RIGHT:
				controller.setMovingDirection(Direction.WEST);
				controller.getRightPressed().set(false);
				break;
			case LEFT:
				controller.setMovingDirection(Direction.EAST);
				controller.getLeftPressed().set(false);
				break;
			default: return;
		}
	}
}
