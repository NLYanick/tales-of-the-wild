package model;

public class Location {

	private int x, y;
	
	public Location() {
		
	}
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "X: " + x + " Y: " + y;
	}
	
	public static boolean isSame(Location locationOne, Location locationTwo) {
		return locationOne.getX() == locationTwo.getX() && locationOne.getY() == locationTwo.getY();
	}
	
	public static boolean isGreater(Location locationOne, Location locationTwo) {
		return locationOne.getX() >= locationTwo.getX() && locationOne.getY() >= locationTwo.getY();
	}
	
	public static boolean isLess(Location locationOne, Location locationTwo) {
		return locationOne.getX() <= locationTwo.getX() && locationOne.getY() <= locationTwo.getY();
	}
	
	public static boolean isDefault(Location location) {
		return location.getX() == 0 && location.getY() == 0;
	}
	
	public static Location createNew(Location location) {
		return new Location(location.getX(), location.getY());
	}
	
	public Location getNext(Direction direction) {
		return new Location(x + direction.getX(), y + direction.getY());
	}
	
	public void move(Direction direction) {
		x += direction.getX();
		y += direction.getY();
	}
	
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
}
