package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.image.ImageView;
import model.Building;
import model.Dialog;
import model.Direction;
import model.Game;
import model.Item;
import model.Location;
import model.NPC;
import model.Player;
import model.Shop;
import model.ShopItem;
import model.Tile;
import view.Background;
import view.ItemView;
import view.MainScene;
import view.NPCView;
import view.Buildings.BuildingView;
import view.Buildings.ShopView;

@SuppressWarnings("static-access")
public class MainController {
	
	private Game game;
	
	private ApplicationController appController;
	private MovementController movementController;
	private DatabaseController databaseController;
	private InputController inputController;
	
	private MainScene scene;
	private FileIO fileIO;
	
	private HashMap<NPC, NPCView> npcsWithViews;
	private HashMap<Item, ItemView> itemsWithViews;
	private HashMap<Tile, ImageView> bgBuildingsWithViews;
	private HashMap<Building, BuildingView> buildingsWithViews;
	
	public MainController(ApplicationController appController) {
		
		this.appController = appController;
		movementController = new MovementController(this);
		databaseController = new DatabaseController(this);
		inputController = new InputController(this);
		
		fileIO = new FileIO();
		scene = new MainScene(this, inputController);
		
		inputController.setScene(scene);
		
		npcsWithViews = new HashMap<NPC, NPCView>();
		itemsWithViews = new HashMap<Item, ItemView>();
		bgBuildingsWithViews = new HashMap<Tile, ImageView>();
		buildingsWithViews = new HashMap<Building, BuildingView>();
	}
	
	public void setPlayer(Player player) {
		game.setPlayer(player);
		movementController.setPlayer(player);
	}
	
	public void setGame(Game game) {
		this.game = game; 
		game.setMainController(this);
	}
		
	public void addNPCView(NPC npc, int bgX, int bgY) {
		NPCView npcView = new NPCView(npc.getURL(), npc.getStartLocation().getX(), npc.getStartLocation().getY());
		npcView.fixImage();
		npc.setViewLocation(new Location(bgX + (int) npcView.getLayoutX(), bgY + (int) npcView.getLayoutY()));
		
		scene.addNPCView(npcView);
		npcsWithViews.put(npc, npcView);
	}
	
	public void addItemView(Item item) {
		ItemView itemView = new ItemView(item.getLocation(), item.getImageUrl());
		itemsWithViews.put(item, itemView);
	}
	
	public void addItemViewToScene(Item item) {
		ItemView itemView = new ItemView(item.getLocation(), item.getImageUrl());
		itemsWithViews.put(item, itemView);
		
		scene.addItemView(itemView);
		moveItemViewWithScreen(item);
	}
	
	public void addBuildingView(Building building) {
		BuildingView buildingView = scene.getBuildingViewByType(building);
		buildingsWithViews.put(building, buildingView);
	}
	
	public void addItemViewsToInventoryViews() {
		addPlayerItemViews();
		addShopItemViews();
	}
	
	private void addPlayerItemViews() {
		ArrayList<ItemView> itemViews = new ArrayList<ItemView>();
		for(Item playerItem : game.getPlayerItems()) {
			ItemView itemView = itemsWithViews.get(playerItem);
			scene.removeItemView(itemView);
			itemViews.add(itemView);
		}
		
		scene.setItemViewsInInventory(itemViews);
	}
	
	private void addShopItemViews() {
		for (Building building : game.getBuildings()) {
			if(building instanceof Shop) {
				Shop shop = (Shop) building;
				
				ArrayList<ItemView> itemViews = new ArrayList<ItemView>();
				
				addShopItemToList(shop, itemViews);
				
				ShopView shopView = (ShopView) buildingsWithViews.get(building);
				shopView.getInventoryView().setItemViews(itemViews);
			}
		}
	}
	
	private void addShopItemToList(Shop shop, ArrayList<ItemView> itemViews) {
		for(ShopItem shopItem : shop.getShopItems()) {
			Item item = shopItem.getItem();
			
			ItemView itemView = itemsWithViews.get(item);
			scene.removeItemView(itemView);
			
			itemViews.add(itemView);
		}
	}
	
	public ArrayList<ItemView> getCurrentShopItemViews() {
		ArrayList<ItemView> itemViews = new ArrayList<ItemView>();
		
		Shop currentBuilding = (Shop) game.getCurrentBuilding();
		for(ShopItem shopItem : currentBuilding.getShopItems()) {
			ItemView itemView = itemsWithViews.get(shopItem.getItem());
			itemViews.add(itemView);
		}
		
		return itemViews;
	}

	public void teleportImages() {
		
		game.setBackgroundLocation();
		
		int bgX = game.getBackgroundX();
		int bgY = game.getBackgroundY();
		
		teleportNPCImages(bgX, bgY);
		teleportWorldItemImages(bgX, bgY);
		teleportBuildingImages(bgX, bgY);
	}
	
	private void teleportNPCImages(int bgX, int bgY) {
		for(NPC npc : game.getNPCs()) {
			teleportNPCView(bgX, bgY, npc);
		}
	}
	
	private void teleportWorldItemImages(int bgX, int bgY) {
		for(Item item : game.getWorldItems()) {
			teleportItemView(bgX, bgY, item);
		}
	}
	
	private void teleportBuildingImages(int bgX, int bgY) {
		for(Building building : game.getBuildings()) {
			for(Item item : building.getItems()) {
				teleportItemView(bgX, bgY, item);
			}
			for(NPC npc : building.getNPCs()) {
				teleportNPCView(bgX, bgY, npc);
			}
		}
	}
	
	private void teleportItemView(int bgX, int bgY, Item item) {
		ItemView itemView = itemsWithViews.get(item);
		item.setViewLocation(new Location(bgX + item.getX(), bgY + item.getY()));
		itemView.move(item.getViewLocation());
		moveItemViewWithScreen(item);
	}
	
	private void teleportNPCView(int bgX, int bgY, NPC npc) {
		NPCView npcView = npcsWithViews.get(npc);
		npc.setViewLocation(new Location(bgX + (int) npc.getX(), bgY + (int) npc.getY()));
		Location viewLoc = npc.getViewLocation();
		
		npcView.move(viewLoc.getX(), viewLoc.getY());
		npcView.fixImage();
		npc.setViewLocation(new Location((int) npcView.getLayoutX(), (int) npcView.getLayoutY()));
		moveNPCViewWithScreen(npc);
	}
	
	public void setPlayerStandingStillAnimation(Direction dir) {
		game.setStandingStillAnimation(dir);
		scene.changePlayerImage();
	}
	
	public void setPlayerImage(Direction dir) {
		game.setRunningImage(dir);
		scene.changePlayerImage();
	}
	
	public void setFullScreen(boolean isFullScreen) {
		appController.setFullScreen(isFullScreen);
	}
	
	public boolean isFullScreen() {
		return appController.isFullScreen();
	}
	
	public void switchNPCImage(NPC npc, String url) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				scene.changeNPCImage(npcsWithViews.get(npc), url);
			}
		});
	}
	
	public void resizeLocationsInView() {
		if(game == null) return;
		
		scene.moveBackground(game.getBackgroundX(), game.getBackgroundY(), appController.isFullScreen());
		scene.resizePlayerViewLocation();
		
		game.resizeLocationsInView();
	}
	
	public void moveNPCViewWithScreen(NPC npc) {
		int screenXDiffernce = (int) scene.getWidth()/2 - scene.SCENEWIDTH/2;
		int screenYDiffernce = (int) scene.getHeight()/2 - scene.SCENEHEIGHT/2;
				
		NPCView npcView = npcsWithViews.get(npc);
		Location viewLoc = npc.getViewLocation();
		if(appController.isFullScreen()) {
			npcView.move(viewLoc.getX() + screenXDiffernce, viewLoc.getY() + screenYDiffernce);
		} else {
			npcView.move(viewLoc.getX(), viewLoc.getY());
		}
	}
	
	public void moveItemViewWithScreen(Item item) {	
		int screenXDiffernce = (int) scene.getWidth()/2 - scene.SCENEWIDTH/2;
		int screenYDiffernce = (int) scene.getHeight()/2 - scene.SCENEHEIGHT/2;
		
		ItemView itemView = itemsWithViews.get(item);
		if(appController.isFullScreen()) {
			itemView.move(new Location(item.getViewLocation().getX() + screenXDiffernce, 
					item.getViewLocation().getY() + screenYDiffernce));
		} else {
			itemView.move(item.getViewLocation());
		}
	}
	
	public void moveBuildingViewWithScreen(Building building) {
		int screenXDiffernce = (int) scene.getWidth()/2 - scene.SCENEWIDTH/2;
		int screenYDiffernce = (int) scene.getHeight()/2 - scene.SCENEHEIGHT/2;
		
		if(building.getViewLocation() == null) return;
		
		if(appController.isFullScreen()) {
			scene.moveBuildingView(new Location(building.getViewLocation().getX() + screenXDiffernce, 
					building.getViewLocation().getY() + screenYDiffernce));
		} else {
			scene.moveBuildingView(building.getViewLocation());
		}
	}
	
	public void moveBgBuildingsWithScreen(int backgroundX, int backgroundY) {
		ArrayList<Tile> buildingImages = getBuildingImages();
		
		for (Tile tile : buildingImages) {
			moveBgBuildingWithScreen(tile, backgroundX, backgroundY);
		}
	}
	
	public void moveBgBuildingWithScreen(Tile tile, int backgroundX, int backgroundY) {	
		int screenXDiffernce = (int) scene.getWidth()/2 - scene.SCENEWIDTH/2;
		int screenYDiffernce = (int) scene.getHeight()/2 - scene.SCENEHEIGHT/2;
		
		ImageView imageView = bgBuildingsWithViews.get(tile);
		Location location = tile.getLocation();
		int x = location.getX() + backgroundX;
		int y = location.getY() + backgroundY;
		
		if(appController.isFullScreen()) {
			imageView.setLayoutX(x + screenXDiffernce);
			imageView.setLayoutY(y + screenYDiffernce);
		} else {
			imageView.setLayoutX(x);
			imageView.setLayoutY(y);
		}
	}
	
	public void putBgBuilding(Tile tile, ImageView buildingView) {
		bgBuildingsWithViews.put(tile, buildingView);
	}
	
	public void loadBuildingsLayer() {
		ArrayList<Tile> buildingImages = getBuildingImages();

		for (Tile tile : buildingImages) {
			scene.addBgBuilding(tile);
		}
	}
	
	public void dropItem(ItemView itemView) {
		Item item = getItemFromView(itemView);
		
		if(item != null) {
			int diff = 16;
			Building currentBuilding = game.getCurrentBuilding();
			
			Location newLocation = new Location(getPlayerLocation().getX() - diff, getPlayerLocation().getY() + diff);
			
			item.setLocation(newLocation);
			game.dropItemFromPlayerInventory(game.getPlayerInventoryItemWithId(item.getId()));
			
			if(currentBuilding != null && scene.isInBuilding().get()) {
				currentBuilding.addItem(item);
				databaseController.dropItemInBuilding(item, 
					new Location(item.getX() + -currentBuilding.getX(), item.getY() + -currentBuilding.getY()), currentBuilding.getId());
				item.setViewLocation(new Location(currentBuilding.getViewLocation().getX() + -currentBuilding.getX() + item.getX(), 
					currentBuilding.getViewLocation().getY() + -currentBuilding.getY() + item.getY()));
			} else {
				game.addWorldItem(item);
				databaseController.dropItem(item, newLocation);
				item.setViewLocation(new Location(game.getBackgroundX() + item.getX(), game.getBackgroundY() + item.getY()));
			}
			
			moveDroppedItemView(itemView, item);
		}
	}
	
	private void moveDroppedItemView(ItemView itemView, Item item) {
		itemView.move(item.getViewLocation());
		
		scene.removeItemViewFromInventoryView(itemView);
		scene.addItemView(itemView);
		
		moveItemViewWithScreen(item);
		
		scene.updateLayersPositions();
	}
	
	public Item getItemFromView(ItemView itemView) {
		for(Item item : itemsWithViews.keySet()) {
			if(itemsWithViews.get(item) == itemView) {
				return item;
			}
		}
		return null;
	}
	
	public void addDialogView(NPC npc) {
		for(Dialog dia : npc.getDialogs()) {
			if(dia.shouldSkip() && dia.hasPlayed()) 
				continue;
			
			if(dia.getItemId() > 0) {
				Item item = getDialogItem(npc.getItems(), dia.getItemId());
				
				if(item == null) continue;
				
				addItemDialogView(dia.getText(), item, npc);
				
			} else if(dia.isInteractive()) {
				scene.addInteractiveDialogView(dia.getText(), dia.getOptions());
			} else if(dia.getOptionChosen() > 0) {
				scene.addOptionDialogView(dia.getText(), dia.getOptionChosen(), dia.getAction());
			} else {
				scene.addDialogView(dia.getText());
			}
			
			dia.setHasPlayed(true);
		}
		scene.addOptionsForInteractiveDialog();
	}
	
	private void addItemDialogView(String text, Item item, NPC npc) {
		addSingleDialogView(text);
		scene.addItemDialogView(item.getName());
		
		game.addItemToPlayerInventory(item);
		addItemViewToInventoryView(item);
		npc.removeItem(item);
		databaseController.removeItemFromNPC(item);
	}
	
	public <T> ArrayList<T> reverseSort(ArrayList<T> list) {
		int j = list.size() - 1;
		T temp;
		for(int i = 0; i < list.size(); i++) {
			if(i >= j) {
				break;
			}
			
			temp = list.get(j);
			list.remove(temp);
			list.add(j, list.get(i));
			list.remove(list.get(i));
			list.add(i, temp);
			
			j--;
		}
		return list;
	}
	
	private Item getDialogItem(ArrayList<Item> items, int number) {
		for(Item item : items) {
			if(item.getId() == number) {
				return item;
			}
		}
		
		return null;
	}
	
	public void setBuildingView(Building building) {
		game.clearBuildingViewImages();
		scene.setBuildingView(building);
	}
	
	public String getImageUrlByIndex(int index) {
		return fileIO.getImageUrlByIndex(index);
	}
	
	public void addBuildingViewTile(String url, boolean canWalkOn, Location location) {
		Tile tile = new Tile(url, canWalkOn);
		tile.setLocation(location);
		game.addBuildingViewImage(tile);
	}
	
	public void endDialog() {
		NPC npc = game.getDialogNPC();
		if(npc != null) {			
			saveNPCDialog(npc);
			game.resumeNearbyNPCThread();
			game.resetDialogNPC();
		}
	}
	
	public void loadBackground() {
		fileIO.readText(new File(FileIO.BACKGROUNDFILEPATH));
	}
	
	
	// -------------------- Pass Methodes --------------------
	
	public void moveBackgroundAndPlayer(Direction dir) {
		game.moveBackgroundAndPlayer(dir);
	}
	
	public void teleportPlayer(Location location) {
		game.teleportPlayer(location);
	}
	
	public void stopNPCThreads() {
		if(game != null) game.stopNPCThreads();
	}
	
	public void pauzeGame() {
		game.pauzeGame();
	}
	
	public void resumeGame() {
		game.resumeGame();
	}
	
	public void playerInteract() {
		game.playerInteract();
	}
	
	public void buyItem(ItemView itemView) {
		game.buyItem(getItemFromView(itemView));
	}
	
	public void updateCoinsText(int talesCoins) {
		scene.updateCoinsText(talesCoins);
	}
	
	public void moveBuildingView(Direction dir) {
		scene.moveBuildingView(dir);
	}
	
	public void moveBackground(int backgroundX, int backgroundY) {
		scene.moveBackground(backgroundX, backgroundY, isFullScreen());
	}
	
	public void addItemViewToInventoryView(Item item) {
		scene.addItemViewToInventoryView(item, itemsWithViews.get(item), game.playerInventoryIsFull());
	}
	
	public void removeItemFromShopInventoryView(Item item) {
		ShopView shopView = (ShopView) buildingsWithViews.get(game.getCurrentBuilding());
		shopView.getInventoryView().removeItemView(itemsWithViews.get(item));
	}
	
	public void addSingleDialogView(String text) {
		scene.addDialogView(text);
	}
	
	public void addSingleItemDialogView(Item item) {
		scene.addItemDialogView(item.getName());
	}
	
	public void removeBuildingView() {
		scene.removeBuildingView();
	}
	
	public void setAllKeyPressesFalse() {
		scene.setAllKeyPressesFalse();
	}
	
	public void updateLayersPositions() {
	    scene.updateLayersPositions();
	}
	
	public void setShopModal(String message) {
		scene.setShopModal(message);
	}
	
	// -------------------- Database --------------------
	
	public void loadGame(String playerName) {
		if(playerName == null || playerName == "") {
			throw new NullPointerException();
		}
		databaseController.loadGame(playerName);
	}
	
	public void saveGame() {
		Player player = game.getPlayer();
		if(scene.isInBuilding().get()) {
			player.setLocation(game.getCurrentBuildingLeaveLocation());
		}
		databaseController.saveGame(player);
	}
	
	public Player createPlayer(String name) {
		return databaseController.createPlayer(name);
	}
	
	public boolean nameIsUnique(String name) {
		return databaseController.nameIsUnique(name);
	}
	
	public void deletePlayer(String playerName) {
		databaseController.deletePlayer(playerName);
	}
	
	public Location getOriginalNPCBuildingLocation(NPC npc, int buildingId) {
		if(buildingId > 0) {
			return databaseController.getOriginalNPCBuildingLocation(npc, buildingId);
		}
		return null;
	}
	
	public Location getOriginalItemBuildingLocation(Item item, int buildingId) {
		if(buildingId > 0) {
			return databaseController.getOriginalItemBuildingLocation(item, buildingId);
		}
		return null;
	}
	
	public void saveNPCDialog(NPC npc) {
		databaseController.saveNPCDialog(npc);
	}
	
	public void addItemToPlayer(Item item) {
		databaseController.addItemToPlayer(item, game.getPlayer());
	}
	
	public void saveBoughtItem(Item item) {
		databaseController.buyItem(item, game.getPlayer());
	}
	
	public void updatePlayerCoins() {
		databaseController.updatePlayerCoins(game.getPlayer());
	}
	
	public void setItemLocation(Item item, Location location) {
		databaseController.setItemLocation(item, location);
	}
	
	public void removeItemFromBuilding(Item item) {
		databaseController.removeItemFromBuilding(item);
	}
	
	public ArrayList<NPC> getAllNPCs() {
		return databaseController.getAllNPCs(game.getId());
	}
	
	public ArrayList<Item> getAllWorldItems() {
		return databaseController.getAllWorldItems(game.getId());
	}
	
	public ArrayList<Item> getNPCItems(NPC npc){
		return databaseController.getNPCItems(npc);
	}
	
	public ArrayList<Item> getBuildingItems(int buildingId) {
		return databaseController.getBuildingItems(buildingId);
	}
	
	public ArrayList<ShopItem> getShopItems(int shopId) {
		return databaseController.getShopItems(shopId);
	}
	
	public ArrayList<NPC> getBuildingNPCs(int buildingId) {
		return databaseController.getBuildingNPCs(buildingId);
	}
	
	public ArrayList<Building> getAllBuildings() {
		return databaseController.getAllBuildings(game.getId());
	}
	
	public ArrayList<String> getAllPlayerNames() {
		return databaseController.getAllPlayerNames();
	}
	
	// -------------------- Getters & Setters --------------------
	
	public BooleanProperty getUpPressed() {
		return movementController.getUpPressed();
	}
	
	public BooleanProperty getLeftPressed() {
		return movementController.getLeftPressed();
	}
	
	public BooleanProperty getDownPressed() {
		return movementController.getDownPressed();
	}
	
	public BooleanProperty getRightPressed() {
		return movementController.getRightPressed();
	}
	
	public MainScene getMainScene() {
		return scene;
	}
	
	public Player getPlayer() {
		return game.getPlayer();
	}
	
	public String getPlayerURL() {
		return game.getPlayerURL();
	}
	
	public ArrayList<Tile> getImagesInFile() {
		return fileIO.getImagesInFile();
	}
	
	public ArrayList<Tile> getBuildingImages() {
		return fileIO.getBuildingImages();
	}
	
	public Background getBackground() {
		return fileIO.getBackground();
	}
	
	public void setMovingDirection(Direction dir) {
		game.setPlayerMovingDirection(dir);
	}
	
	public Direction getMovingDirection() {
		return game.getPlayerMovingDirection();
	}
	
	public Location getBackgroundLocation() {
		return game.getBackgroundLocation();
	}
	
	public Location getPlayerLocation() {
		return game.getPlayerLocation();
	}
	
	public ShopItem getCurrentBuildingShopItem(Item item) {
		return game.getCurrentBuildingShopItem(item);
	}
	
	public BuildingView getBuildingView(Building building) {
		return buildingsWithViews.get(building);
	}

}
