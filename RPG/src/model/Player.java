package model;

import java.util.ArrayList;

import view.MainScene;

public class Player extends Entity {

	public final static String DEFAULT_URL = "Images/Fox/FoxStandingStill.gif";
	public final static Location DEFAULT_LOCATION = new Location(MainScene.SCENEWIDTH/2, MainScene.SCENEHEIGHT/2);
	
	private Inventory inventory;
	
	public Player(String imgURL, Location location, String name, int gameId) {
		super(imgURL, name, gameId);
		setX(location.getX());
		setY(location.getY());
		
		movingDirection = Direction.SOUTH;
		
		inventory = new Inventory();
	}
	
	public void talkToNPC(NPC npc) {
		if(npc != null && npc.getDialogs() != null) {
			npc.startDialog(movingDirection);
		}
	}

	public void addItemToInventory(Item item) {
		inventory.addItem(item);
		item.resetLocation();
	}
	
	public void removeItemFromInventory(Item item) {
		inventory.removeItem(item);
	}

	public boolean inventoryIsFull() {
		return inventory.isFull();
	}
	
	public void addItemsToInventory(ArrayList<Item> items) {
		for(Item item : items) {
			addItemToInventory(item);
		}
	}
	
	public boolean nextStepIsNPC(Location npcLocation, Direction dir) {
		int lessVerticalPersonalSpace = 20;
		int lessHorizontalPersonalSpace = 10;
		int multi = 3;
		
		int width = 32;
		int height = 80;
				
		if(Location.isGreater(new Location(location.getX() + dir.getX(), location.getY() + dir.getY()), 
				new Location(npcLocation.getX() - lessHorizontalPersonalSpace - width, 
				npcLocation.getY() - lessVerticalPersonalSpace * multi))
			&& Location.isLess(new Location(location.getX() + dir.getX(), location.getY() + dir.getY()), 
				new Location(npcLocation.getX() + (int) (width * 1.5) - lessHorizontalPersonalSpace, 
				npcLocation.getY() + height - lessVerticalPersonalSpace * multi))) {
			return true;
		}
		
		return false;
	}
	
	public boolean isOnItem(Item item) {
		int extraSpace = 10;
		int itemWidth = Item.ITEMWIDTH;
		return Location.isGreater(location, new Location(item.getX() - extraSpace, item.getY() - extraSpace)) 
				&& Location.isLess(location, new Location(item.getX() + extraSpace + itemWidth, item.getY() + extraSpace + itemWidth));
	}
	
	public boolean xIsNearNPCX(NPC npc, Direction nextDirection, int multiplier) {
		return (getX() >= npc.getX() + nextDirection.getX() * multiplier) 
		&& (getX() <= npc.getX() - nextDirection.getX() * multiplier);
	}
	
	public boolean yIsNearNPCY(NPC npc, Direction nextDirection, int multiplier) {
		return (getY() >= npc.getY() - nextDirection.getY() * multiplier) 
				&& (getY() <= npc.getY() + nextDirection.getY() * multiplier);
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
	
	// ----- Getters & Setters -----
	
	public Item getInventoryItemWithId(int id) {
		return inventory.getItemWithId(id);
	}
	
	public ArrayList<Item> getItemsOfInventory() {
		return inventory.getItems();
	}
	
}
