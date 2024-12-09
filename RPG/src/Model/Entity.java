package Model;

public abstract class Entity {

	protected int x, y;
	protected String imageURL;
	
	public Entity(String imageURL) {
		this.imageURL = imageURL;
	}
	
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
	
	public String getURL() {
		return imageURL;
	}
	
}
