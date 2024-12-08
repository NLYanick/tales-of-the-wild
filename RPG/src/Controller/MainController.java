package Controller;

import Model.Direction;
import Model.Player;
import View.MainScene;
import View.PlayerView;

public class MainController {

	private Player player;
	private PlayerView playerView;
	
	private MainScene scene;
	
	public MainController() {
		scene = new MainScene(this);
		
		player = new Player();
	}

	public MainScene getMainScene() {
		return scene;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void movePlayer(Direction dir) {
		
		player.move(dir);
		if(player.getX() < 0) {
			player.move(Direction.EAST);
		}
		if(player.getY() < 0) {
			player.move(Direction.SOUTH);
		}
		scene.movePlayerView(player.getX(), player.getY());
		System.out.println(player.getX() + " | " +  player.getY());
	}
}
