package model;

import java.util.ArrayList;

import controller.MainController;

public class Building {

	public final static Location UNLOAD_LOCATION = new Location(-7000, -7000);
	
	private Location insideLocation, leaveLocation, entranceLocation, viewLocation;
	
	private BuildingType type;
	private Direction exit;
	private boolean canPass;
	private int id;
	private Size size;
	
	private ArrayList<NPC> npcs;
	private ArrayList<Item> items;
	
	private MainController controller;
	
	public Building(Location insideLocation, boolean canPass, Size size, BuildingType type, Direction exit,
			Location leaveLocation, MainController controller, int id, Location entranceLocation) {
		this.insideLocation = insideLocation;
		this.leaveLocation = leaveLocation;
		this.entranceLocation= entranceLocation; 
		
		this.type = type;
		this.exit = exit;
		this.canPass = canPass;
		this.size = size;
		this.id = id;
		
		this.controller = controller;
		npcs = new ArrayList<NPC>();
		items = new ArrayList<Item>();
	}
	
	public void enter(Player player) {
		if(canPass) {
			controller.setBuildingView(this);
			
			Location playerInsideLocation = getInsideLocation(true);
						
			loadInside();
			
			player.setInBuilding(true);
			controller.teleportPlayer(playerInsideLocation);
		}
	}
	
	public void leave(Player player) {
		if(canPass) {
			controller.removeBuildingView();
			
			controller.teleportPlayer(leaveLocation);
			player.setInBuilding(false);
			
			unloadInside();
		}
	}
	
	private Location getInsideLocation(boolean spawning) {
		int entranceSpacing = spawning ? 64 : 0;
		switch(exit) {
			case NORTH:
				return new Location(insideLocation.getX() + size.getWidth() / 2, insideLocation.getY() + entranceSpacing);
			case EAST:
				return new Location(insideLocation.getX() + size.getWidth() - entranceSpacing, insideLocation.getY() + size.getHeight() / 2);
			case SOUTH:
				return new Location(insideLocation.getX() + size.getWidth() / 2, insideLocation.getY() + size.getHeight() - entranceSpacing);
			case WEST:
				return new Location(insideLocation.getX() + entranceSpacing, insideLocation.getY() + size.getHeight() / 2);
			default: return new Location(insideLocation.getX(), insideLocation.getY());
		}
	}
	
	public void moveViewLocation(Direction dir) {
		viewLocation.setX(viewLocation.getX() + dir.getX());
		viewLocation.setY(viewLocation.getY() + dir.getY());
	}
	
	public boolean collidesWith(Player player, Direction dir) {
		
		int multiplier = 2;
		Location playerLocation = player.getLocation();
		int nextX = playerLocation.getX() + dir.getX() * multiplier;
		int nextY = playerLocation.getY() + dir.getY() * multiplier;
		
		if(nextStepIsInBuilding(new Location(nextX, nextY))) {
			return false;
		} else if(dir == exit && nextStepIsOnExit(nextX, nextY)) {
			leave(player);
			return true;
		}
		
		return true;
	}
	
	private boolean nextStepIsInBuilding(Location nextLocation) {
		int wallSize = 128;
		
		switch(exit) {
			case NORTH: 
				return Location.isGreater(nextLocation, new Location(insideLocation.getX() + wallSize, insideLocation.getY())) 
						&& Location.isLess(nextLocation, 
							new Location(insideLocation.getX() + size.getWidth() - wallSize, insideLocation.getY() + size.getHeight() - wallSize))
						&& !nextStepIsOnExitSideWall(nextLocation, wallSize); 
			case EAST: 
				return Location.isGreater(nextLocation, new Location(insideLocation.getX() + wallSize, insideLocation.getY() + wallSize)) 
						&& Location.isLess(nextLocation, 
							new Location(insideLocation.getX() + size.getWidth(), insideLocation.getY() + size.getHeight() - wallSize))
						&& !nextStepIsOnExitSideWall(nextLocation, wallSize);
			case SOUTH: 
				return Location.isGreater(nextLocation, new Location(insideLocation.getX() + wallSize, insideLocation.getY() + wallSize)) 
						&& Location.isLess(nextLocation, 
							new Location(insideLocation.getX() + size.getWidth() - wallSize, insideLocation.getY() + size.getHeight()))
						&& !nextStepIsOnExitSideWall(nextLocation, wallSize);
			case WEST: 
				return Location.isGreater(nextLocation, new Location(insideLocation.getX(), insideLocation.getY() + wallSize)) 
						&& Location.isLess(nextLocation, 
							new Location(insideLocation.getX() + size.getWidth() - wallSize, insideLocation.getY() + size.getHeight() - wallSize))
						&& !nextStepIsOnExitSideWall(nextLocation, wallSize);
			default: return false;
		}
	}
	
	private boolean nextStepIsOnExit(int nextX, int nextY) {
		
		switch(exit) {
			case NORTH: 
				return nextY <= insideLocation.getY();
			case EAST: 
				return nextX >= insideLocation.getX() + size.getWidth();
			case SOUTH: 
				return nextY >= insideLocation.getY() + size.getHeight();
			case WEST: 
				return nextX <= insideLocation.getX();
			default: return false;
		}
	}
	
	private boolean nextStepIsOnExitSideWall(Location nextLocation, int wallSize) {
		Location playerInLoc = getInsideLocation(false);
		switch(exit) {
			case NORTH:
			case SOUTH:
				return nextLocation.getY() > playerInLoc.getY() - wallSize 
						&& nextLocation.getY() < playerInLoc.getY() + wallSize
						&& (nextLocation.getX() > playerInLoc.getX() + (widthIsEven() ? wallSize : wallSize/2) 
								|| nextLocation.getX() < playerInLoc.getX() - (widthIsEven() ? wallSize : wallSize/2));
			case EAST:
			case WEST:
				return nextLocation.getX() > playerInLoc.getX() - wallSize
						&& nextLocation.getX() < playerInLoc.getX() + wallSize
						&& (nextLocation.getY() > playerInLoc.getY() + (heightIsEven() ? wallSize : wallSize/2)
								|| nextLocation.getY() < playerInLoc.getY() - (heightIsEven() ? wallSize : wallSize/2));
			default: return true;
		}
	}
	
	private boolean widthIsEven() {
		return ((size.getWidth() / 128) % 2 == 0);
	}
	
	private boolean heightIsEven() {
		return ((size.getHeight() / 128) % 2 == 0);
	}
	
	private void loadInside() {
		for(NPC npc : npcs) {
			Location loc = controller.getOriginalNPCBuildingLocation(npc);
			npc.setLocation(loc);
		}
		for(Item item : items) {
			Location loc = controller.getOriginalItemBuildingLocation(item);
			item.setLocation(loc);
		}
	}
	
	private void unloadInside() {
		for(NPC npc : npcs) {
			npc.setLocation(UNLOAD_LOCATION);
		}
		for(Item item : items) {
			item.setLocation(UNLOAD_LOCATION);
		}
	}
	
	public void addNPC(NPC npc) {
		npcs.add(npc);
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	
	/* Getters and Setters */
	
	public boolean canPass() {
		return canPass;
	}
	
	public int getWidth() {
		return size.getWidth();
	}
	
	public int getHeight() {
		return size.getHeight();
	}
	
	public int getEntranceX() {
		return entranceLocation.getX();
	}
	
	public int getEntranceY() {
		return entranceLocation.getY();
	}
	
	public BuildingType getType() {
		return type;
	}
	
	public Location getEntranceLocation() {
		return entranceLocation;
	}
	
	public Location getLeaveLocation() {
		return leaveLocation;
	}
	
	public Direction getExit() {
		return exit;
	}
	
	public void setViewLocation(Location location) {
		viewLocation = location;
	}
	
	public Location getViewLocation() {
		return viewLocation;
	}
	
	public int getId() {
		return id;
	}
	
}
