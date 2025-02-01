package controller;

import java.util.ArrayList;

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
	
	public ArrayList<Player> getAllPlayers(){
		return playerLayer.getAllPlayers();
	}
	
	public void loadPlayer(Player player) {
		Location playerLocation = getPlayerLocation(player.getName());
		player.setLocation(playerLocation);
		controller.teleportPlayer(player.getLocation());
	}
	
	public Player createPlayer(String name) {
		Player player = new Player(Player.DEFAULT_URL, Player.DEFAULT_LOCATION, name);
		playerLayer.saveNewPlayer(player);
		return player;
	}
	
	public boolean nameIsUnique(String name) {
		for(String playerName : playerLayer.getAllPlayerNames()) {
			if(name.equals(playerName)) {
				return false;
			}
		}
		return true;
	}
	
}
