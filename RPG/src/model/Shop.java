package model;

import controller.MainController;
import javafx.scene.paint.Color;

public class Shop extends Building {

	private Inventory inventory;
	private Color color;
	
	public Shop(Location insideLocation, boolean canPass, Size size, BuildingType type, Direction exit,
			Location leaveLocation, MainController controller, int id, Location entranceLocation, Color color) {
		super(insideLocation, canPass, size, type, exit, leaveLocation, controller, id, entranceLocation);
		
		inventory = new Inventory();
		this.color = color;
	}
	
	public void buy() {
		
	}
	
	public void addShopItem(Item item) {
		inventory.addItem(item);
	}
	
	public Color getColor() {
		return color;
	}

}
