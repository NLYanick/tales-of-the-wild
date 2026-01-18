package model;

import java.util.ArrayList;

public class NPC extends Entity {

	private final static int DISTANCE = 120;
	
	private Game game;
	
	private Location startLocation, endLocation;
	
	private boolean running, isPaused, isInDialog;
	private int id;
	private boolean shopSeller;
	
	private Location viewLocation;
	
	private ArrayList<Dialog> dialogs;
	
	public NPC(String imageURL, Location startLocation, Direction walkDirection, String name, ArrayList<Dialog> dialog, int id, boolean shopSeller) {
		super(imageURL, name);
				
		location.moveTo(startLocation.getX(), startLocation.getY());
		
		this.startLocation = startLocation;
		movingDirection = walkDirection;
		
		this.dialogs = dialog;
		this.id = id;
		this.shopSeller = shopSeller;
		
		setUpEndLocation(walkDirection);
	}
	
	public NPC(String imageURL, Location startLocation, String name, ArrayList<Dialog> dialogs, int id, boolean shopSeller) {
		super(imageURL, name);
		this.startLocation = startLocation;
		
		location.moveTo(startLocation.getX(), startLocation.getY());
		
		this.dialogs = dialogs;
		this.id = id;
		this.shopSeller = shopSeller;
		
		inventory = new Inventory();
	}
	
	private void setUpEndLocation(Direction direction) {
		endLocation = new Location();
		int x = startLocation.getX();
		int y = startLocation.getY();

		switch(direction) {
		case NORTH:
			endLocation.moveTo(x, y - DISTANCE); 
			break;
		case EAST:
			endLocation.moveTo(x + DISTANCE, y);
			break;
		case SOUTH:
			endLocation.moveTo(x, y + DISTANCE); 
			break;
		case WEST:
			endLocation.moveTo(x - DISTANCE, y);
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
		game.switchNPCImage(this, url);
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
		game.switchNPCImage(this, url);
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
	
	public void moveViewLocation(Direction dir) {
		viewLocation.move(dir);
	}
	
	private void switchStartAndEndLocations() {
		int tempX = startLocation.getX();
		int tempY = startLocation.getY();
		
		startLocation.moveTo(endLocation.getX(), endLocation.getY());
		
		endLocation.moveTo(tempX, tempY);
	}

	private void moveInLine() {
		if(game.getPlayer() != null && nextStepIsPlayer(game.getPlayerLocation(), movingDirection)) {
			return;
		}
		
		if(Location.isSame(location, endLocation)) {
			movingDirection = Direction.getOpposite(movingDirection);
			switchStartAndEndLocations();
			setRunningImage(movingDirection);
		}
		move(movingDirection);
		viewLocation.move(movingDirection);
		
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
				game.moveNPCViewWithScreen(this);
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
		
		game.addDialogView(this);
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
		
	public Direction getGoodDirection(Direction dir) {
		if((dir.equals(Direction.NORTH) || dir.equals(Direction.WEST))) {
			dir = Direction.getOpposite(dir);
		}
		return dir;
	} 
	
	public boolean hasDialog() {
		return dialogs.size() > 0;
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
	
	public ArrayList<Dialog> getDialogs(){
		return dialogs;
	}
	
	public boolean isInDialog() {
		return isInDialog;
	}
	
	public void setIsInDialog(boolean isInDialog) {
		this.isInDialog = isInDialog;
	}
		
	public void setGame(Game game) {
		this.game = game;
	}
		
	public int getId() {
		return id;
	}
	
	public ArrayList<Item> getItems() {
		return inventory.getItems();
	}

	public boolean isShopSeller() {
		return shopSeller;
	}

}
