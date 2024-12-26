package Model;

import Controller.MainController;

public class NPC extends Entity {

	private final static int DISTANCE = 60;
	
	private MainController controller;
	
	private Location startLocation;
	private Location endLocation;
	
	private Direction walkDirection;
	private boolean running;
	
	public NPC(String imageURL, Location startLocation, Direction walkDirection, MainController controller) {
		super(imageURL);
		
		this.controller = controller;
		
		location.setX(startLocation.getX());
		location.setY(startLocation.getY());
		
		this.startLocation = startLocation;
		this.walkDirection = walkDirection;

		setUpEndLocation(walkDirection);
	}
	
	public NPC(String imageURL, Location startLocation, MainController controller) {
		super(imageURL);
		this.startLocation = startLocation;
	}
	
	private void setUpEndLocation(Direction direction) {
		endLocation = new Location();
		int x = startLocation.getX();
		int y = startLocation.getY();

		switch(direction) {
		case NORTH:
			endLocation.setX(x);
			endLocation.setY(y - DISTANCE); 
			break;
		case EAST:
			endLocation.setX(x + DISTANCE);
			endLocation.setY(y); 
			break;
		case SOUTH:
			endLocation.setX(x);
			endLocation.setY(y + DISTANCE); 
			break;
		case WEST:
			endLocation.setX(x - DISTANCE);
			endLocation.setY(y); 
			break;
		}
	}

	public void moveWithBackground(Direction direction) {
		
		startLocation.setX(startLocation.getX() + direction.getX()); 
		startLocation.setY(startLocation.getY() + direction.getY()); 
		
		endLocation.setX(endLocation.getX() + direction.getX()); 
		endLocation.setY(endLocation.getY() + direction.getY()); 
		
		move(direction);
	}
	
	private void switchStartAndEndLocations() {
		int tempX = startLocation.getX();
		int tempY = startLocation.getY();
		
		startLocation.setX(endLocation.getX());
		startLocation.setY(endLocation.getY());
		
		endLocation.setX(tempX);
		endLocation.setY(tempY);
	}

	private void moveInLine() {
		if(location.getX() == endLocation.getX() && location.getY() == endLocation.getY()) {
			walkDirection = Direction.getOpposite(walkDirection);
			switchStartAndEndLocations();
		}
		location.setX(getX() + walkDirection.getX()); 
		location.setY(getY() + walkDirection.getY()); 
	}
	
	public void setUpThread(Direction dir) {
		
		Thread walkingThread = new Thread(() -> {
			
			running = true;
			while(running) {
				if(location.getX() == endLocation.getX() && location.getY() == endLocation.getY()) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				moveInLine();
				controller.setNPCViewLocation(this);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});
		walkingThread.start();
	}
	
	public Direction getWalkDirection() {
		return walkDirection;
	}
	
	public void setThreadRunning(boolean running) {
		this.running = running;
	}
	
	public Location getStartLocation() {
		return startLocation;
	}

	public Location getEndLocation() {
		return endLocation;
	}

}
