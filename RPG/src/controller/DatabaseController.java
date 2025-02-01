package controller;

import database.PlayerLayer;
import model.Location;
import model.Player;

public class DatabaseController {

	private MainController controller;
	
	private PlayerLayer playerLayer;
	
	public DatabaseController(MainController controller) {
		this.controller = controller;
		
		playerLayer = new PlayerLayer();
	}
	
	public void saveGame(Player player) {
		playerLayer.saveLocation(player);
	}
	
	public Location getPlayerLocation(String name) {
		return playerLayer.getLocation(name);
	}
	
}
