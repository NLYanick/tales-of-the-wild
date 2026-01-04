package model;

public class Tile {

	protected String url;
	protected boolean canWalkOn;
	protected Location location, originalLocation;
	
	public Tile(String url, boolean canWalkOn) {
		this.url = url;
		this.canWalkOn = canWalkOn;
	}
	
	public Tile(Location location, boolean canWalkOn) {
		this.location = location;
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
	
	public void setOriginalLocation(Location originalLocation) {
		this.originalLocation = originalLocation;
	}
	
	public Location getOriginalLocation() {
		return originalLocation;
	}
	
}
