package view;

import java.util.ArrayList;
import java.util.HashMap;

import controller.FileIO;
import controller.MainController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.Building;
import model.Direction;
import model.Image;
import model.Item;
import model.Location;
import model.Player;
import model.Shop;
import model.Size;
import view.Buildings.BrickBuildingView;
import view.Buildings.BuildingView;
import view.Buildings.ShopView;
import view.Buildings.TentView;

public class MainScene extends Scene {

	public final static int SCENEWIDTH = 700;
	public final static int SCENEHEIGHT = 700;
	public final static String FONTNAME = "Times New Roman";
	public final static int STANDARD_IMAGE_SIZE = FileIO.STANDARD_IMAGE_SIZE;
	
	private MainController controller;
	
	private LoadingView loadingView;
	private PlayerView playerView;
	private Background background;
	private StartUpView startUpView;
	private PauseMenuView pauseMenuView;
	private InGameMenuView inGameMenuView;
	private InventoryView inventoryView;
	private LoadGameView loadGameView;
	private NewGameView newGameView;
	private BuildingView buildingView;
	
	private BorderPane root;
	private StackPane menusPane;
	
	private boolean gameHasLoaded;
	private boolean pauseMenuIsOpen;
	private boolean inGameMenuIsOpen;
	private boolean playerIsInDialog;
	private boolean inventoryIsOpen;
	private boolean inBuilding;
	
	private ArrayList<DialogView> dialogs;
	
	public MainScene(MainController controller) {
		super(new Pane());
		
		this.controller = controller;
		this.dialogs = new ArrayList<DialogView>();
		setUpRoot();
	}
	
	private void setUpRoot() {
		root = new BorderPane();
		menusPane = new StackPane();
		
		loadingView = new LoadingView();
		pauseMenuView = new PauseMenuView(this);
		inGameMenuView = new InGameMenuView(this);
		inventoryView = new InventoryView(this);
		
		root.setPrefSize(SCENEWIDTH, SCENEHEIGHT);
		
		setUpStartUpView();
		setUpListeners();
		
		getStylesheets().addAll("CSS/stylesheet.css", "CSS/start.css", "CSS/pause-menu.css", "CSS/in-game-menu.css", "CSS/dialogs.css");
		
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
	
	private void createPlayerView() {
		playerView = new PlayerView(controller.getPlayerURL());
		playerView.setLayoutX(getWidth()/2);
		playerView.setLayoutY(getHeight()/2);
		playerView.fixImage();
		
		root.getChildren().add(playerView);
		root.setCenter(menusPane);
	}
	
	public void reloadMenusPaneAndPlayerView() {
		root.getChildren().remove(playerView);
		root.getChildren().add(playerView);
		
		root.setCenter(null);
		root.setCenter(menusPane);
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
		setLoadingView(player.getName());
	}
	
	public void deletePlayer(String playerName) {
		controller.deletePlayer(playerName);
		addLoadGameView();
	}
	
	public boolean nameIsUnique(String name) {
		return controller.nameIsUnique(name);
	}
	
	public ArrayList<String> getAllPlayerNames(){
		return controller.getAllPlayerNames();
	}
	
	public Player getPlayer() {
		return controller.getPlayer();
	}
	
	public void loadGame(String playerName) {
				
		gameHasLoaded = true;
		
		root.setCenter(null);
		
		addBackground();
		
		controller.loadGame(playerName);
		
		Location bgLocation = controller.getBackgroundLocation();
		moveBackground(bgLocation.getX(), bgLocation.getY(), true);
		createPlayerView();
		
		inGameMenuView.setPlayerPane();
		
		setCursor(Cursor.NONE);
	}
	
	public void setLoadingView(String playerName) {
		root.setCenter(loadingView);
		
		new Thread(new Task<Void>() {
	        @Override
	        protected Void call() {
	    		controller.loadBackground();
	            return null;
	        }

	        @Override
	        protected void succeeded() {
	        	loadingView.stopAnimation();
	        	Platform.runLater(() -> loadGame(playerName));
	        }
	    }).start();
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
	
	public void dropItem(ItemView itemView) {
		controller.dropItem(itemView);
	}
	
	public void openInventory() {
		menusPane.getChildren().add(inventoryView);
		inGameMenuView.setDisable(true);
		inventoryView.requestFocusForButton();
		inventoryIsOpen = true;
		setCursor(Cursor.DEFAULT);
	}
	
	public void removeInventoryView() {
		menusPane.getChildren().remove(inventoryView);
		inventoryView.disableActiveSlot();
		inGameMenuView.setDisable(false);
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
		addDialog(new DialogView(dialogText, this));
	}
	
	public void addItemDialogView(String itemName) {
		String dialogText = "You've collected a(n) " + itemName + "!";
		
		addDialog(new ItemDialogView(dialogText, this));
	}
	
	public void addInteractiveDialogView(String dialogText, HashMap<Integer, String> options) {
		addDialog(new InteractiveDialogView(dialogText, this, options));
	}
	
	public void addOptionDialogView(String dialogText, int optionChosen) {
		addDialog(new OptionDialogView(dialogText, this, optionChosen));
	}
	
	private void addDialog(DialogView dialogView) {
		menusPane.getChildren().add(0, dialogView);
		
		dialogs.add(dialogView);
		dialogs.get(0).requestFocus();
		
		playerIsInDialog = true;
	}
	
	public void removeDialog(DialogView dialogView) {
		menusPane.getChildren().remove(dialogView);
		dialogs.remove(dialogView);
		if(dialogs.size() == 0) {
			playerIsInDialog = false;
			controller.endDialog();
			return;
		}
		setFocusOnNextDialog();
	}
	
	private void setFocusOnNextDialog() {
		DialogView nextDialogView = dialogs.get(0);
		nextDialogView.requestFocus();
		if(nextDialogView instanceof InteractiveDialogView) {
			((InteractiveDialogView) nextDialogView).addOptions();
		}
	}
	
	public void addOptionsForInteractiveDialog() {
		if(getFocusOwner() instanceof InteractiveDialogView) {
			((InteractiveDialogView) getFocusOwner()).addOptions();
		}
	}
	
	public void removeOptionDialogs(int number) {
		for(int i = 0; i < dialogs.size(); i++) { // ConcurrentModificationException
			DialogView dialogView = dialogs.get(i);
			if(dialogView instanceof OptionDialogView && ((OptionDialogView) dialogView).getOptionChosen() != number) {
				removeDialog(dialogView);
				i--;
			}
		}
	}
	
	public void resizePlayerViewLocation() {
		if(playerView != null) {
			playerView.move((int) getWidth()/2, (int) getHeight()/2);
			playerView.fixImage();
		}
	}
	
	public void addItemViewToInventoryView(Item item, ItemView itemView, boolean inventoryIsFull) {
		if(!inventoryIsFull) {			
			removeItemView(itemView);
			inventoryView.addItemView(itemView);
		} else {
			addDialogView("Your Inventory is full");
		}
	}
	
	public void removeItemViewFromInventoryView(ItemView itemView) {		
		inventoryView.removeItemView(itemView);
	}
	
	public Item getItemFromView(ItemView itemView) {
		return controller.getItemFromView(itemView);
	}
	
	public void saveGame() {
		controller.saveGame();
	}
	
	public void setItemViewsInInventory(ArrayList<ItemView> itemViews) {
		inventoryView.setItemViews(itemViews);
	}
	
	public void setBuildingView(Building building) {
		
		root.getChildren().remove(background);
		
		root.setBackground(new javafx.scene.layout.Background(new BackgroundFill(Color.CADETBLUE, null, null)));
		
		buildingView = getBuildingViewByType(building);
		root.getChildren().add(buildingView);
		buildingView.toBack();
		
		inBuilding = true;
		addBuildingViewLocation(building);
		
	}
	
	private void addBuildingViewLocation(Building building) {
		int screenXDiffernce = (int) getWidth()/2 - SCENEWIDTH/2;
		int screenYDiffernce = (int) getHeight()/2 - SCENEHEIGHT/2;
		building.setViewLocation(new Location((int) buildingView.getLayoutX() - screenXDiffernce, 
				(int) buildingView.getLayoutY() - screenYDiffernce));
	}
	
	public void removeBuildingView() {		
		root.getChildren().add(background);
		background.toBack();
		inBuilding = false;		
		
		root.setBackground(null);
		root.getChildren().remove(buildingView);
	}
	
	private BuildingView getBuildingViewByType(Building building) {
		double newWidth = building.getWidth()/(double) MainScene.STANDARD_IMAGE_SIZE;
		double newHeight = building.getHeight()/(double) MainScene.STANDARD_IMAGE_SIZE;
		
		switch(building.getType()) {
			case BRICK:
				return new BrickBuildingView(new Size((int) newWidth, (int) newHeight), this, building.getExit());
			case SHOP:
				Shop shop = (Shop) building;
				return new ShopView(new Size((int) newWidth, (int) newHeight), this, shop.getExit(), shop.getColor(), shop.getFloorPattern());
			case TENT:
				return new TentView(new Size((int) newWidth, (int) newHeight), this, building.getExit(), building.getColor());
			default: return null;
		}
	}
	
	public void moveBuildingView(Location location) {
		buildingView.move(location.getX(), location.getY());
	}
	
	public void moveBuildingView(Direction dir) {
		buildingView.move(dir);
	}
	
	public String getImageUrlByIndex(int index) {
		return controller.getImageUrlByIndex(index);
	}
	
	public boolean isInBuilding() {
		return inBuilding;
	}
	
	public void addBuildingViewImage(String url, boolean canWalkOn, Location location) {
		controller.addBuildingViewImage(url, canWalkOn, location);
	}
	
	public ArrayList<Image> getBuildingViewImages() {
		return controller.getBuildingViewImages();
	}

	private void togglePauseMenu() {
		if(pauseMenuIsOpen) {
			menusPane.getChildren().add(pauseMenuView);
			pauseMenuView.requestFocusForButtons();
			setCursor(Cursor.DEFAULT);
			toggleDisablePanes(true);
		} else {
			menusPane.getChildren().remove(pauseMenuView);
			pauseMenuView.resetView();
			setCursor(Cursor.NONE);
			requestFocusForView();
			toggleDisablePanes(false);
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
	
	private void toggleDisablePanes(boolean disable) {
		inGameMenuView.setDisable(disable);
		inventoryView.setDisable(disable);
		for(DialogView dialogView : dialogs) {
			dialogView.setDisable(disable);
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
			inGameMenuView.fillGrid();
			menusPane.getChildren().add(inGameMenuView);
			inGameMenuView.requestFocusForButtons();
			setAllKeyPressesFalse();
		} else {
			menusPane.getChildren().remove(inGameMenuView);
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
			if(controller.isFullScreen() && !pauseMenuIsOpen && !inventoryIsOpen) {
				setCursor(Cursor.NONE);
			} else {
				setCursor(Cursor.DEFAULT);
			}
		}
		if(inGameMenuIsOpen) {
			inGameMenuView.fillGrid();
		}
	}
	
	// TODO Clean up
	private void handleInputKeyPressed(KeyEvent e) {
		switch(e.getCode()) {
		case T:
			// TODO Remove 
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
		case Q:
			if(gameHasLoaded && !pauseMenuIsOpen && inventoryIsOpen) {
				inventoryView.dropSelectedItem();
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
