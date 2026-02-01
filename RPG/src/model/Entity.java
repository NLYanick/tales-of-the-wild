package model;

import java.awt.Rectangle;

public abstract class Entity {
	
	public final static int ENTITY_HEIGHT = 96;
	public final static int ENTITY_WIDTH = ENTITY_HEIGHT / 2;

	protected Location location;
	protected Inventory inventory;
	protected Rectangle bounds;
	
	protected String imageURL;
	protected Direction movingDirection;
	protected String name;
	protected int talesCoins;
	
	public Entity(String imageURL, String name, int talesCoins) {
		this.imageURL = imageURL;
		this.name = name;
		this.talesCoins = talesCoins;
		
		location = new Location();
		inventory = new Inventory();
		movingDirection = null;
		bounds = new Rectangle(location.getX() - ENTITY_WIDTH/2, location.getY() - ENTITY_HEIGHT/2, ENTITY_WIDTH, ENTITY_HEIGHT);
	}
	
	public void move(Direction dir) {
		location.move(dir);
		bounds.setBounds(location.getX() - ENTITY_WIDTH/2, location.getY() - ENTITY_HEIGHT/2, ENTITY_WIDTH, ENTITY_HEIGHT);
	}
	
	public void addItem(Item item) {
		inventory.addItem(item);
		item.resetLocation();
	}
	
	public void removeItem(Item item) {
		inventory.removeItem(item);
	}

	public Rectangle getBounds() {
		bounds.setBounds(location.getX() - ENTITY_WIDTH/2, location.getY() - ENTITY_HEIGHT/2, ENTITY_WIDTH, ENTITY_HEIGHT);
	    return bounds;
	}
	
	public void addCoins(int amount) {
		talesCoins += amount;
	}
	public void subtractCoins(int amount) {
		talesCoins -= amount;
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
		this.location = Location.createNew(location);
		bounds.setBounds(location.getX() - ENTITY_WIDTH/2, location.getY() - ENTITY_HEIGHT/2, ENTITY_WIDTH, ENTITY_HEIGHT);
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
	
	public int getTalesCoins() {
		return talesCoins;
	}
	
}
