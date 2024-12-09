package Model;

public class BackgroundLocation {

	private int x, y;
	
	public BackgroundLocation(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public void move(Direction dir) {
		x += dir.getX();
		y += dir.getY();
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
