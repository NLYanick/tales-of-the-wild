package model;

import javafx.scene.paint.Color;

public class Shop extends Building {

	private Inventory inventory;
	private Color color;
	
	public Shop(Location insideLocation, boolean canPass, Size size, BuildingType type, Direction exit,
			Location leaveLocation, int id, Location entranceLocation, Color color) {
		super(insideLocation, canPass, size, type, exit, leaveLocation, id, entranceLocation);
		
		inventory = new Inventory();
		this.color = color;
	}
	
	public void buy(Item item) {
		// if(player.currency > item.cost)
		inventory.removeItem(item);
		// player.addItem(item)
	}
	
	public void addShopItem(Item item) {
		inventory.addItem(item);
	}
	
	public Color getColor() {
		return color;
	}

}
