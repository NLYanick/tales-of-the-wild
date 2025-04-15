package model;

import java.util.ArrayList;

import controller.MainController;

public class Building {

	public final static int INSIDE_SPAWN_X = -2000;
	public final static int INSIDE_SPAWN_Y = -1000;
	public final static Location NEW_NPC_LOCATION = new Location(-4000, -4000);
	
	private Location insideLocation, leaveLocation, entranceLocation, viewLocation;
	
	private BuildingType type;
	private Direction exit;
	private boolean canPass;
	private int id;
	private Size size;
	
	private ArrayList<NPC> npcs;
	
	private MainController controller;
	
	public Building(Location insideLocation, boolean canPass, Size size, BuildingType type, Direction exit,
			Location leaveLocation, MainController controller, int id, Location entranceLocation) {
		this.insideLocation = insideLocation;
		this.leaveLocation = leaveLocation;
		this.entranceLocation= entranceLocation; 
		
		this.type = type;
		this.exit = exit;
		this.exit = Direction.EAST;
		this.canPass = canPass;
		this.size = size;
		this.id = id;
		
		this.controller = controller;
		npcs = new ArrayList<NPC>();
	}
	
	public void enter(Player player) {
		if(canPass) {
			controller.setBuildingView(this);
			
			Location playerInsideLocation = getInsideLocation();
			Location difference = new Location(player.getX() - playerInsideLocation.getX(), player.getY() - playerInsideLocation.getY());
			
			player.setLocation(playerInsideLocation);
			player.setInBuilding(true);
			
			loadNPCs(difference);
		}
	}
	
	public void leave(Player player) {
		if(canPass) {
			controller.removeBuildingView();
			
			player.setLocation(leaveLocation);
			player.setInBuilding(false);
			
			unloadNPCs();
		}
	}
	
	private Location getInsideLocation() {
		int extra = 40;
		switch(exit) {
			case NORTH:
				return new Location(INSIDE_SPAWN_X, INSIDE_SPAWN_Y - size.getHeight() + extra * 2);
			case EAST:
				return new Location(INSIDE_SPAWN_X + size.getWidth() / 2 - extra, INSIDE_SPAWN_Y - size.getHeight() / 2);
			case SOUTH:
				return new Location(INSIDE_SPAWN_X, INSIDE_SPAWN_Y);
			case WEST:
				return new Location(INSIDE_SPAWN_X - size.getWidth() / 2 + extra, INSIDE_SPAWN_Y - size.getHeight() / 2);
			default: return new Location(INSIDE_SPAWN_X, INSIDE_SPAWN_Y);
		}
	}
	
	public void moveViewLocation(Direction dir) {
		viewLocation.setX(viewLocation.getX() + dir.getX());
		viewLocation.setY(viewLocation.getY() + dir.getY());
	}
	
	public boolean collidesWith(Location playerLocation, Direction dir) {
		
		int multiplier = 2;
		int nextX = playerLocation.getX() + dir.getX() * multiplier;
		int nextY = playerLocation.getY() + dir.getY() * multiplier;
		
		if(nextStepIsInBuilding(nextX, nextY)) {
			return false;
		} else if(dir == exit && nextStepIsOnExit(nextX, nextY)) {
			controller.leaveBuilding();
			return true;
		}
		
		return true;
	}
	
	private boolean nextStepIsInBuilding(int nextX, int nextY) {
		int wallSize = 128;
		Location nextLocation = new Location(nextX, nextY);
		
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
		Location playerInLoc = getInsideLocation();
		int extra = 32;
		switch(exit) {
			case NORTH:
			case SOUTH:
				return nextLocation.getY() > playerInLoc.getY() - wallSize / 2 - extra 
						&& nextLocation.getY() < playerInLoc.getY() + wallSize / 2 + extra
						&& (nextLocation.getX() > playerInLoc.getX() + wallSize || nextLocation.getX() < playerInLoc.getX() - wallSize);
			case EAST:
			case WEST:
				return nextLocation.getX() > playerInLoc.getX() - wallSize / 2 - extra 
						&& nextLocation.getX() < playerInLoc.getX() + wallSize / 2 + extra 
						&& (nextLocation.getY() > playerInLoc.getY() + wallSize + extra
								|| nextLocation.getY() < playerInLoc.getY() - wallSize + extra);
			default: return true;
		}
	}
	
	public void addNPC(NPC npc) {
		npcs.add(npc);
	}
	
	private void loadNPCs(Location difference) {
		for(NPC npc : npcs) {
			npc.setViewLocation(new Location(npc.getViewLocation().getX() + difference.getX(), npc.getViewLocation().getY() + difference.getY()));
		}
	}
	
	private void unloadNPCs() {
		for(NPC npc : npcs) {
			npc.setViewLocation(NEW_NPC_LOCATION);
		}
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
	
	public int getX() {
		return insideLocation.getX();
	}
	
	public int getY() {
		return insideLocation.getY();
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
