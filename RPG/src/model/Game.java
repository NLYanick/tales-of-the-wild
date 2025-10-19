package model;

import java.util.ArrayList;

import controller.MainController;

public class Game {

	public static final int BACKGROUND_PLAYER_DIFFERENCE = 350;
	
	private int id;
	private boolean gameIsPaused;
	
	private MainController controller;
	private BackgroundLocation backgroundLocation;
	
	private Building currentBuilding;
	private NPC dialogNPC;
	
	private Player player;
	private ArrayList<NPC> npcs;
	private ArrayList<Item> items;
	private ArrayList<Building> buildings;
	private ArrayList<Image> buildingViewImages;
	
	public Game(int id) {
		this.id = id;
		
		backgroundLocation = new BackgroundLocation(-Player.DEFAULT_LOCATION.getX() + BACKGROUND_PLAYER_DIFFERENCE, 
				-Player.DEFAULT_LOCATION.getY() + BACKGROUND_PLAYER_DIFFERENCE);
	}
	
	public void startGame() {
		setUpItems();
		setUpNPCs();
		setUpBuildings();
		
		controller.addPlayerItemViewsToInventoryView(items);
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
	
	public void stopNPCThreads() {
		if(npcs != null && npcs.size() > 0) {			
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
	
	public void resizeLocationsInView() {
		if(npcs != null) {
			for(NPC npc : npcs) {
				controller.moveNPCViewWithScreen(npc);
			}
		}
		if(items != null) {
			for(Item item : items) {
				controller.moveItemViewWithScreen(item);
			}
		}	
		if(buildings != null && currentBuilding != null) {	
			controller.moveBuildingViewWithScreen(currentBuilding);
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
		
		player.move(oppositeDir);
	}
	
	private void moveBackground(Direction dir) {
		backgroundLocation.move(dir);
		controller.moveBackground(getBackgroundX(), getBackgroundY());
	}
	
	private void moveBuilding(Direction dir) {
		currentBuilding.moveViewLocation(dir);
		controller.moveBuildingView(dir);
	}
	
	private boolean canWalk(Direction dir) {
		return playerCanWalk(dir) && !gameIsPaused && !nextStepForPlayerisNPC();
	}
	
	private boolean nextStepForPlayerisNPC() {
		Direction movingDirection = Direction.getOpposite(player.getMovingDirection());
		for(NPC npc : npcs) {
			if(npc != null && player.nextStepIsNPC(npc.getLocation(), movingDirection)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean playerCanWalk(Direction dir) {
		ArrayList<Image> backgroundImages = controller.getImagesInFile();
		
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
		NPC nearbyNPC = getNearbyNPC(player.getMovingDirection());
		Item item;
		if(currentBuilding != null) 
			item = currentBuilding.getNearbyItem(player);
		else
			item = getNearbyItem();
		
		if(nearbyNPC != null) {	
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
	}
	
	// ---------- NPCs ----------
	
	public NPC getNearbyNPC(Direction direction) {
		for(NPC npc : npcs) {
			if(hasNPCNearby(npc, direction)) {
				return npc;
			}
		}
		return null;
	}

	private boolean hasNPCNearby(NPC npc, Direction movingDirection) {
		
		Direction direction = npc.getGoodDirection(movingDirection);
		Direction nextDirection = Direction.getNext(direction);

		if(Direction.isHorizontal(movingDirection)) {
			return hasNPCNearbyHorizontal(npc, direction, nextDirection);
		} else if(Direction.isVertical(movingDirection)) {
			return hasNPCNearbyVertical(npc, direction, nextDirection);
		}
		
		return false;
	}
	
	private boolean hasNPCNearbyHorizontal(NPC npc, Direction direction, Direction nextDirection) {
		
		int multiplier = 12;
		boolean hasNearby = false;
		Direction movingDirection = player.getMovingDirection();
		
		if(movingDirection == Direction.EAST) {
			if((player.getX() >= npc.getX()) && (player.getX() <= npc.getX() + direction.getX() * multiplier)
				&& player.yIsNearNPCY(npc, nextDirection, multiplier)) {
				hasNearby = true;
			}
		} else if(movingDirection == Direction.WEST) {
			if((player.getX() >= npc.getX() - direction.getX() * multiplier) && (player.getX() <= npc.getX())
				&& player.yIsNearNPCY(npc, nextDirection, multiplier)) {
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
					&& player.xIsNearNPCX(npc, nextDirection, multiplier/2)) {
				hasNearby = true;
			}
		} else if(movingDirection == Direction.SOUTH) {
			if((player.getY() >= npc.getY()) && (player.getY() <= npc.getY() + direction.getY() * multiplier)
				&& player.xIsNearNPCX(npc, nextDirection, multiplier/2)) {
				hasNearby = true;
			}
		}
		return hasNearby;
	}
	
	private void moveNPCs(Direction dir) {
		for(NPC npc : npcs) {
			npc.moveViewLocationWithBackground(dir);
			controller.moveNPCViewWithScreen(npc);			
		}
	}
	
	public void resetDialogNPC() {
		dialogNPC = null;
	}
	
	
	// ---------- Items ----------
	
	private Item getNearbyItem() {
		for(Item item : items) {
			if(player.isOnItem(item)) {
				return item;
			}
		}
		return null;
	}
	
	private void moveItems(Direction dir) {
		for(Item item : items) {
			item.moveViewLocation(dir);
			controller.moveItemViewWithScreen(item);
		}
	}
	
	
	// ---------- Buildings ----------
	
	private Building getNearbyBuilding(Direction dir) {
		int nextX = player.getX() + dir.getX();
		int nextY = player.getY() + dir.getY();
		for (Building building : buildings) {
			if(building.canPass() && building.getEntranceLocation() != null 
					&& isBetweenBuildingEntranceWalls(building, new Location(nextX, nextY))) {
				return building;
			}
		}
		return null;
	}
	
	private boolean isBetweenBuildingEntranceWalls(Building building, Location nextLocation) {
		int entranceSize = 64;
		return Location.isGreater(nextLocation, new Location(building.getEntranceX(), building.getEntranceY()))
				&& Location.isLess(nextLocation,
					new Location(building.getEntranceX() + entranceSize, building.getEntranceY() + entranceSize));
	}
	
	private void enterBuilding(Direction dir) {
		Building building = getNearbyBuilding(dir);
		currentBuilding = building;
		building.enter(player);
	}
	
	public void clearBuildingViewImages() {
		buildingViewImages.clear();
	}

	public void addBuildingViewImage(Image image) {
		buildingViewImages.add(image);
	}
	
	public void removeCurrentBuilding() {
		currentBuilding = null;
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
	
	public ArrayList<Item> getItemsOfPlayerInventory() {
		return player.getItemsOfInventory();
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
	
	// ---------- Set up ----------
	
	public void setUpNPCs() {
		npcs = controller.getAllNPCs();
		
		int bgX = backgroundLocation.getX();
		int bgY = backgroundLocation.getY();
		
		for(NPC npc : npcs) {
			npc.setMainController(controller);
			
			controller.addNPCView(npc, bgX, bgY);
			if(npc.getMovingDirection() != null) {
				npc.setUpThread();
			}
			
			addItemsToNPC(npc);
		}
	}
	
	private void addItemsToNPC(NPC npc) {
		for(Item item : controller.getNPCItems(npc)) {
			npc.addItem(item);
			controller.addItemView(item);
		}
	}
	
	public void setUpItems() {
		items = controller.getAllWorldItems();
		
		for(Item item : items) {
			controller.addItemViewToScene(item);
		}
	}
	
	public void setUpBuildings() {
		buildings = controller.getAllBuildings();
		buildingViewImages = new ArrayList<Image>();
		
		for (Building building : buildings) {
			addNPCsToBuilding(building);
			addItemsToBuilding(building);
		}
	}
	
	private void addNPCsToBuilding(Building building) {
		for (NPC npc : controller.getBuildingNPCs(building.getId())) {
			npc.setLocation(Building.UNLOAD_LOCATION);
			building.addNPC(npc);
		}
	}
	
	private void addItemsToBuilding(Building building) {
		for (Item item : controller.getBuildingItems(building.getId())) {
			item.setLocation(Building.UNLOAD_LOCATION);
			building.addItem(item);
			controller.addItemView(item);
		}
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
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	
	public ArrayList<Image> getBuildingViewImages() {
		return buildingViewImages;
	}
	
	public BackgroundLocation getBackgroundLocation() {
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
