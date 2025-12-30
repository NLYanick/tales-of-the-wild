package model;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import controller.FileIO;
import controller.MainController;
import javafx.scene.paint.Color;

public class Building {

	public final static Location UNLOAD_LOCATION = new Location(-7000, -7000);
	public final static Location INSIDE_LOCATION = new Location(-3000, -3000);
	
	protected Location location, leaveLocation, entranceLocation, viewLocation, spawnLocation;
	
	protected BuildingType type;
	protected Direction exit;
	protected boolean canPass;
	protected int id;
	protected ArrayList<BuildingTile> tiles;
	protected HashMap<String, String> tileSettings;
	protected Color color; // TRANSPARENT is default (null)
	protected int tileSize;
	protected Shape outsideBounds;
	
	protected ArrayList<NPC> npcs;
	protected ArrayList<Item> items;
	
	protected MainController controller;
	protected Game game;
	
	public Building(boolean canPass, BuildingType type, Direction exit, Location location, Location leaveLocation, int id, 
			Location entranceLocation, ArrayList<BuildingTile> tiles, HashMap<String, String> tileSettings, Color color) {
		this.location = location;
		this.leaveLocation = leaveLocation;
		this.entranceLocation = entranceLocation; 
		
		this.type = type;
		this.exit = exit;
		this.canPass = canPass;
		this.id = id;
		this.tiles = tiles;
		this.tileSettings = tileSettings;
		this.color = color;
		
		npcs = new ArrayList<NPC>();
		items = new ArrayList<Item>();
		setOutsideBounds();
		
		setUp();
	}
	
	private void setOutsideBounds() {
		int tileSize = FileIO.STANDARD_IMAGE_SIZE;
		
		// The type determines what the borders are
		switch (type) {
		case BRICK:
			outsideBounds = createBounds(location.getX(), location.getY() + tileSize, 3, 3, tileSize, 0, 0);
			break;
		case SHOP:
			int xWalkSpace = 16;
			outsideBounds = createBounds(location.getX(), location.getY() + tileSize, 4, 2, tileSize, xWalkSpace, 0);
			break;
		case TENT:
			outsideBounds = createBounds(location.getX(), location.getY(), 2, 2, tileSize, 0, 0);
			break;
		default:
			outsideBounds = createBounds(location.getX(), location.getY(), 2, 2, tileSize, 0, 0);
			break;
		}
	}
	
	private Path2D createBounds(int x, int y, double tilesWidth, double tilesHeight, int tileSize, int xWalkSpace, int yWalkSpace) {
		double width = tilesWidth * tileSize - xWalkSpace * 2; // * 2 because otherwise only 1 side shifts
		double height = tilesHeight * tileSize - yWalkSpace * 2;
		
		x += xWalkSpace;
		y += yWalkSpace;
	    
	    Path2D.Double path = new Path2D.Double();
	    
	    path.moveTo(x, y);
	    path.lineTo(x + width, y);
	    path.lineTo(x + width, y + height);
	    path.lineTo(x, y + height);
	    path.closePath();
	    
	    return path;
	}

	private void setUp() {
		if(tileSettings.size() == 0) {
			tileSettings.put("size", "big");
		}
		
		switch(tileSettings.get("size")) {
			case "small": tileSize = (int) (FileIO.STANDARD_IMAGE_SIZE * 0.75); break;
			case "big": tileSize = FileIO.STANDARD_IMAGE_SIZE; break;
		}
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
		int wallSize = tileSize;
		double sumX = 0, sumY = 0;
		
		for (BuildingTile tile : spawnTiles) {
			sumX += tile.getX() * wallSize + (wallSize / 2);
			sumY += tile.getY() * wallSize + (wallSize / 2);
		}
		
		int x = (int) (sumX / spawnTiles.size() + INSIDE_LOCATION.getX());
		int y = (int) (sumY / spawnTiles.size() + INSIDE_LOCATION.getY());
		
		return new Location(x, y);
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
		return npcs.stream().filter(n -> game.hasNPCNearby(n, movingDirection)).findFirst().orElse(null);
	}
	
	public boolean collidesWith(Player player, Direction dir) {

		int multiplier = 2;
		Location playerLocation = player.getLocation();
		int nextX = playerLocation.getX() + (dir.getX() * multiplier) - INSIDE_LOCATION.getX();
		int nextY = playerLocation.getY() + (dir.getY() * multiplier) - INSIDE_LOCATION.getY();
		
		if(canWalkInBuilding(new Location(nextX, nextY))) {
			return false;
		} else if(dir == exit && isOnExit(nextX, nextY)) {
			leave(player);
			return true;
		}
		
		return true;
	}
	
	public boolean collidesWithOutside(Location nextStep) {
		return outsideBounds.contains(nextStep.getX(), nextStep.getY());
	}
	
	private boolean canWalkInBuilding(Location nextLocation) {
		int wallSize = tileSize;
		
		ArrayList<BuildingTile> walkTiles = new ArrayList<BuildingTile>(tiles.stream().filter(t -> t.canWalkOn()).collect(Collectors.toList()));
		for (BuildingTile tile : walkTiles) {
			if(Location.isGreater(nextLocation, new Location(tile.getX() * wallSize, tile.getY() * wallSize)) && 
					Location.isLess(nextLocation, new Location((tile.getX() + 1) * wallSize, (tile.getY() + 1) * wallSize))) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isOnExit(int nextX, int nextY) {
		int wallSize = tileSize;
		
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
	
	public Location getSpawnLocation() {
		return spawnLocation;
	}
	
	public int getTileSize() {
		return tileSize;
	}

}
