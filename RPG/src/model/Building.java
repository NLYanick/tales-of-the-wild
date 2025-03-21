package model;

import controller.MainController;

public class Building {

	public final static Location BUILDING_LOCATION = new Location(-2000, -1000);
	
	private Location location, leaveLocation, startLocation;
	
	private BuildingType type;
	private Direction exit;
	private boolean canPass;
	private Size size;
	
	private MainController controller;
	
	public Building(Location location, boolean canPass, Size size, BuildingType type,
			Direction exit, Location leaveLocation, MainController controller) {
		this.location = location;
		this.leaveLocation = leaveLocation;
		startLocation = location;
		
		this.type = type;
		this.exit = exit;
		this.canPass = canPass;
		this.size = size;
		
		this.controller = controller;
	}
	
	public void enter(Player player) {
		if(canPass) {
			controller.setBuildingView(this);
			player.setLocation(BUILDING_LOCATION);
			player.setInBuilding(true);
			location = null;
		}
	}
	
	public void leave(Player player) {
		if(canPass) {
			controller.removeBuildingView(this);
			player.setLocation(leaveLocation);
			player.setInBuilding(false);
			location = startLocation;
		}
	}
	
	public void setCanPass(boolean canPass) {
		this.canPass = canPass;
	}
	
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
		return location.getX();
	}
	
	public int getY() {
		return location.getY();
	}
	
	public BuildingType getType() {
		return type;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Location getLeaveLocation() {
		return leaveLocation;
	}
	
	public Direction getExit() {
		return exit;
	}
	
}
