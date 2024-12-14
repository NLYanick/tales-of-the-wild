package Controller;

import Model.BackgroundLocation;
import Model.Direction;
import Model.Player;
import View.MainScene;

public class MainController {

	private BackgroundLocation backgroundLocation;
	private Player player;
	
	private MainScene scene;
	
	private boolean imageIsRunning = true;
	
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
		
//		imageIsRunning = true;
		setPlayerImage(dir);
		
	}
	
	public void setPlayerStandingStillAnimation(Direction dir) {
		switch(dir) {
		case NORTH:
			player.setImageURL("Images/FoxStandingStill.gif");
			break;
		case EAST:
			player.setImageURL("Images/FoxLeftStandingStill.gif");
			break;
		case SOUTH:
			player.setImageURL("Images/FoxBackStandingStill.gif");
			break;
		case WEST:
			player.setImageURL("Images/FoxRightStandingStill.gif");
			break;
		default:
			break;
		}
		
		imageIsRunning = true;
		scene.changePlayerImage();
	}
	
	public void setPlayerImage(Direction dir) {
		if(imageIsRunning) {
			switch(dir) {
			case NORTH:
				player.setImageURL("Images/FoxRunning.gif");
				break;
			case EAST:
				player.setImageURL("Images/FoxLeftRunning.gif");
				break;
			case SOUTH:
				player.setImageURL("Images/FoxBackRunning.gif");
				break;
			case WEST:
				player.setImageURL("Images/FoxRightRunning.gif");
				break;
			default:
				break;
			}
			scene.changePlayerImage();
		} 
		imageIsRunning = false;
	}
}
