package Model;

public enum Direction {
	NORTH(0, -6), EAST(6, 0), SOUTH(0, 6), WEST(-6, 0);
	
	private Location location;
	
	private Direction(int x, int y) {
		location = new Location(x, y);
	}
	
	public int getX() {
		return location.getX();
	}
	
	public int getY() {
		return location.getY();
	}
	
	public static Direction getOpposite(Direction direction) {
		switch(direction) {
			case NORTH: 
				return SOUTH;
			case EAST: 
				return WEST;
			case SOUTH: 
				return NORTH;
			case WEST: 
				return EAST;
			default: return direction;
		}
	}
	
	public static Direction getNext(Direction direction) {
		switch(direction) {
			case NORTH: 
				return EAST;
			case EAST: 
				return SOUTH;
			case SOUTH: 
				return WEST;
			case WEST: 
				return NORTH;
			default: return direction;
		}
	}
	
	public static boolean isHorizontal(Direction dir) {
		return dir == EAST || dir == WEST;
	}
	
	public static boolean isVertical(Direction dir) {
		return dir == NORTH || dir == SOUTH;
	}
	
	public static int getAmount() {
		return 6;
	}
}
