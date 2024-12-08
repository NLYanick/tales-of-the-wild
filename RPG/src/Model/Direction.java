package Model;

public enum Direction {
	NORTH(0, -4), EAST(4, 0), SOUTH(0, 4), WEST(-4, 0);
	
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
