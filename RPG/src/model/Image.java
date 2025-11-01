package model;

public class Image {

	private String url;
	private boolean canWalkOn;
	private Location location;
	
	public Image(String url, boolean canWalkOn) {
		this.url = url;
		this.canWalkOn = canWalkOn;
	}

	public boolean canWalkOn() {
		return canWalkOn;
	}

	public String getUrl() {
		return url;
	}

	public int getX() {
		return location.getX();
	}
	
	public int getY() {
		return location.getY();
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return location;
	}
	
}
