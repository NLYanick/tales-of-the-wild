package View;

import Controller.MainController;
import Model.Direction;
import javafx.scene.Cursor;
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
	
	private boolean gameHasLoaded = false;
	
	public MainScene(MainController controller) {
		super(new Pane());
		
		this.controller = controller;
		
		setUpRoot();
	}
	
	private void setUpRoot() {
		root = new BorderPane();
		
		root.setPrefSize(SCENEWIDTH, SCENEHEIGHT);
		
		setUpStartUpView();
		setUpListeners();
		
		setRoot(root);
	}
	
	private void setUpListeners() {
		setOnKeyPressed(e -> handleInputKeyPressed(e));
		setOnKeyReleased(e -> handleMovementReleased(e));
	}
	
	private void setUpStartUpView() {
		startUpView = new StartUpView(this);
		root.setCenter(startUpView);
	}
	
	private void addBackground() {
		background = controller.getBackground();
		root.getChildren().add(background);
	}
	
	private void createPlayerView()
	{
		playerView = new PlayerView(controller.getPlayerURL());
		controller.setPlayerLocation();
		root.setCenter(playerView);
	}
	
	public void loadBackground() {
		
		gameHasLoaded = true;
		
		root.setCenter(null);
		
		addBackground();
		controller.setUpNPCs();
		createPlayerView();
		
		setCursor(Cursor.NONE);
	}
	
	public void addNPCView(NPCView nPCView) {
		root.getChildren().add(nPCView);
	}
	
	public void setAsRoot(Pane root) {
		setRoot(root);
	}
	
	public void changePlayerImage() {
		playerView.setImageURL(controller.getPlayerURL());
		playerView.refreshImage();
	}
	
	public void changeNPCImage(NPCView npcView, String url) {
		npcView.setImageURL(url);
		npcView.refreshImage();
	}

	public void moveBackground(int x, int y) {
		background.move(x, y);
	}
	
	private void handleInputKeyPressed(KeyEvent e) {
		switch(e.getCode()) {
		case E: 
			if(gameHasLoaded) {
				
				controller.playerInteract();
			}
			break;
		case ESCAPE: 
			controller.setFullScreen(false);
			controller.resetPlayerLocation();
			break;
		case F11:
			controller.setFullScreen(true);
			controller.resetPlayerLocation();
			if(gameHasLoaded) {
				setCursor(Cursor.NONE);
			}
			break;
		default: 
			if(gameHasLoaded) {
				handleMovementPressed(e);
			}
		}
	}
	
	private void handleMovementPressed(KeyEvent e) {
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
			default: System.out.println("Input not valid");
		}
	}
	
	private void handleMovementReleased(KeyEvent e) {
		if(gameHasLoaded) {
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
				default: System.out.println("Input not valid");
			}
		}
	}
}
