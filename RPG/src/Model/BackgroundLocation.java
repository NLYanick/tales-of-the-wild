package Model;

public class BackgroundLocation {

	private Location location;
	
	public BackgroundLocation(int x, int y) {
		location.setX(x);
		location.setY(y);
	}
	
	public void move(Direction dir) {
		location.setX(getX() + dir.getX()); 
		location.setY(getY() + dir.getY()); 
	}

	public int getX() {
		return location.getX();
	}

	public void setX(int x) {
		location.setX(x);
	}

	public int getY() {
		return location.getY();
	}

	public void setY(int y) {
		location.setY(y);
	}
	
}
