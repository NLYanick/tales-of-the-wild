package model;

import java.util.ArrayList;

import controller.MainController;

public class NPC extends Entity {

	private final static int DISTANCE = 120;
	
	private MainController controller;
	
	private Location startLocation, endLocation;
	
	private boolean running, isPaused, isInDialog;
	private int buildingId, id;
	
	private Location viewLocation;
	
	private ArrayList<Dialog> dialogs;
	private ArrayList<Item> items;
	
	public NPC(String imageURL, Location startLocation, Direction walkDirection, String name, ArrayList<Dialog> dialog, 
			int buildingId, int id, int gameId) {
		super(imageURL, name, gameId);
				
		location.setX(startLocation.getX());
		location.setY(startLocation.getY());
		
		this.startLocation = startLocation;
		movingDirection = walkDirection;
		
		this.dialogs = dialog;
		this.buildingId = buildingId;
		this.id = id;
		
		items = new ArrayList<Item>();

		setUpEndLocation(walkDirection);
	}
	
	public NPC(String imageURL, Location startLocation, String name, ArrayList<Dialog> dialogs, int buildingId, int id, int gameId) {
		super(imageURL, name, gameId);
		this.startLocation = startLocation;
		
		location.setX(startLocation.getX());
		location.setY(startLocation.getY());
		
		this.dialogs = dialogs;
		this.buildingId = buildingId;
		this.id = id;
		this.gameId = gameId;
		
		items = new ArrayList<Item>();
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
	
	public boolean nextStepIsPlayer(Location playerLocation, Direction dir) {
		int lessVerticalPersonalSpace = 20;
		int lessHorizontalPersonalSpace = 10;
		int multi = 3;
		
		int width = 32;
		int height = 80;
				
		if(Location.isGreater(new Location(location.getX() + dir.getX(), location.getY() + dir.getY()), 
				new Location(playerLocation.getX() - lessHorizontalPersonalSpace - width, 
				playerLocation.getY() - lessVerticalPersonalSpace))
			&& Location.isLess(new Location(location.getX(), location.getY() + dir.getY()), 
				new Location(playerLocation.getX() + width * 2 - lessHorizontalPersonalSpace + dir.getX(), 
				playerLocation.getY() + (int) (height * 1.5) - lessVerticalPersonalSpace * multi))) {
			return true;
		}
		
		return false;
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
		if(controller.getPlayer() != null && nextStepIsPlayer(controller.getPlayerLocation(), movingDirection)) {
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
		
		controller.addDialogView(dialogs, items);
		isInDialog = true;
	}
	
	public void pauzeThread() {
		isPaused = true;
	}
	
	public void resumeThread() {
		isPaused = false;
	}
	
	public void setThreadRunning(boolean running) {
		this.running = running;
		resumeThread();
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public Direction getGoodDirection(Direction dir) {
		if((dir.equals(Direction.NORTH) || dir.equals(Direction.WEST))) {
			dir = Direction.getOpposite(dir);
		}
		return dir;
	} 
	
	// ----- Getters & Setters -----
	
	public Location getStartLocation() {
		return startLocation;
	}

	public Location getEndLocation() {
		return endLocation;
	}

	public Location getViewLocation() {
		return viewLocation;
	}

	public void setViewLocation(Location viewLocation) {
		this.viewLocation = viewLocation;
	}
	
	public ArrayList<Dialog> getDialog(){
		return dialogs;
	}
	
	public boolean isInDialog() {
		return isInDialog;
	}
	
	public void setIsInDialog(boolean isInDialog) {
		this.isInDialog = isInDialog;
	}
	
	public void setMainController(MainController controller) {
		this.controller = controller;
	}
	
	public int getBuildingId() {
		return buildingId;
	}
	
	public int getId() {
		return id;
	}

}
