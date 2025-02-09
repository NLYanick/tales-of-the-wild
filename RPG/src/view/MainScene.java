package view;

import java.util.ArrayList;

import controller.MainController;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import model.Direction;
import model.Item;
import model.Location;
import model.Player;

public class MainScene extends Scene {

	public final static int SCENEWIDTH = 700;
	public final static int SCENEHEIGHT = 700;
	public final static String FONTNAME = "Times New Roman";
	
	private MainController controller;
	
	private PlayerView playerView;
	private Background background;
	private StartUpView startUpView;
	private PauseMenuView pauseMenuView;
	private InGameMenuView inGameMenuView;
	private InventoryView inventoryView;
	private LoadGameView loadGameView;
	private NewGameView newGameView;
	
	private BorderPane root;
	private StackPane playerAndMenusPane;
	
	private boolean gameHasLoaded;
	private boolean pauseMenuIsOpen;
	private boolean inGameMenuIsOpen;
	private boolean playerIsInDialog;
	private boolean inventoryIsOpen;
	
	private ArrayList<DialogView> dialogs;
	
	public MainScene(MainController controller) {
		super(new Pane());
		
		this.controller = controller;
		this.dialogs = new ArrayList<DialogView>();
		setUpRoot();
	}
	
	private void setUpRoot() {
		root = new BorderPane();
		playerAndMenusPane = new StackPane();
		
		pauseMenuView = new PauseMenuView(this);
		inGameMenuView = new InGameMenuView(this);
		inventoryView = new InventoryView(this);
		
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
		playerView.setLayoutX(getWidth()/2);
		playerView.setLayoutY(getHeight()/2);
		playerView.fixImage();
		
		root.getChildren().add(playerView);
		root.setCenter(playerAndMenusPane);
	}
	
	public void addLoadGameView() {
		loadGameView = new LoadGameView(this);
		root.setCenter(null);
		root.setCenter(loadGameView);
	}
	public void addNewGameView() {
		newGameView = new NewGameView(this);
		root.setCenter(null);
		root.setCenter(newGameView);
	}
	
	public void goBackToStartUpView() {
		root.setCenter(null);
		root.setCenter(startUpView);
	}
	
	public void createPlayer(String name) {
		Player player = controller.createPlayer(name);
		loadGame(player);
	}
	
	public void deletePlayer(Player player) {
		controller.deletePlayer(player);
		addLoadGameView();
	}
	
	public boolean nameIsUnique(String name) {
		return controller.nameIsUnique(name);
	}
	
	public ArrayList<Player> getAllPlayers(){
		return controller.getAllPlayers();
	}
	
	public Player getPlayer() {
		return controller.getPlayer();
	}
	
	public void loadGame(Player player) {
		
		gameHasLoaded = true;
		
		root.setCenter(null);
		
		addBackground();
		controller.setUpNPCs();
		controller.setUpItems();
		controller.setUpBuildings();
		controller.loadPlayer(player);
		
		moveBackground(controller.getBackgroundLocation().getX(), controller.getBackgroundLocation().getY(), true);
		createPlayerView();
		
		inGameMenuView.setPlayerPane();
		
		setCursor(Cursor.NONE);
	}
	
	public void addNPCView(NPCView npcView) {
		root.getChildren().add(npcView);
	}
	
	public void addItemView(ItemView itemView) {
		root.getChildren().add(itemView);
	}
	
	public void removeItemView(ItemView itemView) {
		root.getChildren().remove(itemView);
	}
	
	public void openInventory() {
		playerAndMenusPane.getChildren().add(inventoryView);
		inventoryView.requestFocusForButton();
		inventoryIsOpen = true;
		setCursor(Cursor.DEFAULT);
	}
	
	public void removeInventoryView() {
		playerAndMenusPane.getChildren().remove(inventoryView);
		inGameMenuView.requestFocusForButtons();
		inventoryIsOpen = false;
		setCursor(Cursor.NONE);
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
	
	public void addDialogView(String dialogText) {
		DialogView dialogView = new DialogView(dialogText, this);
		playerAndMenusPane.getChildren().add(dialogView);
		
		dialogs.add(dialogView);
		dialogs.get(dialogs.size() - 1).requestFocus();
		
		playerIsInDialog = true;
	}
	
	public void removeDialog(DialogView dialogView) {
		playerAndMenusPane.getChildren().remove(dialogView);
		dialogs.remove(dialogView);
		if(dialogs.size() == 0) {
			playerIsInDialog = false;
			controller.resumeNearbyNPCThread();
			return;
		}
		dialogs.get(dialogs.size() - 1).requestFocus();
	}
	
	public boolean playerViewNextStepIsOnNPCView(NPCView npcView, Direction dir) {
		return playerView.nextStepIsOnNPCView(npcView, dir);
	}
	
	public boolean npcViewNextStepIsOnPlayerView(NPCView npcView, Direction dir) {
		return npcView.nextStepIsOnPlayerView(playerView, dir);
	}
	
	public void resizePlayerViewLocation() {
		if(playerView != null) {
			playerView.move((int) getWidth()/2, (int) getHeight()/2);
			playerView.fixImage();
		}
	}
	
	public void addItemViewToInventoryView(Item item, ItemView itemView, boolean inventoryIsFull) {
		if(!inventoryIsFull) {
			controller.addItemToPlayerInventory(item);
			
			removeItemView(itemView);
			inventoryView.addItemView(itemView);
		} else {
			addDialogView("Your Inventory is full");
		}
	}
	
	public void saveGame() {
		controller.saveGame();
	}
	
	public void setItemViewsInInventory(ArrayList<ItemView> itemViews) {
		inventoryView.setItemViews(itemViews);
	}

	private void togglePauseMenu() {
		if(pauseMenuIsOpen) {
			playerAndMenusPane.getChildren().add(pauseMenuView);
			pauseMenuView.requestFocusForButtons();
			setCursor(Cursor.DEFAULT);
		} else {
			playerAndMenusPane.getChildren().remove(pauseMenuView);
			pauseMenuView.resetView();
			setCursor(Cursor.NONE);
			requestFocusForView();
		}
	}
	
	private void requestFocusForView() {
		if(inventoryIsOpen) {
			inventoryView.requestFocusForButton();
			setCursor(Cursor.DEFAULT);
			return;
		} 
		if(inGameMenuIsOpen) {
			inGameMenuView.requestFocusForButtons();
			return;
		} 
		if(playerIsInDialog) {
			dialogs.get(dialogs.size() - 1).requestFocus();
			return;
		} 
		
		root.requestFocus();
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
			inGameMenuView.fillGrid();
			playerAndMenusPane.getChildren().add(inGameMenuView);
			inGameMenuView.requestFocusForButtons();
			setAllKeyPressesFalse();
		} else {
			playerAndMenusPane.getChildren().remove(inGameMenuView);
			inGameMenuView.resetView();
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
	
	private void handleFullScreen() {
		controller.setFullScreen(!controller.isFullScreen());
		controller.resizeLocationsInView();
		if(gameHasLoaded) {
			if(controller.isFullScreen() && !pauseMenuIsOpen) {
				setCursor(Cursor.NONE);
			} else {
				setCursor(Cursor.DEFAULT);
			}
		}
		if(inGameMenuIsOpen) {
			inGameMenuView.fillGrid();
		}
	}
	
	private void handleInputKeyPressed(KeyEvent e) {
		switch(e.getCode()) {
		// TODO Remove 
		case T:
			if(gameHasLoaded)
				controller.teleportPlayer(new Location(5100, 3500));
			break;
		case E: 
			if(gameHasLoaded && allIsClosed()) {
				controller.playerInteract();
			}
			break;
		case ESCAPE: 
			if(gameHasLoaded) {
				handlePauseMenu();
			}
			break;
		case I:
			if(gameHasLoaded && !pauseMenuIsOpen && !playerIsInDialog && !inventoryIsOpen) {
				handleInGameMenu();
			}
			break;
		case F11:
			handleFullScreen();
			break;
		default: 
			if(gameHasLoaded && allIsClosed()) {
				handleMovementPressed(e);
			}
		}
	}
	
	private void handleMovementPressed(KeyEvent e) {
		switch(e.getCode()) {
			case UP: case W:
				controller.getUpPressed().set(true);
				break;
			case DOWN: case S:
				controller.getDownPressed().set(true);
				break;
			case RIGHT: case D:
				controller.getRightPressed().set(true);
				break;
			case LEFT: case A:
				controller.getLeftPressed().set(true);
				break;
			default: System.out.println("Input not valid");
		}
	}
	
	private void handleMovementReleased(KeyEvent e) {
		if(gameHasLoaded && allIsClosed()) {
			switch(e.getCode()) {
				case UP: case W:
					controller.setMovingDirection(Direction.SOUTH);
					controller.getUpPressed().set(false);
					break;
				case DOWN: case S:
					controller.setMovingDirection(Direction.NORTH);
					controller.getDownPressed().set(false);
					break;
				case RIGHT: case D:
					controller.setMovingDirection(Direction.WEST);
					controller.getRightPressed().set(false);
					break;
				case LEFT: case A:
					controller.setMovingDirection(Direction.EAST);
					controller.getLeftPressed().set(false);
					break;
				default:
			}
		}
	}
	
	public void setAllKeyPressesFalse() {
		controller.getUpPressed().set(false);
		controller.getDownPressed().set(false);
		controller.getRightPressed().set(false);
		controller.getLeftPressed().set(false);
	}
	
	private boolean allIsClosed() {
		return !pauseMenuIsOpen && !inGameMenuIsOpen && !playerIsInDialog && !inventoryIsOpen;
	}

}
