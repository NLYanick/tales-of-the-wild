package Model;

public class Location {

	private int x, y;
	
	public Location() {
		
	}
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static boolean isSame(Location locationOne, Location locationTwo) {
		return locationOne.getX() == locationTwo.getX() && locationOne.getY() == locationTwo.getY();
	}
	
	public static boolean isGreater(Location locationOne, Location locationTwo) {
		return locationOne.getX() > locationTwo.getX() && locationOne.getY() > locationTwo.getY();
	}
	
	public static boolean isLess(Location locationOne, Location locationTwo) {
		return locationOne.getX() < locationTwo.getX() && locationOne.getY() < locationTwo.getY();
	}
	
	public Location getNext(Direction direction) {
		return new Location(x + direction.getX(), y + direction.getY());
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
