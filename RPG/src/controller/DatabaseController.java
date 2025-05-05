package controller;

import java.util.ArrayList;

import database.BuildingLayer;
import database.GameLayer;
import database.ItemLayer;
import database.NPCLayer;
import database.PlayerLayer;
import model.Building;
import model.Game;
import model.Item;
import model.Location;
import model.NPC;
import model.Player;

public class DatabaseController {

	private MainController controller;
	
	private GameLayer gameLayer;
	private PlayerLayer playerLayer;
	private ItemLayer itemLayer;
	private NPCLayer npcLayer;
	private BuildingLayer buildingLayer;
	
	public DatabaseController(MainController controller) {
		this.controller = controller;
		
		gameLayer = new GameLayer();
		playerLayer = new PlayerLayer();
		itemLayer = new ItemLayer();
		npcLayer = new NPCLayer();
		buildingLayer = new BuildingLayer(controller);
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
	
	public ArrayList<String> getAllPlayerNames(){
		return playerLayer.getAllPlayerNames();
	}
	
	public void loadGame(String playerName) {
		Game game = gameLayer.getGameByPlayerName(playerName);
		Player player = playerLayer.getPlayer(playerName);
		
		game.setPlayer(player);
		controller.setGame(game);
		
		controller.setPlayer(player);
		player.addItemsToInventory(itemLayer.getAllItemsOfPlayer(player));
		
		controller.startGame();
		controller.teleportPlayer(player.getLocation());
	}
	
	private Game createGame() {
		return gameLayer.createGame();
	}
	
	public Player createPlayer(String name) {
		Game newGame = createGame();
		
		Player player = new Player(Player.DEFAULT_URL, Player.DEFAULT_LOCATION, name, newGame.getId());
		playerLayer.saveNewPlayer(player);
		controller.setGame(newGame);
		controller.setPlayer(player);
		
		return player;
	}
	
	public void deletePlayer(String playerName) {
		playerLayer.deletePlayer(playerName);
	}
	
	public boolean nameIsUnique(String name) {
		for(String playerName : playerLayer.getAllPlayerNames()) {
			if(name.equals(playerName)) {
				return false;
			}
		}
		return true;
	}
	
	public Location getOriginalNPCBuildingLocation(NPC npc) {
		return npcLayer.getOriginalNPCBuildingLocation(npc);
	}
	
	public Location getOriginalItemBuildingLocation(Item item) {
		return itemLayer.getOriginalItemBuildingLocation(item);
	}
	
	public ArrayList<Item> getAllItems(int gameId){
		return itemLayer.getAllItems(gameId);
	}
	
	public ArrayList<NPC> getAllNPCs(int gameId){
		return npcLayer.getAllNPCs(gameId);
	}
	
	public ArrayList<Building> getAllBuildings(int gameId){
		return buildingLayer.getAllBuildings(gameId);
	}
	
	public ArrayList<Item> getAllItemsOfPlayer(Player player){
		return itemLayer.getAllItemsOfPlayer(player);
	}
	
	public void setItemLocation(Item item, Location location) {
		itemLayer.setItemLocation(item, location);
	}
	
	public void dropItem(Item item, Location location) {
		itemLayer.dropItem(item, location);
	}

	public void addItemToPlayer(Item item, Player player) {
		itemLayer.addItemToPlayer(item, player);
	}
	
}
