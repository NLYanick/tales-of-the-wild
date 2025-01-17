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
	private MenuView menuView;
	
	private BorderPane root;
	
	private boolean gameHasLoaded;
	private boolean menuIsOpen;
	
	public MainScene(MainController controller) {
		super(new Pane());
		
		this.controller = controller;
		
		setUpRoot();
	}
	
	private void setUpRoot() {
		root = new BorderPane();
		menuView = new MenuView();
		
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

		background.setLayoutX(getWidth()/2 - SCENEWIDTH/2);
		background.setLayoutY(getHeight()/2 - SCENEHEIGHT/2);
		
		root.getChildren().add(background);
	}
	
	private void createPlayerView()
	{
		playerView = new PlayerView(controller.getPlayerURL());
		root.setCenter(playerView);
	}
	
	public void loadBackground() {
		
		gameHasLoaded = true;
		
		root.setCenter(null);
		
		addBackground();
		controller.setUpNPCs();
		moveBackground(controller.getBackgroundLocation().getX(), controller.getBackgroundLocation().getY(), true);
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

	public void moveBackground(int x, int y, boolean isFullScreen) {
		if(background != null) {
			if(isFullScreen)
				background.move((int) (x + getWidth()/2 - SCENEWIDTH/2), (int) (y + getHeight()/2 - SCENEHEIGHT/2));
			else
				background.move(x, y);
		}
	}
	
	private void toggleMenu() {
		if(menuIsOpen) {
			root.setCenter(menuView);
//			root.getChildren().add(menuView);
		} else {
			root.setCenter(playerView);
//			root.getChildren().remove(menuView);
		}
	}
	
	private void handleInputKeyPressed(KeyEvent e) {
		switch(e.getCode()) {
		case E: 
			if(gameHasLoaded) {
				controller.playerInteract();
			}
			break;
		case ESCAPE: 
			controller.resizeBackgroundAndNPCLocation();
			if(gameHasLoaded) {
				menuIsOpen = !menuIsOpen;
				toggleMenu();
			}
			break;
		case F11:
			controller.setFullScreen(!controller.isFullScreen());
			controller.resizeBackgroundAndNPCLocation();
			if(gameHasLoaded) {
				if(controller.isFullScreen()) {
					setCursor(Cursor.NONE);
				} else {
					setCursor(Cursor.DEFAULT);
				}
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
				controller.getUpPressed().set(true);
				break;
			case DOWN:
				controller.getDownPressed().set(true);
				break;
			case RIGHT:
				controller.getRightPressed().set(true);
				break;
			case LEFT:
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
				default:
			}
		}
	}
}
