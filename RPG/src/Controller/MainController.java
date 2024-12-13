package Controller;

import Model.BackgroundLocation;
import Model.Direction;
import Model.Player;
import View.MainScene;

public class MainController {

	private BackgroundLocation backgroundLocation;
	private Player player;
	
	private MainScene scene;
	
	public MainController() {
		
		player = new Player("Images/FoxStandingStill.gif");
		backgroundLocation = new BackgroundLocation(0, 0);
		
		scene = new MainScene(this);
		
	}

	public MainScene getMainScene() {
		return scene;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getPlayerURL() {
		return player.getURL();
	}
	
	public void moveBackground(Direction dir) {
		backgroundLocation.move(dir);
		scene.moveBackground(backgroundLocation.getX(), backgroundLocation.getY());
		
		setPlayerImage(dir);
		
	}
	
	public void setPlayerStandingStillAnimation(Direction dir) {
		switch(dir) {
		case NORTH:
			player.setImageURL("Images/FoxStandingStill.gif");
			break;
		case EAST:
			player.setImageURL("Images/FoxLeft.png");
			break;
		case SOUTH:
			player.setImageURL("Images/FoxBackStandingStill.gif");
			break;
		case WEST:
			player.setImageURL("Images/FoxRight.png");
			break;
		default:
			break;
		}
		
		scene.changePlayerImage();
	}
	
	public void setPlayerImage(Direction dir) {
		switch(dir) {
		case NORTH:
			player.setImageURL("Images/Fox.png");
			break;
		case EAST:
			player.setImageURL("Images/FoxLeft.png");
			break;
		case SOUTH:
			player.setImageURL("Images/FoxBack.png");
			break;
		case WEST:
			player.setImageURL("Images/FoxRight.png");
			break;
		default:
			break;
		}
		scene.changePlayerImage();
	}
}
