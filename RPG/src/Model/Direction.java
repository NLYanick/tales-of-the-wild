package Model;

public enum Direction {
	NORTH(0, -6), EAST(6, 0), SOUTH(0, 6), WEST(-6, 0);
	
	private Location location;
	
	private Direction(int x, int y) {
		location.setX(x);
		location.setY(y);
	}
	
	public int getX() {
		return location.getX();
	}
	
	public int getY() {
		return location.getY();
	}
}
