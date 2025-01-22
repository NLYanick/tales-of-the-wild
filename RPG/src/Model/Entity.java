package Model;

public abstract class Entity {

	protected Location location;
	protected String imageURL;
	protected Direction movingDirection;
	
	public Entity(String imageURL) {
		this.imageURL = imageURL;
		location = new Location();
		movingDirection = Direction.SOUTH;
	}
	
	public void move(Direction dir) {
		location.setX(getX() + dir.getX()); 
		location.setY(getY() + dir.getY()); 
	}
	
	public abstract void setRunningImage(Direction dir);
	
	public abstract void setStandingStillAnimation(Direction dir);
	
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
	
	public Direction getMovingDirection() {
		return movingDirection;
	}
	
	public void setMovingDirection(Direction movingDirection) {
		this.movingDirection = movingDirection;
	}
	
}
