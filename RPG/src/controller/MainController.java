package controller;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import model.BackgroundLocation;
import model.Building;
import model.Dialog;
import model.Direction;
import model.Game;
import model.Image;
import model.Item;
import model.Location;
import model.NPC;
import model.Player;
import view.Background;
import view.ItemView;
import view.MainScene;
import view.NPCView;

@SuppressWarnings("static-access")
public class MainController {
	
	private Game game;
	
	private ApplicationController appController;
	private MovementController movementController;
	private DatabaseController databaseController;
	
	private MainScene scene;
	private FileIO fileIO;
	
	private HashMap<NPC, NPCView> npcsWithViews;
	private HashMap<Item, ItemView> itemsWithViews;
	
	public MainController(ApplicationController appController, FileIO fileIO) {
		
		this.fileIO = fileIO;
		scene = new MainScene(this);
		
		this.appController = appController;
		movementController = new MovementController(this);
		databaseController = new DatabaseController(this);
		
		npcsWithViews = new HashMap<NPC, NPCView>();
		itemsWithViews = new HashMap<Item, ItemView>();
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
		
		if(item.getNPCId() == 0) {			
			scene.addItemView(itemView);
			moveItemViewWithScreen(item);
		}
	}
	
	public void addPlayerItemViewsToInventoryView(ArrayList<Item> items) {
		ArrayList<ItemView> itemViews = new ArrayList<ItemView>();
		for(Item playerItem : game.getItemsOfPlayerInventory()) {
			for(int i = 0; i < items.size(); i++) {
				Item item = items.get(i);
				if(playerItem.getId() == item.getId()) {
					ItemView itemView = itemsWithViews.get(item);
					scene.removeItemView(itemView);
					itemViews.add(itemView);
				}
			}
		}
		
		scene.setItemViewsInInventory(itemViews);
	}
	
	public ArrayList<Image> getImagesInFile() {
		return fileIO.getImagesInFile();
	}
	
	public void teleportImages() {
		
		game.setBackgroundLocation();
		
		int bgX = game.getBackgroundX();
		int bgY = game.getBackgroundY();
		
		teleportNPCImages(bgX, bgY);
		
		teleportItemImages(bgX, bgY);
	}
	
	private void teleportNPCImages(int bgX, int bgY) {
		for(NPC npc : game.getNPCs()) {
			NPCView npcView = npcsWithViews.get(npc);
			npc.setViewLocation(new Location(bgX + (int) npc.getX(), bgY + (int) npc.getY()));
			npcView.move(npc.getViewLocation().getX(), npc.getViewLocation().getY());
			npcView.fixImage();
			npc.setViewLocation(new Location((int) npcView.getLayoutX(), (int) npcView.getLayoutY()));
			moveNPCViewWithScreen(npc);
		}
	}
	
	private void teleportItemImages(int bgX, int bgY) {
		for(Item item : game.getItems()) {
			ItemView itemView = itemsWithViews.get(item);
			item.setViewLocation(new Location(bgX + item.getX(), bgY + item.getY()));
			itemView.move(item.getViewLocation());
			moveItemViewWithScreen(item);
		}
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
		if(appController.isFullScreen()) {
			npcView.move(npc.getViewLocation().getX() + screenXDiffernce, npc.getViewLocation().getY() + screenYDiffernce);
		} else {
			npcView.move(npc.getViewLocation().getX(), npc.getViewLocation().getY());
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
	
	public void dropItem(ItemView itemView) {
		Item item = getItemFromView(itemView);
		
		if(item != null) {
			int diff = 32;
			Location newLocation = new Location(getPlayerLocation().getX() - diff, getPlayerLocation().getY() - diff);
			
			item.setLocation(newLocation);
			game.dropItemFromPlayerInventory(game.getPlayerInventoryItemWithId(item.getId()));
			databaseController.dropItem(item, newLocation);
			
			item.setViewLocation(new Location(game.getBackgroundX() + item.getX(), game.getBackgroundY() + item.getY()));
			itemView.move(item.getViewLocation());
			
			scene.removeItemViewFromInventoryView(itemView);
			scene.addItemView(itemView);
			
			moveItemViewWithScreen(item);
			
			scene.reloadMenusPaneAndPlayerView();
		}
	}
	
	public Item getItemFromView(ItemView itemView) {
		for(Item item : itemsWithViews.keySet()) {
			if(itemsWithViews.get(item) == itemView) {
				return item;
			}
		}
		return null;
	}
	
	public void addDialogView(ArrayList<Dialog> dialogs, ArrayList<Item> items) {
		scene.setPlayerIsInDialog(true);
		
		for(Dialog dia : dialogs) {
			if(dia.shouldSkip() && dia.hasPlayed()) {
				continue;
			}
			
			if(dia.getItemId() > 0) {
				Item item = getDialogItem(items, dia.getItemId());
				
				if(item == null || item.getNPCId() <= 0) {
					continue;
				}
				
				addItemDialogView(dia.getText(), item);
			} else {
				scene.addDialogView(dia.getText());
			}
			
			dia.setHasPlayed(true);
		}
	}
	
	private void addItemDialogView(String text, Item item) {
		addSingleDialogView(text);
		scene.addItemDialogView(item.getName());
		
		game.addItemToPlayerInventory(item);
		addItemViewToInventoryView(item);
		item.setNPCId(0);
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
	
	public void addBuildingViewImage(String url, boolean canWalkOn, Location location) {
		Image image = new Image(url, canWalkOn);
		image.setLocation(location);
		game.addBuildingViewImage(image);
	}
	
	public void endDialog() {
		NPC npc = game.getDialogNPC();
		if(npc != null) {			
			saveNPCDialog(npc);
			game.resumeNearbyNPCThread();
			game.resetDialogNPC();
		}
	}
	
	
	// -------------------- Pass Methodes --------------------
	
	public void startGame() {
		game.startGame();
	}
	
	public void moveBackgroundAndPlayer(Direction dir) {
		game.moveBackgroundAndPlayer(dir);
	}
	
	public void teleportPlayer(Location location) {
		game.teleportPlayer(location);
	}
	
	public void stopNPCThreads() {
		if(game != null) game.stopNPCThreads();
	}
	
	public void resumeNPCThreads() {
		game.resumeNPCThreads();
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
	
	public void moveBuildingView(Direction dir) {
		scene.moveBuildingView(dir);
	}
	
	public void moveBackground(int backgroundX, int backgroundY) {
		scene.moveBackground(backgroundX, backgroundY, isFullScreen());
	}
	
	public void addItemViewToInventoryView(Item item) {
		scene.addItemViewToInventoryView(item, itemsWithViews.get(item), game.playerInventoryIsFull());
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
	
	// -------------------- Database --------------------
	
	public void loadGame(String playerName) {
		if(playerName == null || playerName == "") {
			throw new NullPointerException();
		}
		databaseController.loadGame(playerName);
	}
	
	public void saveGame() {
		Player player = game.getPlayer();
		if(scene.isInBuilding()) {
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
	
	public Location getOriginalNPCBuildingLocation(NPC npc) {
		if(npc.getBuildingId() > 0) {
			return databaseController.getOriginalNPCBuildingLocation(npc);
		}
		return null;
	}
	
	public Location getOriginalItemBuildingLocation(Item item) {
		if(item.getBuildingId() > 0) {
			return databaseController.getOriginalItemBuildingLocation(item);
		}
		return null;
	}
	
	public void saveNPCDialog(NPC npc) {
		databaseController.saveNPCDialog(npc);
	}
	
	public void addItemToPlayer(Item item) {
		databaseController.addItemToPlayer(item, game.getPlayer());
	}
	
	public void setItemLocation(Item item, Location location) {
		databaseController.setItemLocation(item, location);
	}
	
	public ArrayList<NPC> getAllNPCs() {
		return databaseController.getAllNPCs(game.getId());
	}
	
	public ArrayList<Item> getAllItems() {
		return databaseController.getAllItems(game.getId());
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
	
	public Background getBackground() {
		return fileIO.getBackground();
	}
	
	public void setMovingDirection(Direction dir) {
		game.setPlayerMovingDirection(dir);
	}
	
	public Direction getMovingDirection() {
		return game.getPlayerMovingDirection();
	}
	
	public BackgroundLocation getBackgroundLocation() {
		return game.getBackgroundLocation();
	}
	
	public Location getPlayerLocation() {
		return game.getPlayerLocation();
	}
	
	public void setPlayer(Player player) {
		game.setPlayer(player);
		movementController.setPlayer(player);
	}
	
	public ArrayList<Image> getBuildingViewImages() {
		return game.getBuildingViewImages();
	}

	public void setGame(Game game) {
		this.game = game; 
		game.setMainController(this);
	}
	
}
