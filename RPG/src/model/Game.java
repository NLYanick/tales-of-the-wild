package model;

import java.awt.Rectangle;
import java.util.ArrayList;

import controller.MainController;

public class Game {

	public static final int BACKGROUND_PLAYER_DIFFERENCE = 350;
	
	private int id;
	private boolean gameIsPaused;
	
	private MainController controller;
	private Location backgroundLocation;
	
	private Building currentBuilding;
	private NPC dialogNPC;
	
	private Player player;
	private ArrayList<NPC> npcs;
	private ArrayList<Item> worldItems;
	private ArrayList<Building> buildings;
	private ArrayList<Tile> buildingViewImages;
	
	public Game(int id) {
		this.id = id;
		
		backgroundLocation = new Location(-Player.DEFAULT_LOCATION.getX() + BACKGROUND_PLAYER_DIFFERENCE, 
				-Player.DEFAULT_LOCATION.getY() + BACKGROUND_PLAYER_DIFFERENCE);
	}
	
	public void startGame() {
		setUpItems();
		setUpNPCs();
		setUpBuildings();
		
		controller.addPlayerItemViewsToInventoryView();
	}
	
	public void pauzeGame() {
		gameIsPaused = true;
		for(NPC npc : npcs) {
			npc.pauzeThread();
		}
		if(currentBuilding != null) {
			for(NPC npc : currentBuilding.getNPCs()) {
				npc.pauzeThread();
			}
		}
	}
	
	public void resumeGame() {
		gameIsPaused = false;
		for(NPC npc : npcs) {
			if(!npc.isInDialog()) {
				npc.resumeThread();
			}
		}
		if(currentBuilding != null) {
			for(NPC npc : currentBuilding.getNPCs()) {
				if(!npc.isInDialog()) {
					npc.resumeThread();
				}
			}
		}
	}
	
	public void stopNPCThreads() {
		if(npcs != null && npcs.size() > 0) {			
			for(NPC npc : npcs) {
				npc.setThreadRunning(false);
			}
		}
		if(currentBuilding != null) {
			for(NPC npc : currentBuilding.getNPCs()) {
				controller.moveNPCViewWithScreen(npc);
				npc.setThreadRunning(false);
			}
		}
	}
		
	public void resizeLocationsInView() {
		if(npcs != null) {
			for(NPC npc : npcs) {
				controller.moveNPCViewWithScreen(npc);
			}
		}
		if(worldItems != null) {
			for(Item item : worldItems) {
				controller.moveItemViewWithScreen(item);
			}
		}	
		if(buildings != null && currentBuilding != null) {	
			controller.moveBuildingViewWithScreen(currentBuilding);
			for(NPC npc : currentBuilding.getNPCs()) {
				controller.moveNPCViewWithScreen(npc);
			}
			for(Item item : currentBuilding.getItems()) {
				controller.moveItemViewWithScreen(item);
			}
		}	
	}
	
	public void moveBackgroundAndPlayer(Direction dir) {
		Direction oppositeDir = Direction.getOpposite(dir);
		
		if(canWalk(dir)) {
			moveBackground(dir);
			moveRest(dir, oppositeDir);
		}
		else if(inBuilding(oppositeDir)) {
			moveBuilding(dir);
			moveRest(dir, oppositeDir);
		}
		else if(getNearbyBuilding(oppositeDir) != null) {
			enterBuilding(oppositeDir);
		}
	}
	
	private boolean inBuilding(Direction oppositeDir) {
		return player.inBuilding() && !currentBuilding.collidesWith(player, oppositeDir) && !nextStepForPlayerisNPC();
	}
	
	private void moveRest(Direction dir, Direction oppositeDir) {
		moveNPCs(dir);
		moveItems(dir);
		moveBuildingContent(dir);
		
		player.move(oppositeDir);
	}
	
	private void moveBackground(Direction dir) {
		backgroundLocation.move(dir);
		controller.moveBackground(getBackgroundX(), getBackgroundY());
		controller.updateLayersPositions();
	}
	
	private void moveBuilding(Direction dir) {
		currentBuilding.moveViewLocation(dir);
		controller.moveBuildingView(dir);
	}
	
	private boolean canWalk(Direction dir) {
		return playerCanWalk(dir) && !gameIsPaused && !nextStepForPlayerisNPC() && !collidesWithBuilding(player.getLocation(), Direction.getOpposite(dir));
	}
	
	private boolean collidesWithBuilding(Location playerLocation, Direction dir) {
		int nextX = playerLocation.getX() + dir.getX();
		int nextY = playerLocation.getY() + dir.getY();
		
		for (Building building : buildings) {
			if(building.collidesWithOutside(new Location(nextX, nextY))) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean nextStepForPlayerisNPC() {
		Direction movingDirection = Direction.getOpposite(player.getMovingDirection());
		if(currentBuilding != null) {			
			for(NPC npc : currentBuilding.getNPCs()) {
				if(npc != null && player.nextStepIsNPC(npc.getBounds(), movingDirection)) {
					return true;
				}
			}
		}
		for(NPC npc : npcs) {
			if(npc != null && player.nextStepIsNPC(npc.getBounds(), movingDirection)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean playerCanWalk(Direction dir) {
		ArrayList<Tile> backgroundImages = controller.getImagesInFile();
		
		for(Tile img : backgroundImages) {
			if(nextStepIsOnImage(img, dir) && img.canWalkOn()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean nextStepIsOnImage(Tile img, Direction dir) {
		int restOfImgSize = 127;
		Direction opposite = Direction.getOpposite(dir);
		int nextStepX = player.getX() + opposite.getX();
		int nextStepY = player.getY() + opposite.getY();
		
		return Location.isGreater(new Location(nextStepX, nextStepY), new Location(img.getX(), img.getY())) && 
				Location.isLess(new Location(nextStepX, nextStepY), 
						new Location(img.getX() + restOfImgSize, img.getY() + restOfImgSize));
	}
	
	public void teleportPlayer(Location location) {
		player.setLocation(Location.createNew(location));
		
		controller.teleportImages();
	}
	
	public void setBackgroundLocation() {
		setBackgroundX(-getPlayerX() + BACKGROUND_PLAYER_DIFFERENCE);
		setBackgroundY(-getPlayerY() + BACKGROUND_PLAYER_DIFFERENCE);
		controller.moveBackground(getBackgroundX(), getBackgroundY());
	}
	
	public void resumeNearbyNPCThread() {
		if(dialogNPC != null) {
			dialogNPC.setIsInDialog(false);
			dialogNPC.resumeThread();
		}
	}
	
	public void playerInteract() {
		NPC nearbyNPC;
		Item item;
		
		if(currentBuilding != null) {
			item = currentBuilding.getNearbyItem(player);
		 	nearbyNPC = currentBuilding.getNearbyNPC(player.getMovingDirection());
		} else {
			item = getNearbyItem();
		 	nearbyNPC = getNearbyNPC(player.getMovingDirection());
		}
		
		if(nearbyNPC != null && nearbyNPC.hasDialog()) {	
			controller.setAllKeyPressesFalse();
			player.talkToNPC(nearbyNPC);
			dialogNPC = nearbyNPC;
		} else if(item != null && !player.inventoryIsFull()) {
			addItemToPlayerInventory(item);
			controller.addItemViewToInventoryView(item);
			controller.addSingleItemDialogView(item);
		} else if(player.inventoryIsFull()) {
			controller.addSingleDialogView("Your Inventory is full");
		}
	}
	
	public void addItemToPlayerInventory(Item item) {
		player.addItem(item);
		controller.addItemToPlayer(item);
		
		if(currentBuilding != null && currentBuilding.containsItem(item)) { 
			currentBuilding.removeItem(item);
			controller.removeItemFromBuilding(item);
		}
		worldItems.remove(item);
	}
	
	// ---------- NPCs ----------
	
	private NPC getNearbyNPC(Direction direction) {
		for(NPC npc : npcs) {
			if(hasNPCNearby(npc, direction)) {
				return npc;
			}
		}
		return null;
	}
	
	public boolean hasNPCNearby(NPC npc, Direction movingDirection) {
		Direction direction = Direction.getOpposite(movingDirection);
		double divider = 6 * (npc.isShopSeller() ? 0.15 : 0.5); // The Direction gives 6, not 1
		
	    double centerX = player.getX() + Entity.ENTITY_WIDTH / 2;
	    double centerY = player.getY() + Entity.ENTITY_HEIGHT / 2 + Entity.ENTITY_HEIGHT / 8;

	    double reachDistance = 24;
	    
	    double sensorX = centerX + (direction.getX() * reachDistance / divider);
	    double sensorY = centerY + (direction.getY() * reachDistance / divider);
	    
	    Rectangle npcBounds = npc.getBounds();
	    Rectangle checkedBounds = new Rectangle(npc.getX(), (int) (npc.getY() + npcBounds.getHeight() / 8), 
	    		(int) npcBounds.getWidth(), (int) (npcBounds.getHeight() * 0.75));
	    
	    return checkedBounds.contains(sensorX, sensorY); 
	}

	private void moveNPCs(Direction dir) {
		for(NPC npc : npcs) {
			npc.moveViewLocation(dir);
			controller.moveNPCViewWithScreen(npc);			
		}
	}
	
	public void resetDialogNPC() {
		dialogNPC = null;
	}
	
	
	// ---------- Items ----------
	
	private Item getNearbyItem() {
		for(Item item : worldItems) {
			if(player.isOnItem(item)) {
				return item;
			}
		}
		return null;
	}
	
	private void moveItems(Direction dir) {
		for(Item item : worldItems) {
			item.moveViewLocation(dir);
			controller.moveItemViewWithScreen(item);
		}
	}
	
	public void addWorldItem(Item item) {
		worldItems.add(item);
	}
	

	// ---------- Buildings ----------
	
	private Building getNearbyBuilding(Direction dir) {
		int nextX = player.getX() + dir.getX();
		int nextY = player.getY() + dir.getY();
		for (Building building : buildings) {
			if(building.canPass() && building.getEntranceLocation() != null 
					&& building.isBetweenEntranceWalls(new Location(nextX, nextY))) {
				return building;
			}
		}
		return null;
	}
	
	private void enterBuilding(Direction dir) {
		currentBuilding = getNearbyBuilding(dir);
		currentBuilding.enter(player);
	}
	
	public void clearBuildingViewImages() {
		buildingViewImages.clear();
	}

	public void addBuildingViewImage(Tile image) {
		buildingViewImages.add(image);
	}
	
	public void removeCurrentBuilding() {
		currentBuilding = null;
	}
	
	private void moveBuildingContent(Direction dir) {
		if(currentBuilding != null) {
			for(Item item : currentBuilding.getItems()) {
				item.moveViewLocation(dir);
				controller.moveItemViewWithScreen(item);
			}
			for(NPC npc : currentBuilding.getNPCs()) {
				npc.moveViewLocation(dir);
				controller.moveNPCViewWithScreen(npc);
			}
		}
	}
	
	// ---------- Set up ----------
	
	public void setUpNPCs() {
		npcs = controller.getAllNPCs();
		
		int bgX = backgroundLocation.getX();
		int bgY = backgroundLocation.getY();
		
		for(NPC npc : npcs) {
			setUpNPC(npc, bgX, bgY);
		}
	}
	
	private void setUpNPC(NPC npc, int bgX, int bgY) {
		npc.setGame(this);
		
		controller.addNPCView(npc, bgX, bgY);
		if(npc.getMovingDirection() != null) {
			npc.setUpThread();
		}
		
		addItemsToNPC(npc);
	}
	
	private void addItemsToNPC(NPC npc) {
		for(Item item : controller.getNPCItems(npc)) {
			npc.addItem(item);
			controller.addItemView(item);
		}
	}
	
	public void setUpItems() {
		worldItems = controller.getAllWorldItems();
		ArrayList<Item> playerItems = player.getItems();
		
		for(Item item : worldItems) {
			controller.addItemViewToScene(item);
		}
		for(Item item : playerItems) {
			controller.addItemView(item);
		}
	}
	
	public void setUpBuildings() {
		buildings = controller.getAllBuildings();
		buildingViewImages = new ArrayList<Tile>();
		
		for (Building building : buildings) {
			building.setGame(this);
			addNPCsToBuilding(building);
			addItemsToBuilding(building);
		}
	}
	
	private void addNPCsToBuilding(Building building) {
		for (NPC npc : controller.getBuildingNPCs(building.getId())) {
			setUpNPC(npc, building.getX(), building.getY());
			building.addNPC(npc);
			npc.setLocation(Building.UNLOAD_LOCATION);
		}
	}
	
	private void addItemsToBuilding(Building building) {
		for (Item item : controller.getBuildingItems(building.getId())) {
			item.setLocation(Building.UNLOAD_LOCATION);
			building.addItem(item);
			controller.addItemViewToScene(item);
		}
	}
	
	// ---------- Pass methods ----------
	
	public void setBackgroundX(int x) {
		backgroundLocation.setX(x);
	}
	
	public int getBackgroundX() {
		return backgroundLocation.getX();
	}
	
	public void setBackgroundY(int y) {
		backgroundLocation.setY(y);
	}
	
	public int getBackgroundY() {
		return backgroundLocation.getY();
	}
	
	public Location getCurrentBuildingLeaveLocation() {
		return currentBuilding.getLeaveLocation();
	}
	
	public ArrayList<Item> getPlayerItems() {
		return player.getItems();
	}
	
	public int getPlayerX() {
		return player.getX();
	}
	
	public int getPlayerY() {
		return player.getY();
	}
	
	public Location getPlayerLocation() {
		return player.getLocation();
	}
	
	public boolean playerInventoryIsFull() {
		return player.inventoryIsFull();
	}
	
	public void setStandingStillAnimation(Direction dir) {
		player.setStandingStillAnimation(dir);
	}
	
	public void setRunningImage(Direction dir) {
		player.setRunningImage(dir);
	}
	
	public String getPlayerURL() {
		return player.getURL();
	}
	
	public Direction getPlayerMovingDirection() {
		return player.getMovingDirection();
	}
	
	public void setPlayerMovingDirection(Direction dir) {
		player.setMovingDirection(dir);
	}
	
	public Item getPlayerInventoryItemWithId(int id) {
		return player.getInventoryItemWithId(id);
	}
	
	public void dropItemFromPlayerInventory(Item item) {
		player.removeItem(item);
	}
	
	public void addDialogView(NPC npc) {
		controller.addDialogView(npc);
	}
	
	public void moveNPCViewWithScreen(NPC npc) {
		controller.moveNPCViewWithScreen(npc);
	}
	
	public void switchNPCImage(NPC npc, String url) {
		controller.switchNPCImage(npc, url);
	}
	
	public void setBuildingView(Building building) {
		controller.setBuildingView(building);
	}
	
	public void removeBuildingView() {
		controller.removeBuildingView();
	}
	
	public Location getOriginalNPCBuildingLocation(NPC npc, int buildingId) {
		return controller.getOriginalNPCBuildingLocation(npc, buildingId);
	}
	
	public Location getOriginalItemBuildingLocation(Item item, int buildingId) {
		return controller.getOriginalItemBuildingLocation(item, buildingId);
	}
	
	// ---------- Getters & Setters ----------
	
	public int getId() {
		return id;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public ArrayList<NPC> getNPCs() {
		return npcs;
	}
	
	public ArrayList<Item> getWorldItems() {
		return worldItems;
	}
	
	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	
	public Location getBackgroundLocation() {
		return backgroundLocation;
	}

	public void setMainController(MainController controller) {
		this.controller = controller;
	}

	public Building getCurrentBuilding() {
		return currentBuilding;
	}

	public void setCurrentBuilding(Building currentBuilding) {
		this.currentBuilding = currentBuilding;
	}
	
	public NPC getDialogNPC() {
		return dialogNPC;
	}
	
}
