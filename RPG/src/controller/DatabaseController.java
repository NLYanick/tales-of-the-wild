package controller;

import java.util.ArrayList;

import database.ItemLayer;
import database.PlayerLayer;
import model.Item;
import model.Location;
import model.Player;

public class DatabaseController {

	private MainController controller;
	
	private PlayerLayer playerLayer;
	private ItemLayer itemLayer;
	
	public DatabaseController(MainController controller) {
		this.controller = controller;
		
		playerLayer = new PlayerLayer();
		itemLayer = new ItemLayer();
	}
	
	public void saveGame(Player player) {
		playerLayer.saveLocation(player);
		itemLayer.savePlayerItems(player, player.getItemsOfInventory());
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
		controller.setPlayer(player);
		controller.teleportPlayer(player.getLocation());
	}
	
	public Player createPlayer(String name) {
		Player player = new Player(Player.DEFAULT_URL, Player.DEFAULT_LOCATION, name);
		playerLayer.saveNewPlayer(player);
		player.addItemsToInventory(itemLayer.getAllItemsOfPlayer(player));
		controller.setPlayer(player);
		return player;
	}
	
	public void deletePlayer(Player player) {
		playerLayer.deletePlayer(player);
	}
	
	public boolean nameIsUnique(String name) {
		for(String playerName : playerLayer.getAllPlayerNames()) {
			if(name.equals(playerName)) {
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<Item> getAllItems(){
		return itemLayer.getAllItems();
	}
	
	public ArrayList<Item> getAllItemsOfPlayer(Player player){
		return itemLayer.getAllItemsOfPlayer(player);
	}
	
}
