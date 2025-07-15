package model;

public class Item {

	public final static int ITEMWIDTH = 32;
	
	private String imageUrl, name, description;
	private Location location, viewLocation;
	private int id;
	
	public Item(Location location, String name, String imageUrl, String description, int id) {
		this.location = location;
		this.imageUrl = imageUrl;
		this.name = name;
		this.description = description;
		this.id = id;
		
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
			
	public String getDescription() {
		return description;
	}
	
}
