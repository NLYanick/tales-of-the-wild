package model;

public abstract class Entity {

	protected Location location;
	protected String imageURL;
	protected Direction movingDirection;
	protected String name;
	protected boolean inBuilding;
	protected int gameId;
	
	public Entity(String imageURL, String name, int gameId) {
		this.imageURL = imageURL;
		this.name = name;
		this.gameId = gameId;
		
		location = new Location();
		movingDirection = null;
	}
	
	public void move(Direction dir) {
		location.setX(getX() + dir.getX()); 
		location.setY(getY() + dir.getY()); 
	}
	
	public abstract void setRunningImage(Direction dir);
	
	public abstract void setStandingStillAnimation(Direction dir);
	
	/*    Getters & Setters    */
	
	public int getX() {
		return location.getX();
	}

	public void setX(int x) {
		location.setX(x);
	}

	public int getY() {
		return location.getY();
	}

	public void setY(int y) {
		location.setY(y);
	}
	
	public String getURL() {
		return imageURL;
	}
	
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Direction getMovingDirection() {
		return movingDirection;
	}
	
	public void setMovingDirection(Direction movingDirection) {
		this.movingDirection = movingDirection;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isInBuilding() {
		return inBuilding;
	}
	
	public void setInBuilding(boolean inBuilding) {
		this.inBuilding = inBuilding;
	}
	
	public int getGameId() {
		return gameId;
	}
	
}
