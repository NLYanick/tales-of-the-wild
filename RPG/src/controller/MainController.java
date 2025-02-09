package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import model.BackgroundLocation;
import model.Direction;
import model.Image;
import model.Item;
import model.Location;
import model.NPC;
import model.Player;
import view.Background;
import view.ItemView;
import view.MainScene;
import view.NPCView;

public class MainController {

	public static final int BACKGROUND_PLAYER_DIFFERENCE = 350 ;
	
	private BackgroundLocation backgroundLocation;
	private Player player;
	
	private ApplicationController appController;
	private MovementController movementController;
	private DatabaseController databaseController;
	
	private MainScene scene;
	private FileIO fileIO;
	
	private ArrayList<NPC> npcs;
	private HashMap<NPC, NPCView> npcsWithViews;
	private ArrayList<Item> items;
	private HashMap<Item, ItemView> itemsWithViews;
	
	private boolean gameIsPaused;
	
	public MainController(ApplicationController appController, FileIO fileIO) {
		
		this.fileIO = fileIO;
		scene = new MainScene(this);
		
		backgroundLocation = new BackgroundLocation(-Player.DEFAULT_LOCATION.getX() + BACKGROUND_PLAYER_DIFFERENCE, -Player.DEFAULT_LOCATION.getY() + BACKGROUND_PLAYER_DIFFERENCE);
		
		this.appController = appController;
		movementController = new MovementController(this);
		databaseController = new DatabaseController(this);
		
	}
	
	public void setUpNPCs() {
		npcs = databaseController.getAllNPCs();
		npcsWithViews = new HashMap<NPC, NPCView>();
		int bgX = backgroundLocation.getX();
		int bgY = backgroundLocation.getY();
		
		for(NPC npc : npcs) {
			npc.setMainController(this);
			
			NPCView npcView = new NPCView(npc.getURL(), npc.getStartLocation().getX(), npc.getStartLocation().getY());
			npcView.fixImage();
			npc.setViewLocation(new Location(bgX + (int) npcView.getLayoutX(), bgY + (int) npcView.getLayoutY()));
			
			scene.addNPCView(npcView);
			npcsWithViews.put(npc, npcView);
			npc.setUpThread();
		}
		
	}
	
	public void setUpItems() {
		items = databaseController.getAllItems();
		itemsWithViews = new HashMap<Item, ItemView>();
		
		addItemsToGame();
	}
	
	private void addItemsToGame() {
		for(Item item : items) {
			ItemView itemView = new ItemView(new Location(item.getX(), item.getY()), item.getImageUrl());
			itemsWithViews.put(item, itemView);
			
			scene.addItemView(itemView);
			moveItemViewWithScreen(item);
		}
	}
	
	public void addPlayerItemViewsToInventoryView() {
		ArrayList<ItemView> itemViews = new ArrayList<ItemView>();
		for(Item playerItem : player.getItemsOfInventory()) {
			for(int i = 0; i < items.size(); i++) {
				Item item = items.get(i);
				if(playerItem.getId() == item.getId()) {
					ItemView itemView = itemsWithViews.get(item);
					itemViews.add(itemView);
					items.remove(item);
				}
			}
		}
		
		scene.setItemViewsInInventory(itemViews);
	}
	
	public void moveBackground(Direction dir) {
		if(canWalk(dir)) {
			backgroundLocation.move(dir);
			scene.moveBackground(backgroundLocation.getX(), backgroundLocation.getY(), appController.isFullScreen());
			moveNPCs(dir);
			moveItems(dir);
			
			player.move(Direction.getOpposite(dir));
		}
	}
	
	private boolean canWalk(Direction dir) {
		return playerCanWalk(dir) && !gameIsPaused && !nextStepForPlayerisNPC();
	}
	
	private boolean nextStepForPlayerisNPC() {
		Direction movingDirection = player.getMovingDirection();
		for(NPC npc : npcs) {
			NPCView npcView = npcsWithViews.get(npc);
			if(npc != null && scene.playerViewNextStepIsOnNPCView(npcView, movingDirection)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean npcViewNextStepIsOnPlayerView(NPC npc, Direction dir) {
		return scene.npcViewNextStepIsOnPlayerView(npcsWithViews.get(npc), dir);
	}
	
	public void teleportPlayer(Location location) {
		player.setX(location.getX());
		player.setY(location.getY());
		
		teleportImages();
	}
	
	private void teleportImages() {
		backgroundLocation.setX(-player.getX() + BACKGROUND_PLAYER_DIFFERENCE);
		backgroundLocation.setY(-player.getY() + BACKGROUND_PLAYER_DIFFERENCE);
		scene.moveBackground(backgroundLocation.getX(), backgroundLocation.getY(), appController.isFullScreen());
		
		int bgX = backgroundLocation.getX();
		int bgY = backgroundLocation.getY();
		for(NPC npc : npcs) {
			NPCView npcView = npcsWithViews.get(npc);
			npc.setViewLocation(new Location(bgX + (int) npc.getX(), bgY + (int) npc.getY()));
			npcView.move(npc.getViewLocation().getX(), npc.getViewLocation().getY());
			npcView.fixImage();
			npc.setViewLocation(new Location((int) npcView.getLayoutX(), (int) npcView.getLayoutY()));
			moveNPCViewWithScreen(npc);
		}
		for(Item item : items) {
			ItemView itemView = itemsWithViews.get(item);
			item.setViewLocation(new Location(bgX + (int) item.getX(), bgY + (int) item.getY()));
			itemView.move(item.getViewLocation());
			moveItemViewWithScreen(item);
		}
	}
	
	private boolean playerCanWalk(Direction dir) {
		ArrayList<Image> backgroundImages = fileIO.getImagesInFile();
		
		for(Image img : backgroundImages) {
			if(nextStepIsOnImage(img, dir) && img.canWalkOn()) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean nextStepIsOnImage(Image img, Direction dir) {
		int extraSpace = 127;
		Direction opposite = Direction.getOpposite(dir);
		int nextStepX = player.getX() + opposite.getX();
		int nextStepY = player.getY() + opposite.getY();
		
		return (nextStepX >= img.getX()
				&& nextStepY >= img.getY())
				&& (nextStepX <= img.getX() + extraSpace
				&& nextStepY <= img.getY() + extraSpace);
	}
	
	public void setPlayerStandingStillAnimation(Direction dir) {
		player.setStandingStillAnimation(dir);
		scene.changePlayerImage();
	}
	
	public void setPlayerImage(Direction dir) {
		player.setRunningImage(dir);
		scene.changePlayerImage();
	}
	
	public void stopNPCThreads() {
		if(npcs != null && !(npcs.size() <= 0)) {			
			for(NPC npc : npcs) {
				npc.setThreadRunning(false);
			}
		}
	}
	
	public void resumeNPCThreads() {
		if(npcs != null && !(npcs.size() <= 0)) {			
			for(NPC npc : npcs) {
				npc.resumeThread();
			}
		}
	}
	
	private void moveNPCs(Direction dir) {
		for(NPC npc : npcs) {
			npc.moveViewLocationWithBackground(dir);
			moveNPCViewWithScreen(npc);
		}
	}
	
	private void moveItems(Direction dir) {
		for(Item item : items) {
			item.moveViewLocation(dir);
			moveItemViewWithScreen(item);
		}
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
		scene.moveBackground(backgroundLocation.getX(), backgroundLocation.getY(), appController.isFullScreen());
		scene.resizePlayerViewLocation();
		if(npcs != null)
			resizeNPCViewLocation();
		if(items != null)
			resizeItemViewLocation();
	}
	
	private void resizeNPCViewLocation() {
		for(NPC npc : npcs) {
			moveNPCViewWithScreen(npc);
		}
	}
	
	private void resizeItemViewLocation() {
		for(Item item : items) {
			moveItemViewWithScreen(item);
		}
	}
	
	@SuppressWarnings("static-access")
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
	
	@SuppressWarnings("static-access")
	public void moveItemViewWithScreen(Item item) {
		int screenXDiffernce = (int) scene.getWidth()/2 - scene.SCENEWIDTH/2;
		int screenYDiffernce = (int) scene.getHeight()/2 - scene.SCENEHEIGHT/2;
		
		ItemView itemView = itemsWithViews.get(item);
		if(appController.isFullScreen()) {
			itemView.move(new Location(item.getViewLocation().getX() + screenXDiffernce, item.getViewLocation().getY() + screenYDiffernce));
		} else {
			itemView.move(new Location(item.getViewLocation().getX(), item.getViewLocation().getY()));
		}
	}
	
	public void pauzeGame() {
		gameIsPaused = true;
		for(NPC npc : npcs) {
			npc.pauzeThread();
		}
	}
	
	public void resumeGame() {
		gameIsPaused = false;
		for(NPC npc : npcs) {
			if(!npc.isInDialog()) {
				npc.resumeThread();
			}
		}
	}
	
	public void resumeNearbyNPCThread() {
		NPC nearbyNPC = getNearbyNPC(player.getMovingDirection());

		if(nearbyNPC != null) {
			nearbyNPC.setIsInDialog(false);
			nearbyNPC.resumeThread();
		}
	}
	
	public void playerInteract() {
		NPC nearbyNPC = getNearbyNPC(player.getMovingDirection());
		Item item = getNearbyItem();
		
		if(nearbyNPC != null) {	
			scene.setAllKeyPressesFalse();
			player.talkToNPC(nearbyNPC);
		} else if(item != null) {
			scene.addItemViewToInventoryView(item, itemsWithViews.get(item), player.inventoryIsFull());
		}
	}
	
	public void addItemToPlayerInventory(Item item) {
		player.addItemToInventory(item);
		item.resetLocation();
		databaseController.setItemLocation(item, new Location(item.getX(), item.getY()));
	}
	
	public void dropItem(Item item) {
		// TODO
		databaseController.setItemLocation(item, player.getLocation());
	}
	
	private Item getNearbyItem() {
		for(Item item : items) {
			if(playerIsOnItem(item)) {
				return item;
			}
		}
		return null;
	}
	
	private boolean playerIsOnItem(Item item) {
		int extraSpace = 20;
		return Location.isGreater(player.getLocation(), new Location(item.getX() - extraSpace, item.getY() - extraSpace)) 
				&& Location.isLess(player.getLocation(), new Location(item.getX() + extraSpace * 3, item.getY() + extraSpace * 3));
	}

	private NPC getNearbyNPC(Direction direction) {
		for(NPC npc : npcs) {
			if(hasNPCNearby(npc, direction)) {
				return npc;
			}
		}
		return null;
	}

	private boolean hasNPCNearby(NPC npc, Direction movingDirection) {
		
		Direction direction = getGoodNPCCheckDirection(movingDirection);
		Direction nextDirection = Direction.getNext(direction);

		if(Direction.isHorizontal(movingDirection)) {
			return hasNPCNearbyHorizontal(npc, direction, nextDirection);
		} else if(Direction.isVertical(movingDirection)) {
			return hasNPCNearbyVertical(npc, direction, nextDirection);
		}
		
		return false;
	}
	
	private Direction getGoodNPCCheckDirection(Direction dir) {
		if((dir.equals(Direction.NORTH) || dir.equals(Direction.WEST))) {
			dir = Direction.getOpposite(dir);
		}
		return dir;
	}
	
	private boolean hasNPCNearbyHorizontal(NPC npc, Direction direction, Direction nextDirection) {
		
		int multiplier = 12;
		boolean hasNearby = false;
		Direction movingDirection = player.getMovingDirection();
		
		if(movingDirection == Direction.EAST) {
			if((player.getX() >= npc.getX()) && (player.getX() <= npc.getX() + direction.getX() * multiplier)
				&& playerYIsNearNPCY(npc, nextDirection, multiplier)) {
				hasNearby = true;
			}
		} else if(movingDirection == Direction.WEST) {
			if((player.getX() >= npc.getX() - direction.getX() * multiplier) && (player.getX() <= npc.getX())
				&& playerYIsNearNPCY(npc, nextDirection, multiplier)) {
				hasNearby = true;
			}
		}
		return hasNearby;
	}
	
	private boolean hasNPCNearbyVertical(NPC npc, Direction direction, Direction nextDirection) {
		
		int multiplier = 12;
		boolean hasNearby = false;
		Direction movingDirection = player.getMovingDirection();
		
		if(movingDirection == Direction.NORTH) {
			if(((player.getY() >= npc.getY() - direction.getY() * (multiplier * 1.5)) && (player.getY() <= npc.getY()))
					&& playerXIsNearNPCX(npc, nextDirection, multiplier/2)) {
				hasNearby = true;
			}
		} else if(movingDirection == Direction.SOUTH) {
			if((player.getY() >= npc.getY()) && (player.getY() <= npc.getY() + direction.getY() * multiplier)
				&& playerXIsNearNPCX(npc, nextDirection, multiplier/2)) {
				hasNearby = true;
			}
		}
		return hasNearby;
	}
	
	private boolean playerXIsNearNPCX(NPC npc, Direction nextDirection, int multiplier) {
		return (player.getX() >= npc.getX() + nextDirection.getX() * multiplier) 
		&& (player.getX() <= npc.getX() - nextDirection.getX() * multiplier);
	}
	
	private boolean playerYIsNearNPCY(NPC npc, Direction nextDirection, int multiplier) {
		return (player.getY() >= npc.getY() - nextDirection.getY() * multiplier) 
				&& (player.getY() <= npc.getY() + nextDirection.getY() * multiplier);
	}
	
	public void addDialogView(List<String> dialog) {
		dialog = reverseSort(new ArrayList<>(dialog));
		for(String text : dialog) {
			scene.addDialogView(text);
		}
	}
	
	private <T> ArrayList<T> reverseSort(ArrayList<T> list) {
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
	
	public void saveGame() {
		databaseController.saveGame(player);
	}
	
	public void loadPlayer(Player player) {
		if(player == null) {
			throw new NullPointerException();
		}
		databaseController.loadPlayer(player);
	}
	
	public Player createPlayer(String name) {
		return databaseController.createPlayer(name);
	}
	
	public boolean nameIsUnique(String name) {
		return databaseController.nameIsUnique(name);
	}
	
	public void deletePlayer(Player player) {
		databaseController.deletePlayer(player);
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
		return player;
	}
	
	public String getPlayerURL() {
		return player.getURL();
	}
	
	public Background getBackground() {
		return fileIO.getBackground();
	}
	
	public FileIO getFileIO() {
		return fileIO;
	}
	
	public void setMovingDirection(Direction dir) {
		player.setMovingDirection(dir);
	}
	
	public Direction getMovingDirection() {
		return player.getMovingDirection();
	}
	
	public BackgroundLocation getBackgroundLocation() {
		return backgroundLocation;
	}
	
	public Location getPlayerLocation() {
		return player.getLocation();
	}
	
	public ArrayList<Player> getAllPlayers() {
		return databaseController.getAllPlayers();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		movementController.setPlayer(player);
	}
	
}
