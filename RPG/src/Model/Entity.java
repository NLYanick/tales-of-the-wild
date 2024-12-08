package Model;

public abstract class Entity {

	int x, y;
	
	public void move(Direction dir) {
		x += dir.getX();
		y += dir.getY();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
