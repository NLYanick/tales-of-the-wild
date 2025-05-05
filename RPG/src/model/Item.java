package model;

public class Item {

	private String imageUrl, name;
	private Location location, viewLocation;
	private int id, buildingId, npcId, gameId;
	
	public Item(Location location, String name, String imageUrl, int buildingId, int id, int npcId, int gameId) {
		this.location = location;
		this.imageUrl = imageUrl;
		this.name = name;
		this.gameId = gameId;
		this.buildingId = buildingId;
		this.id = id;
		this.npcId = npcId;
		
		viewLocation = new Location(location.getX(), location.getY());
	}
	
	public void moveViewLocation(Direction dir) {
		viewLocation.setX(viewLocation.getX() + dir.getX());
		viewLocation.setY(viewLocation.getY() + dir.getY());
	}
	
	public void resetLocation() {
		location.setX(0);
		location.setY(0);
		viewLocation.setX(0);
		viewLocation.setY(0);
	}
	
	public void setViewLocation(Location location) {
		viewLocation = location;
	}
	
	public void setLocation(Location location) {
		this.location = new Location(location.getX(), location.getY());
	}
	
	public Location getViewLocation() {
		return viewLocation;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public String getName() {
		return name;
	}
	
	public int getX() {
		return location.getX();
	}
	
	public int getY() {
		return location.getY();
	}
	
	public Location getLocation() {
		return location;
	}
	
	public int getId() {
		return id;
	}
	
	public int getBuildingId() {
		return buildingId;
	}
	
	public void setBuildingId(int id) {
		buildingId = id;
	}

	public int getNPCId() {
		return npcId;
	}

	public void setNPCId(int id) {
		npcId = id;
	}
	
	public int getGameId() {
		return gameId;
	}
	
}
