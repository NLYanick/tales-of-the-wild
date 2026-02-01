package view;

import java.util.ArrayList;
import java.util.HashMap;

import controller.FileIO;
import controller.InputController;
import controller.MainController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.Building;
import model.Direction;
import model.Item;
import model.Location;
import model.Player;
import model.Shop;
import model.ShopItem;
import model.Tile;
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
	private InputController inputController;

	private LoadingView loadingView;
	private StartUpView startUpView;
	private PauseMenuView pauseMenuView;
	private InGameMenuView inGameMenuView;
	private InventoryView inventoryView;
	private LoadGameView loadGameView;
	private NewGameView newGameView;

	private BorderPane root;
	private Background background;
	private PlayerView playerView;
	private StackPane menusPane;
	private BuildingView buildingView;
	private EntityLayer entityLayer;

	private BooleanProperty gameLoaded = new SimpleBooleanProperty(false);
	private BooleanProperty pauseMenuOpen = new SimpleBooleanProperty(false);
	private BooleanProperty inGameMenuOpen = new SimpleBooleanProperty(false);
	private BooleanProperty playerInDialog = new SimpleBooleanProperty(false);
	private BooleanProperty inventoryOpen = new SimpleBooleanProperty(false);
	private BooleanProperty inBuilding = new SimpleBooleanProperty(false);

	private ArrayList<DialogView> dialogs;

	public MainScene(MainController controller, InputController inputController) {
		super(new Pane());

		this.controller = controller;
		this.inputController = inputController;
		this.dialogs = new ArrayList<DialogView>();
		setUpRoot();
	}

	private void setUpRoot() {
		root = new BorderPane();
		menusPane = new StackPane();		
		entityLayer = new EntityLayer();
		
		loadingView = new LoadingView();
		pauseMenuView = new PauseMenuView(this);
		inGameMenuView = new InGameMenuView(this);
		inventoryView = new InventoryView(this);

		root.setPrefSize(SCENEWIDTH, SCENEHEIGHT);

		setUpStartUpView();
		setUpListeners();

		getStylesheets().addAll("CSS/stylesheet.css", "CSS/start.css", "CSS/pause-menu.css", "CSS/in-game-menu.css",
				"CSS/dialogs.css");

		setRoot(root);
	}

	private void setUpListeners() {
		setOnKeyPressed(e -> inputController.handleKeyPressed(e));
		setOnKeyReleased(e -> inputController.handleMovementReleased(e));
	}

	private void setUpStartUpView() {
		startUpView = new StartUpView(this);
		root.setCenter(startUpView);
	}

	private void addBackground() {
		background = controller.getBackground();

		background.setLayoutX(getWidth() / 2 - SCENEWIDTH / 2);
		background.setLayoutY(getHeight() / 2 - SCENEHEIGHT / 2);

		root.getChildren().add(background);
	}

	private void createPlayerView() {
		playerView = new PlayerView(controller.getPlayerURL());
		playerView.setLayoutX(getWidth() / 2);
		playerView.setLayoutY(getHeight() / 2);
		playerView.fixImage();

		entityLayer.getChildren().add(playerView);
		root.getChildren().add(entityLayer);
		root.setCenter(menusPane);
	}

	public void updateLayersPositions() {
		ArrayList<Node> sortedNodes = new ArrayList<>(entityLayer.getChildren());

		sortedNodes.sort((node1, node2) -> {
			double y1 = node1.getBoundsInParent().getMaxY();
			double y2 = node2.getBoundsInParent().getMaxY();

			return Double.compare(y1, y2);
		});

		entityLayer.getChildren().setAll(sortedNodes);
	}

	public void addLoadGameView() {
		loadGameView = new LoadGameView(this);
		root.setCenter(loadGameView);
	}

	public void addNewGameView() {
		newGameView = new NewGameView(this);
		root.setCenter(newGameView);
	}

	public void goBackToStartUpView() {
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
	
	public void addBgBuilding(Tile tile) {
		ImageView buildingView = entityLayer.addBuilding(tile.getX(), tile.getY(), tile.getUrl());
		controller.putBgBuilding(tile, buildingView);
	}

	public void loadGame(String playerName) {
		gameLoaded.set(true);

		root.setCenter(null);

		addBackground();
		controller.loadBuildingsLayer();
		
		controller.loadGame(playerName);

		Location bgLocation = controller.getBackgroundLocation();
		moveBackground(bgLocation.getX(), bgLocation.getY(), true);
		createPlayerView();

		inGameMenuView.setPlayerPane();
		
		updateLayersPositions();

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
		entityLayer.getChildren().add(npcView);
	}

	public void addItemView(ItemView itemView) {
		entityLayer.getChildren().add(itemView);
	}
	
	public void addBuildingView(Building building) {
		entityLayer.addBuilding(building.getViewLocation().getX(), building.getViewLocation().getY(), "");
	}

	public void removeItemView(ItemView itemView) {
		entityLayer.getChildren().remove(itemView);
	}
	
	public void openShopInventory(ShopInventoryView inventoryView) {
		openInventory(inventoryView);
	}
	
	public void openPlayerInventory() {
		openInventory(inventoryView);
	}
	
	private void openInventory(InventoryView inventoryView) {
		menusPane.getChildren().add(inventoryView);
		
		inGameMenuView.setDisable(true);
		inventoryView.requestFocusForButton();
		inventoryOpen.set(true);
		
		setCursor(Cursor.DEFAULT);
	}

	public void removeInventoryView(InventoryView inventoryView) {
		menusPane.getChildren().remove(inventoryView);
		
		inventoryView.disableActiveSlot();
		inGameMenuView.setDisable(false);
		inGameMenuView.requestFocusForButtons();
		inventoryOpen.set(false);
		
		setCursor(Cursor.NONE);
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
		if (background != null) {
			if (isFullScreen)
				background.move((int) (x + getWidth() / 2 - SCENEWIDTH / 2),
						(int) (y + getHeight() / 2 - SCENEHEIGHT / 2));
			else
				background.move(x, y);
		}
		
		controller.moveBgBuildingsWithScreen(x, y);
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

	public void addOptionDialogView(String dialogText, int optionChosen, String action) {
		addDialog(new OptionDialogView(dialogText, this, optionChosen, action));
	}

	private void addDialog(DialogView dialogView) {
		menusPane.getChildren().add(0, dialogView);

		dialogs.add(dialogView);
		dialogs.get(0).requestFocus();

		playerInDialog.set(true);
	}

	public void removeDialog(DialogView dialogView) {
		menusPane.getChildren().remove(dialogView);
		dialogs.remove(dialogView);
		
		checkSpecificDialogs(dialogView);
		
		if (dialogs.size() == 0) {
			playerInDialog.set(false);
			controller.endDialog();
			return;
		}
		
		setFocusOnNextDialog();
	}
	
	private void checkSpecificDialogs(DialogView dialogView) {
		if(dialogView instanceof OptionDialogView) {
			OptionDialogView optionDialog = (OptionDialogView) dialogView;
			if(optionDialog.isChosen()) {
				doDialogAction(optionDialog.getAction());
			}
		}
	}
	
	private void doDialogAction(String action) {
		switch(action) {
			case "OPEN_SHOP_INVENTORY": 
				ShopView shopView = (ShopView) buildingView;
				openShopInventory(shopView.getInventoryView());
				break;
			default: return;
		}
	}

	private void setFocusOnNextDialog() {
		DialogView nextDialogView = dialogs.get(0);
		nextDialogView.requestFocus();
		if (nextDialogView instanceof InteractiveDialogView) {
			((InteractiveDialogView) nextDialogView).addOptions();
		}
	}

	public void addOptionsForInteractiveDialog() {
		if (getFocusOwner() instanceof InteractiveDialogView) {
			((InteractiveDialogView) getFocusOwner()).addOptions();
		}
	}

	public void removeOptionDialogs(int number) {
		for (int i = 0; i < dialogs.size(); i++) { // ConcurrentModificationException
			DialogView dialogView = dialogs.get(i);
			if (dialogView instanceof OptionDialogView) {
				OptionDialogView optionDialog = (OptionDialogView) dialogView;
				if(optionDialog.getOptionChosen() != number) {
					optionDialog.setChosen(false);
					removeDialog(dialogView);
					i--;
				} else if(optionDialog.getOptionChosen() == number && optionDialog.getDialogText().isEmpty()) {
					optionDialog.setChosen(true);
					removeDialog(dialogView);
					i--;
				}
			} 
		}
	}

	public void resizePlayerViewLocation() {
		if (playerView != null) {
			playerView.move((int) getWidth() / 2, (int) getHeight() / 2);
			playerView.fixImage();
		}
	}

	public void addItemViewToInventoryView(Item item, ItemView itemView, boolean inventoryIsFull) {
		if (!inventoryIsFull) {
			removeItemView(itemView);
			inventoryView.addItemView(itemView);
		} else {
			addDialogView("Your Inventory is full");
		}
	}

	public void setBuildingView(Building building) {

		root.getChildren().remove(background);

		root.setBackground(new javafx.scene.layout.Background(new BackgroundFill(Color.CADETBLUE, null, null)));

		buildingView = controller.getBuildingView(building);
		buildingView.setLocation();
		
		root.getChildren().add(buildingView);
		buildingView.toBack();

		inBuilding.set(true);
		addBuildingViewLocation(building);	
	}

	private void addBuildingViewLocation(Building building) {
		int screenXDiffernce = (int) getWidth() / 2 - SCENEWIDTH / 2;
		int screenYDiffernce = (int) getHeight() / 2 - SCENEHEIGHT / 2;
		building.setViewLocation(new Location((int) buildingView.getLayoutX() - screenXDiffernce,
				(int) buildingView.getLayoutY() - screenYDiffernce));
	}

	public void removeBuildingView() {
		root.getChildren().add(background);
		background.toBack();
		inBuilding.set(false);

		root.setBackground(null);
		root.getChildren().remove(buildingView);
		
		buildingView = null;
	}

	public BuildingView getBuildingViewByType(Building building) {
		switch (building.getType()) {
		case BRICK:
			return new BrickBuildingView(this, building.getTiles(), building.getTileSize(),
					building.getSpawnLocation());
		case SHOP:
			Shop shop = (Shop) building;
			return new ShopView(this, shop.getTiles(), shop.getTileSize(), shop.getColor(), shop.getFloorPattern(),
					building.getSpawnLocation());
		case TENT:
			return new TentView(this, building.getColor(), building.getTiles(), building.getTileSize(),
					building.getSpawnLocation());
		default:
			return null;
		}
	}

	private void togglePauseMenu() {
		if (pauseMenuOpen.get()) {
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
		if (inventoryOpen.get()) {
			inventoryView.requestFocusForButton();
			setCursor(Cursor.DEFAULT);
			return;
		}
		if (inGameMenuOpen.get()) {
			inGameMenuView.requestFocusForButtons();
			return;
		}
		if (playerInDialog.get()) {
			dialogs.get(dialogs.size() - 1).requestFocus();
			return;
		}

		root.requestFocus();
	}

	private void toggleDisablePanes(boolean disable) {
		inGameMenuView.setDisable(disable);
		inventoryView.setDisable(disable);
		
		for (DialogView dialogView : dialogs) {
			dialogView.setDisable(disable);
		}
		
		if(buildingView instanceof ShopView)
			((ShopView) buildingView).getInventoryView().setDisable(disable);
	}

	private void pauzeOrResumeGame() {
		if (pauseMenuOpen.get()) {
			setAllKeyPressesFalse();
			controller.pauzeGame();
		} else {
			controller.resumeGame();
		}
	}

	private void toggleInGameMenu() {
		if (inGameMenuOpen.get()) {
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

	public void handlePauseMenu() {
		pauseMenuOpen.set(!pauseMenuOpen.get());
		togglePauseMenu();
		pauzeOrResumeGame();
	}

	public void handleInGameMenu() {
		inGameMenuOpen.set(!inGameMenuOpen.get());
		toggleInGameMenu();
	}

	public void handleFullScreen() {
		controller.setFullScreen(!controller.isFullScreen());
		controller.resizeLocationsInView();
		if (gameLoaded.get()) {
			if (controller.isFullScreen() && !pauseMenuOpen.get() && !inventoryOpen.get()) {
				setCursor(Cursor.NONE);
			} else {
				setCursor(Cursor.DEFAULT);
			}
		}
		if (inGameMenuOpen.get()) {
			inGameMenuView.fillGrid();
		}
	}
	
	public void setShopError(String error) {
		ShopView shopView = (ShopView) buildingView;
		
		shopView.setShopError(error);
	}

	
	// -------------------- Pass Methodes --------------------
	
	public boolean nameIsUnique(String name) {
		return controller.nameIsUnique(name);
	}

	public void stopNPCThreads() {
		controller.stopNPCThreads();
	}

	public void dropItem(ItemView itemView) {
		controller.dropItem(itemView);
	}
	
	public String getImageUrlByIndex(int index) {
		return controller.getImageUrlByIndex(index);
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

	public void moveBuildingView(Location location) {
		buildingView.move(location.getX(), location.getY());
	}

	public void moveBuildingView(Direction dir) {
		buildingView.move(dir);
	}

	public void addBuildingViewImage(String url, boolean canWalkOn, Location location) {
		controller.addBuildingViewTile(url, canWalkOn, location);
	}
	
	public void updateCoinsText(int coins) {
		inGameMenuView.setCoinsText(coins);
	}
	
	public void setAllKeyPressesFalse() {
		inputController.setAllKeyPressesFalse();
	}
	
	public ShopItem getCurrentBuildingShopItem(Item item) {
		return controller.getCurrentBuildingShopItem(item);
	}
	
	public void buyItem(ItemView itemView) {
		controller.buyItem(itemView);
	}
	
	// -------------------- Getters & Setters --------------------
	
	public ArrayList<String> getAllPlayerNames() {
		return controller.getAllPlayerNames();
	}
	
	public Player getPlayer() {
		return controller.getPlayer();
	}
	
	public InventoryView getInventoryView() {
		return inventoryView;
	}
	
	public BuildingView getBuildingView() {
		return buildingView;
	}
	
	public BooleanProperty gameHasLoaded() {
		return gameLoaded;
	}
	
	public BooleanProperty inGameMenuIsOpen() {
		return inGameMenuOpen;
	}
	
	public BooleanProperty inventoryIsOpen() {
		return inventoryOpen;
	}
	
	public BooleanProperty pauseMenuIsOpen() {
		return pauseMenuOpen;
	}
	
	public BooleanProperty playerIsInDialog() {
		return playerInDialog;
	}

	public BooleanProperty isInBuilding() {
		return inBuilding;
	}

}
