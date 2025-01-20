package View;

import Controller.MainController;
import Model.Direction;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainScene extends Scene {

	public final static int SCENEWIDTH = 700;
	public final static int SCENEHEIGHT = 700;
	
	private MainController controller;
	
	private PlayerView playerView;
	private Background background;
	private StartUpView startUpView;
	private PauseMenuView pauseMenuView;
	private InGameMenuView inGameMenuView;
	
	private BorderPane root;
	private StackPane playerAndMenusPane;
	
	private boolean gameHasLoaded;
	private boolean pauseMenuIsOpen;
	private boolean inGameMenuIsOpen;
	
	public MainScene(MainController controller) {
		super(new Pane());
		
		this.controller = controller;
		
		setUpRoot();
	}
	
	private void setUpRoot() {
		root = new BorderPane();
		playerAndMenusPane = new StackPane();
		
		pauseMenuView = new PauseMenuView(this);
		inGameMenuView = new InGameMenuView();
		
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
		playerAndMenusPane.getChildren().add(playerView);
		root.setCenter(playerAndMenusPane);
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
	
	public void stopNPCThreads() {
		controller.stopNPCThreads();
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
	
	private void togglePauseMenu() {
		if(pauseMenuIsOpen) {
			playerAndMenusPane.getChildren().add(pauseMenuView);
			pauseMenuView.requestFocusForButtons();
			setCursor(Cursor.DEFAULT);
		} else {
			playerAndMenusPane.getChildren().remove(pauseMenuView);
			pauseMenuView.resetView();
			root.requestFocus();
			setCursor(Cursor.NONE);
		}
	}
	
	private void pauzeOrResumeGame() {
		if(pauseMenuIsOpen) {
			setAllKeyPressesFalse();
			controller.pauzeGame();
		} else {
			controller.resumeGame();
		}
	}
	
	private void toggleInGameMenu() {
		if(inGameMenuIsOpen) {
			playerAndMenusPane.getChildren().add(inGameMenuView);
			inGameMenuView.requestFocusForButtons();
			setAllKeyPressesFalse();
		} else {
			playerAndMenusPane.getChildren().remove(inGameMenuView);
			root.requestFocus();
		}
	}
	
	private void handlePauseMenu() {
		pauseMenuIsOpen = !pauseMenuIsOpen;
		togglePauseMenu();
		pauzeOrResumeGame();
	}
	
	private void handleInGameMenu() {
		inGameMenuIsOpen = !inGameMenuIsOpen;
		toggleInGameMenu();
	}
	
	private void handleInputKeyPressed(KeyEvent e) {
		switch(e.getCode()) {
		case E: 
			if(gameHasLoaded && !pauseMenuIsOpen && !inGameMenuIsOpen) {
				controller.playerInteract();
			}
			break;
		case ESCAPE: 
			if(gameHasLoaded) {
				handlePauseMenu();
			}
			break;
		case I:
			if(gameHasLoaded && !pauseMenuIsOpen) {
				handleInGameMenu();
			}
			break;
		case F11:
			controller.setFullScreen(!controller.isFullScreen());
			controller.resizeBackgroundAndNPCLocation();
			if(gameHasLoaded) {
				if(controller.isFullScreen() && !pauseMenuIsOpen) {
					setCursor(Cursor.NONE);
				} else {
					setCursor(Cursor.DEFAULT);
				}
			}
			break;
		default: 
			if(gameHasLoaded && !pauseMenuIsOpen && !inGameMenuIsOpen) {
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
		if(gameHasLoaded && !pauseMenuIsOpen) {
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
	
	private void setAllKeyPressesFalse() {
		controller.getUpPressed().set(false);
		controller.getDownPressed().set(false);
		controller.getRightPressed().set(false);
		controller.getLeftPressed().set(false);
	}
}
