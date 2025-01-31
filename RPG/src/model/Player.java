package model;

public class Player extends Entity {

	private Inventory inventory;
	
	public Player(String imgURL, Location location) {
		super(imgURL);
		setX(location.getX());
		setY(location.getY());
		
		inventory = new Inventory();
	}
	
	@Override
	public void setRunningImage(Direction dir) {
		switch(dir) {
		case NORTH:
			setImageURL("Images/Fox/FoxRunning.gif");
			break;
		case EAST:
			setImageURL("Images/Fox/FoxLeftRunning.gif");
			break;
		case SOUTH:
			setImageURL("Images/Fox/FoxBackRunning.gif");
			break;
		case WEST:
			setImageURL("Images/Fox/FoxRightRunning.gif");
			break;
		default:
			break;
		}
	}
	
	@Override
	public void setStandingStillAnimation(Direction dir) {
		switch(dir) {
		case NORTH:
			setImageURL("Images/Fox/FoxStandingStill.gif");
			break;
		case EAST:
			setImageURL("Images/Fox/FoxLeftStandingStill.gif");
			break;
		case SOUTH:
			setImageURL("Images/Fox/FoxBackStandingStill.gif");
			break;
		case WEST:
			setImageURL("Images/Fox/FoxRightStandingStill.gif");
			break;
		default:
			break;
		}
	}
	
	public void talkToNPC(NPC npc) {
		if(npc != null && npc.getDialog() != null) {
			npc.startDialog(movingDirection);
		}
	}

	public void addItemToInventory(Item item) {
		inventory.addItem(item);
	}
	
	public void removeItemFromInventory(Item item) {
		inventory.removeItem(item);
	}

	public boolean inventoryIsFull() {
		return inventory.isFull();
	}
	
}
