package Model;

public class Player extends Entity {

	public Player(String imgURL, Location location) {
		super(imgURL);
		setX(location.getX());
		setY(location.getY());
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
	
	public boolean nextStepIsNPC(NPC npc) {
		int multiplier = 5;
		int extraSpace = Direction.getAmount() * multiplier;
		
		Location nextLocation = location.getNext(movingDirection);
		Location npcLocation = npc.getLocation();
		
		if(Location.isSame(nextLocation, npcLocation)
			|| (Location.isLess(nextLocation, new Location(npcLocation.getX() + extraSpace, npcLocation.getY() +extraSpace)) 
			&& Location.isGreater(nextLocation, new Location(npcLocation.getX() - extraSpace, npcLocation.getY() - extraSpace)))) {
			return true;
		}
		return false;
	}
	
}
