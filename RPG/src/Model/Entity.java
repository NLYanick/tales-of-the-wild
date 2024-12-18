package Model;

public abstract class Entity {

	protected Location location;
	protected String imageURL;
	
	public Entity(String imageURL) {
		this.imageURL = imageURL;
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
	
	public String getURL() {
		return imageURL;
	}
	
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
}
