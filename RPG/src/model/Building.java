package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

import controller.FileIO;
import controller.MainController;
import javafx.scene.paint.Color;

public class Building {

	public final static Location UNLOAD_LOCATION = new Location(-7000, -7000);
	public final static Location INSIDE_LOCATION = new Location(-3000, -3000);
	
	protected Location leaveLocation, entranceLocation, viewLocation, spawnLocation;
	
	protected BuildingType type;
	protected Direction exit;
	protected boolean canPass;
	protected int id;
	protected Size size;
	protected ArrayList<BuildingTile> tiles;
	protected HashMap<String, String> tileSettings;
	protected Color color; // TRANSPARENT is default (null)
	protected boolean wallsHalfWidth, wallsHalfHeight;
	
	protected ArrayList<NPC> npcs;
	protected ArrayList<Item> items;
	
	protected MainController controller;
	protected Game game;
	
	public Building(boolean canPass, BuildingType type, Direction exit,
			Location leaveLocation, int id, Location entranceLocation, ArrayList<BuildingTile> tiles, 
			HashMap<String, String> tileSettings, Color color) {
		this.leaveLocation = leaveLocation;
		this.entranceLocation = entranceLocation; 
		
		this.type = type;
		this.exit = exit;
		this.canPass = canPass;
		this.id = id;
		this.tiles = tiles;
//		this.tileSettings = tileSettings;
		this.tileSettings = new HashMap<String, String>();
		this.color = color;
		
		this.size = getNewSize();
		npcs = new ArrayList<NPC>();
		items = new ArrayList<Item>();
		
		setUp();
	}
	
	private Size getNewSize() {
		int imgSize = FileIO.STANDARD_IMAGE_SIZE;
		BuildingTile maxTile = tiles.stream().max(Comparator.comparingInt(Tile::getX).thenComparingInt(Tile::getY)).orElse(null);
		return new Size(maxTile.getX() * imgSize, maxTile.getY() * imgSize);
	}

	private void setUp() {
		if(tileSettings.size() == 0) {
			tileSettings.put("width", "full");
			tileSettings.put("height", "full");
		}
		wallsHalfWidth = tileSettings.get("width").equals("half");
		wallsHalfHeight = tileSettings.get("height").equals("half");
	}
	
	protected ArrayList<BuildingTile> getTilesOfType(BuildingTileType type) {
		return new ArrayList<BuildingTile>(tiles.stream().filter(t -> t.getType() == type).collect(Collectors.toList()));
	}
	
	public void enter(Player player) {
		if(canPass) {
			spawnLocation = calculateSpawnLocation();
			Location playerInsideLocation = spawnLocation;
			
			loadInside();
			game.setBuildingView(this);
			
			player.setInBuilding(true);
			game.teleportPlayer(playerInsideLocation);
		}
	}
	
	public void leave(Player player) {
		if(canPass) {
			game.removeBuildingView();
			
			unloadInside();
			
			player.setInBuilding(false);
			game.teleportPlayer(leaveLocation);
			game.removeCurrentBuilding();
		}
	}
	
	private Location calculateSpawnLocation() {
		ArrayList<BuildingTile> spawnTiles = new ArrayList<BuildingTile>(tiles.stream().filter(t -> t.isSpawn()).collect(Collectors.toList()));
		int wallSize = FileIO.STANDARD_IMAGE_SIZE;
		double avgX = 0, avgY = 0;
		
		for (BuildingTile tile : spawnTiles) {
			avgX += tile.getX();
			avgY += tile.getY();
		}
		
		avgX = (avgX + 1) / spawnTiles.size() * wallSize + INSIDE_LOCATION.getX();
		avgY = avgY / spawnTiles.size() * wallSize + INSIDE_LOCATION.getY();
		
		int x = (int) avgX;
		int y = (int) avgY;
		
		int entranceSpacing = 64;
		
		switch(exit) {
			case NORTH: return new Location(x, y - entranceSpacing);
			case EAST: return new Location(x + entranceSpacing, y);
			case SOUTH: return new Location(x, y + entranceSpacing);
			case WEST: return new Location(x - entranceSpacing, y);
			default: return new Location(x, y);
		}
	}
	
	private void loadInside() {
		for(NPC npc : npcs) {
			Location originalLoc = game.getOriginalNPCBuildingLocation(npc, id);
			npc.setLocation(new Location(originalLoc.getX() + INSIDE_LOCATION.getX(), originalLoc.getY() + INSIDE_LOCATION.getY()));
		}
		for(Item item : items) {
			Location originalLoc = game.getOriginalItemBuildingLocation(item, id);
			item.setLocation(new Location(originalLoc.getX() + INSIDE_LOCATION.getX(), originalLoc.getY() + INSIDE_LOCATION.getY()));
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
	
	public void moveViewLocation(Direction dir) {
		viewLocation.move(dir);
	}
	
	public Item getNearbyItem(Player player) {
		for(Item item : items) {
			if(player.isOnItem(item)) {
				return item;
			}
		}
		return null;
	}
	
	public NPC getNearbyNPC(Direction movingDirection) {
		for(NPC npc: npcs) {
			if(game.hasNPCNearby(npc, movingDirection)) {
				return npc;
			}
		}
		return null;
	}
	
	public boolean collidesWith(Player player, Direction dir) {
		
		int multiplier = 2;
		Location playerLocation = player.getLocation();
		int nextX = playerLocation.getX() + (dir.getX() * multiplier) - INSIDE_LOCATION.getX();
		int nextY = playerLocation.getY() + (dir.getY() * multiplier) - INSIDE_LOCATION.getY();
		
		if(isInBuilding(new Location(nextX, nextY))) {
			return false;
		} else if(dir == exit && isOnExit(nextX, nextY)) {
			leave(player);
			return true;
		}
		
		return true;
	}
	
	private boolean isInBuilding(Location nextLocation) {
		int wallSize = FileIO.STANDARD_IMAGE_SIZE;
		int wallWidth = wallsHalfWidth ? wallSize/2 : wallSize;
		int wallHeight = wallsHalfHeight ? wallSize/2 : wallSize;
		
		ArrayList<BuildingTile> walkTiles = new ArrayList<BuildingTile>(tiles.stream().filter(t -> t.getType() == BuildingTileType.FLOOR 
				|| t.getType() == BuildingTileType.EXIT).collect(Collectors.toList()));
		for (BuildingTile tile : walkTiles) {
			if(Location.isGreater(nextLocation, new Location(tile.getX() * wallWidth, tile.getY() * wallHeight)) && 
					Location.isLess(nextLocation, new Location((tile.getX() + 1) * wallWidth, (tile.getY() + 1) * wallHeight))) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isOnExit(int nextX, int nextY) {
		int wallSize = FileIO.STANDARD_IMAGE_SIZE;
		
		for (BuildingTile tile : getTilesOfType(BuildingTileType.EXIT)) {
			switch(exit) {
				case NORTH: if (nextY <= tile.getY() * wallSize) return true; 
					break;
	            case EAST: if (nextX >= tile.getX() * wallSize + wallSize) return true; 
	            	break;
	            case SOUTH: if (nextY >= tile.getY() * wallSize + wallSize) return true; 
	            	break;
	            case WEST: if (nextX <= tile.getX() * wallSize) return true; 
	            	break;
			} 
		}
		
		return false;
	}
		
	public void addNPC(NPC npc) {
		npcs.add(npc);
	}
	
	public void removeNPC(NPC npc) {
		npcs.remove(npc);
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public void removeItem(Item item) {
		items.remove(item);
	}
	
	public boolean containsItem(Item item) {
		return items.contains(item);
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
	
	public int getX() {
		return INSIDE_LOCATION.getX();
	}
	
	public int getY() {
		return INSIDE_LOCATION.getY();
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	public Color getColor() {
		return color;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public ArrayList<NPC> getNPCs() {
		return npcs;
	}
	
	public ArrayList<BuildingTile> getTiles() {
		return tiles;
	}
	
	public HashMap<String, String> getTileSettings() {
		return tileSettings;
	}
	
	public Location getSpawnLocation() {
		return spawnLocation;
	}
}
