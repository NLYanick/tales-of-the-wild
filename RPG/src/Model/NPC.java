package Model;

public class NPC extends Entity {

	private final static int DISTANCE = 10;
	
	private Location startLocation;
	private Location endLocation;
	
	private Direction walkDirection;
	
	public NPC(String imageURL, Location startLocation, Direction walkDirection) {
		super(imageURL);
		
		this.startLocation = startLocation;
		endLocation = startLocation;
		this.walkDirection = walkDirection;
		
		setUpEndLocation(startLocation, walkDirection);
	}
	
	public NPC(String imageURL, Location startLocation) {
		super(imageURL);
		this.startLocation = startLocation;
	}
	
	private void setUpEndLocation(Location startLocation, Direction direction) {
		switch(direction) {
		case NORTH:
			endLocation.setX(startLocation.getX());
			endLocation.setY(startLocation.getY() - DISTANCE); 
			break;
		case EAST:
			endLocation.setX(startLocation.getX() + DISTANCE);
			endLocation.setY(startLocation.getY()); 
			break;
		case SOUTH:
			endLocation.setX(startLocation.getX());
			endLocation.setY(startLocation.getY() + DISTANCE); 
			break;
		case WEST:
			endLocation.setX(startLocation.getX() - DISTANCE);
			endLocation.setY(startLocation.getY()); 
			break;
		}
	}
	
	private void switchStartAndEndLocations() {
		int tempX = startLocation.getX();
		int tempY = startLocation.getY();
		
		startLocation.setX(endLocation.getX());
		startLocation.setY(endLocation.getY());
		
		endLocation.setX(tempX);
		endLocation.setY(tempY);
	}

	public void move() {
		if(location.getX() == endLocation.getX() && location.getY() == endLocation.getY()) {
			walkDirection = Direction.getOpposite(walkDirection);
			switchStartAndEndLocations();
		}
		location.setX(getX() + walkDirection.getX()); 
		location.setY(getY() + walkDirection.getY()); 
	}
	
	public Location getStartLocation() {
		return startLocation;
	}

	public Location getEndLocation() {
		return endLocation;
	}

	public void moveWithBackground(Direction direction) {
		
		startLocation.setX(getStartLocation().getX() + direction.getX()); 
		startLocation.setY(getStartLocation().getY() + direction.getY()); 
		
		endLocation.setX(getEndLocation().getX() + direction.getX()); 
		endLocation.setY(getEndLocation().getY() + direction.getY()); 
		
		move(direction);
	}

}
