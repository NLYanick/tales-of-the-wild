package model;

import java.util.ArrayList;

import controller.MainController;

public class Building {

	public final static Location BUILDING_LOCATION = new Location(-2000, -1000);
	public final static Location NEW_NPC_LOCATION = new Location(-4000, -4000);
	
	private Location location, leaveLocation, startLocation, viewLocation;
	
	private BuildingType type;
	private Direction exit;
	private boolean canPass;
	private int id;
	private Size size;
	
	private ArrayList<NPC> npcs;
	
	private MainController controller;
	
	public Building(Location location, boolean canPass, Size size, BuildingType type,
			Direction exit, Location leaveLocation, MainController controller, int id) {
		this.location = location;
		this.leaveLocation = leaveLocation;
		startLocation = location;
		
		this.type = type;
		this.exit = exit;
		this.canPass = canPass;
		this.size = size;
		this.id = id;
		
		this.controller = controller;
		npcs = new ArrayList<NPC>();
	}
	
	public void enter(Player player) {
		if(canPass) {
			controller.setBuildingView(this);
			
			Location difference = new Location(player.getX() - BUILDING_LOCATION.getX(), player.getY() - BUILDING_LOCATION.getY());
			
			player.setLocation(BUILDING_LOCATION);
			player.setInBuilding(true);
			location = null;
			
			loadNPCs(difference);
		}
	}
	
	public void leave(Player player) {
		if(canPass) {
			controller.removeBuildingView(this);
			
			player.setLocation(leaveLocation);
			player.setInBuilding(false);
			location = startLocation;
			
			unloadNPCs();
		}
	}
	
	public void moveViewLocation(Direction dir) {
		viewLocation.setX(viewLocation.getX() + dir.getX());
		viewLocation.setY(viewLocation.getY() + dir.getY());
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
	
	public void setViewLocation(Location location) {
		viewLocation = location;
	}
	
	public Location getViewLocation() {
		return viewLocation;
	}
	
	public void setNPCs(ArrayList<NPC> npcs) {
		this.npcs = npcs;
	}
	
	public ArrayList<NPC> getNPCs() {
		return npcs;
	}
	
	public int getId() {
		return id;
	}
	
}
