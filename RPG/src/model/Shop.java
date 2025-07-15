package model;

import controller.MainController;

public class Shop extends Building {

	public Shop(Location insideLocation, boolean canPass, Size size, BuildingType type, Direction exit,
			Location leaveLocation, MainController controller, int id, Location entranceLocation) {
		super(insideLocation, canPass, size, type, exit, leaveLocation, controller, id, entranceLocation);
	}

}
