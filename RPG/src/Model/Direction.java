package Model;

public enum Direction {
	NORTH(0, -8), EAST(8, 0), SOUTH(0, 8), WEST(-8, 0);
	
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
