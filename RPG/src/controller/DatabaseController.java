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
import model.ShopItem;

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
		buildingLayer = new BuildingLayer();
	}
	
	public void saveGame(Player player) {
		playerLayer.saveLocation(player);
		itemLayer.savePlayerItems(player, player.getItems());
	}
	
	public Location getPlayerLocation(String name) {
		return playerLayer.getLocation(name);
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
		
		game.startGame();
		controller.teleportPlayer(player.getLocation());
	}
	
	private Game createGame() {
		return gameLayer.createGame();
	}
	
	public Player createPlayer(String name) {
		Game newGame = createGame();
		
		Player player = new Player(Player.DEFAULT_URL, Player.DEFAULT_LOCATION, name, 0);
		playerLayer.saveNewPlayer(player, newGame.getId());
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
	
	public Location getOriginalNPCBuildingLocation(NPC npc, int buildingId) {
		return npcLayer.getOriginalNPCBuildingLocation(npc, buildingId);
	}
	
	public Location getOriginalItemBuildingLocation(Item item, int buildingId) {
		return itemLayer.getOriginalItemBuildingLocation(item, buildingId);
	}
	
	public ArrayList<Item> getAllWorldItems(int gameId){
		return itemLayer.getAllWorldItems(gameId);
	}
	
	public ArrayList<NPC> getAllNPCs(int gameId){
		return npcLayer.getAllNPCs(gameId);
	}
	
	public ArrayList<Building> getAllBuildings(int gameId){
		return buildingLayer.getAllBuildings(gameId);
	}
	
	public ArrayList<Item> getNPCItems(NPC npc){
		return itemLayer.getNPCItems(npc);
	}
	
	public ArrayList<Item> getBuildingItems(int buildingId){
		return itemLayer.getBuildingItems(buildingId);
	}
	
	public ArrayList<ShopItem> getShopItems(int shopId) {
		return itemLayer.getShopItems(shopId);
	}
	
	public ArrayList<NPC> getBuildingNPCs(int buildingId) {
		return npcLayer.getBuildingNPCs(buildingId);
	}
	
	public void setItemLocation(Item item, Location location) {
		itemLayer.setItemLocation(item, location);
	}
	
	public void dropItem(Item item, Location location) {
		itemLayer.dropItem(item, location);
	}
	
	public void dropItemInBuilding(Item item, Location location, int buildingId) {
		itemLayer.dropItemInBuilding(item, location, buildingId);
	}

	public void addItemToPlayer(Item item, Player player) {
		itemLayer.addItemToPlayer(item, player);
	}
	
	public void saveNPCDialog(NPC npc) {
		npcLayer.saveDialogs(npc);
	}
	
	public void removeItemFromNPC(Item item) {
		itemLayer.removeItemFromNPC(item);
	}
	
	public void removeItemFromBuilding(Item item) {
		itemLayer.removeItemFromBuilding(item);
	}
	
}
