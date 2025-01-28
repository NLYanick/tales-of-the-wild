package model;

public class Item {

	private String imageUrl;
	private String name;
	private Location location;
	
	public Item(Location location, String name, String imageUrl) {
		this.location = location;
		this.imageUrl = imageUrl;
		this.name = name;
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
	
}
