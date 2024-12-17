package Model;

public enum Direction {
	NORTH(0, -6), EAST(6, 0), SOUTH(0, 6), WEST(-6, 0);
	
	private int x, y;
	
	private Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
