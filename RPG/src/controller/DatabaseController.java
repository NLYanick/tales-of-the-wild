package controller;

import java.util.ArrayList;

import database.BuildingLayer;
import database.ItemLayer;
import database.NPCLayer;
import database.PlayerLayer;
import model.Building;
import model.Item;
import model.Location;
import model.NPC;
import model.Player;

public class DatabaseController {

	private MainController controller;
	
	private PlayerLayer playerLayer;
	private ItemLayer itemLayer;
	private NPCLayer npcLayer;
	private BuildingLayer buildingLayer;
	
	public DatabaseController(MainController controller) {
		this.controller = controller;
		
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
	
	public void loadPlayer(Player player) {
		Location playerLocation = getPlayerLocation(player.getName());
		player.setLocation(playerLocation);
		controller.setPlayer(player);
		player.addItemsToInventory(itemLayer.getAllItemsOfPlayer(player));
		controller.addPlayerItemViewsToInventoryView();
		controller.teleportPlayer(player.getLocation());
	}
	
	public Player createPlayer(String name) {
		Player player = new Player(Player.DEFAULT_URL, Player.DEFAULT_LOCATION, name);
		playerLayer.saveNewPlayer(player);
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
	
	public Location getOriginalNPCBuildingLocation(NPC npc) {
		return npcLayer.getOriginalNPCBuildingLocation(npc);
	}
	
	public Location getOriginalItemBuildingLocation(Item item) {
		return itemLayer.getOriginalItemBuildingLocation(item);
	}
	
	public ArrayList<Item> getAllItems(){
		return itemLayer.getAllItems();
	}
	
	public ArrayList<NPC> getAllNPCs(){
		return npcLayer.getAllNPCs();
	}
	
	public ArrayList<Building> getAllBuildings(){
		return buildingLayer.getAllBuildings();
	}
	
	public ArrayList<Item> getAllItemsOfPlayer(Player player){
		return itemLayer.getAllItemsOfPlayer(player);
	}
	
	public void setItemLocation(Item item, Location location) {
		itemLayer.setItemLocation(item, location);
	}

	public void addItemToPlayer(Item item, Player player) {
		itemLayer.addItemToPlayer(item, player);
	}
	
}
