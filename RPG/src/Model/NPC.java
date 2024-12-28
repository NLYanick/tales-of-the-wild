package Model;

import Controller.MainController;

public class NPC extends Entity {

	private final static int DISTANCE = 120;
	
	private MainController controller;
	
	private Location startLocation;
	private Location endLocation;
	
	private Direction walkDirection;
	private String name;
	private boolean running;
	
	public NPC(String imageURL, Location startLocation, Direction walkDirection, MainController controller, String name) {
		super(imageURL);
		
		this.controller = controller;
		
		this.name = name;
		
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
	
	@Override
	public void setRunningImage(Direction dir) {
		String url;
		switch(dir) {
		case NORTH:
			url = "Images/NPCs/"+ name +"BackRunning.gif";
			break;
		case EAST:
			url = "Images/NPCs/"+ name +"RightRunning.gif";
			break;
		case SOUTH:
			url = "Images/NPCs/"+ name +"Running.gif";
			break;
		case WEST:
			url = "Images/NPCs/"+ name +"LeftRunning.gif";
			break;
		default:
			return;
		}
		imageURL = url;
		controller.switchNPCImage(this, url);
	}

	@Override
	public void setStandingStillAnimation(Direction dir) {
		String url;
		switch(dir) {
		case NORTH:
			url = "Images/NPCs/"+ name +"BackStandingStill.gif";
			break;
		case EAST:
			url = "Images/NPCs/"+ name +"RightStandingStill.gif";
			break;
		case SOUTH:
			url = "Images/NPCs/"+ name +"StandingStill.gif";
			break;
		case WEST:
			url = "Images/NPCs/"+ name +"LeftStandingStill.gif";
			break;
		default: 
			return;
		}
		imageURL = url;
		controller.switchNPCImage(this, url);
	}
	
	private void switchStartAndEndLocations() {
		int tempX = startLocation.getX();
		int tempY = startLocation.getY();
		
		startLocation.setX(endLocation.getX());
		startLocation.setY(endLocation.getY());
		
		endLocation.setX(tempX);
		endLocation.setY(tempY);
		
		setRunningImage(walkDirection);
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
						setStandingStillAnimation(walkDirection);
//						System.out.println(getX() + " " + getY());
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
	
	public String getName() {
		return name;
	}

}
