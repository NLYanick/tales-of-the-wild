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
		
		player = new Player("Images/Fox.png");
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
	}
}
