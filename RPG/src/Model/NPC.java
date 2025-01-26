package Model;

import java.util.List;

import Controller.MainController;

public class NPC extends Entity {

	private final static int DISTANCE = 120;
	
	private MainController controller;
	
	private Location startLocation;
	private Location endLocation;
	
	private String name;
	private boolean running;
	private boolean isPaused;
	
	private Location viewLocation;
	
	private List<String> dialog;
	
	public NPC(String imageURL, Location startLocation, Direction walkDirection, MainController controller, 
			String name, List<String> dialog) {
		super(imageURL);
		
		this.controller = controller;
		
		this.name = name;
		
		location.setX(startLocation.getX());
		location.setY(startLocation.getY());
		
		this.startLocation = startLocation;
		movingDirection = walkDirection;
		
		this.dialog = dialog;

		setUpEndLocation(walkDirection);
	}
	
	public NPC(String imageURL, Location startLocation, MainController controller, String name, List<String> dialog) {
		super(imageURL);
		this.startLocation = startLocation;
		this.name = name;
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
	
	private String getCorrectImage() {
		switch(movingDirection) {
		case NORTH:
			return "Images/NPCs/"+ name +"BackRunning.gif";
		case EAST:
			return "Images/NPCs/"+ name +"RightRunning.gif";
		case SOUTH:
			return "Images/NPCs/"+ name +"Running.gif";
		case WEST:
			return "Images/NPCs/"+ name +"LeftRunning.gif";
			default:
				return "Images/NPCs/"+ name +".png";
		}
	}
	
	public void moveViewLocationWithBackground(Direction dir) {
		viewLocation.setX(viewLocation.getX() + dir.getX());
		viewLocation.setY(viewLocation.getY() + dir.getY());
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
		if(controller.npcViewNextStepIsOnPlayerView(this, movingDirection)) {
			return;
		}
		
		if(Location.isSame(location, endLocation)) {
			movingDirection = Direction.getOpposite(movingDirection);
			switchStartAndEndLocations();
			setRunningImage(movingDirection);
		}
		location.setX(getX() + movingDirection.getX()); 
		location.setY(getY() + movingDirection.getY()); 
		
		viewLocation.setX(viewLocation.getX() + movingDirection.getX()); 
		viewLocation.setY(viewLocation.getY() + movingDirection.getY()); 
		
		checkForCorrectImage();
	}
	
	private void checkForCorrectImage() {
		if(!imageURL.equals(getCorrectImage())) {
			setRunningImage(movingDirection);
		}
	}
	
	private boolean nextStepIsPlayer() {
		int multiplier = 8;
		int extraSpace = Direction.getAmount() * multiplier;
		
		Location nextLocation = location.getNext(movingDirection);
		Location playerLocation = controller.getPlayerLocation();
		
		if(Location.isSame(nextLocation, playerLocation)
			|| (Location.isLess(nextLocation, new Location(playerLocation.getX() + extraSpace, playerLocation.getY() + extraSpace)) 
			&& Location.isGreater(nextLocation, new Location(playerLocation.getX() - extraSpace, playerLocation.getY() - extraSpace)))) {
			return true;
		}
		return false;
	}
	
	public void setUpThread() {
		
		Thread walkingThread = new Thread(() -> {
			
			running = true;
			while(running) {
				if(Location.isSame(location, endLocation)) {
					try {
						setStandingStillAnimation(movingDirection);
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while(isPaused) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				moveInLine();
				controller.moveNPCViewWithScreen(this);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});
		walkingThread.start();
	}
	
	public void startDialog(Direction direction) {
		pauzeThread();
		setStandingStillAnimation(direction);
		
		controller.addDialogView(dialog);
	}
	
	public void pauzeThread() {
		isPaused = true;
	}
	
	public void resumeThread() {
		isPaused = false;
	}
	
	public Direction getWalkDirection() {
		return movingDirection;
	}
	
	public void setThreadRunning(boolean running) {
		this.running = running;
		resumeThread();
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

	public Location getViewLocation() {
		return viewLocation;
	}

	public void setViewLocation(Location viewLocation) {
		this.viewLocation = viewLocation;
	}
	
	public List<String> getDialog(){
		return dialog;
	}

}
