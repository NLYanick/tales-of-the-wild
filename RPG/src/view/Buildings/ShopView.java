package view.Buildings;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.BuildingTile;
import model.BuildingTileType;
import model.Direction;
import model.Location;
import model.Size;
import view.MainScene;

public class ShopView extends BuildingView {

	private Color color;
	private int floorPattern;
	
	public ShopView(Size size, MainScene scene, Direction exit, ArrayList<BuildingTile> tiles, HashMap<String, String> tileSettings, Color color, 
			int floorPattern, Location spawnLocation) {
		super(size, scene, exit, tiles, tileSettings, spawnLocation);
		this.color = color;
		this.floorPattern = floorPattern;
		
		loadTiles();
	}
	
	protected void createImage(BuildingTileType type, Location location) {
		int imgSize = MainScene.STANDARD_IMAGE_SIZE;
		int buildingX = -INSIDE_SPAWN_X;
		int buildingY = -INSIDE_SPAWN_Y;
		
		switch (type) {
		case WALL:
			Rectangle wall = new Rectangle(imgSize, imgSize, color);
			layout.add(wall, location.getX(), location.getY());
			wall.toBack();
			break;
		case EXIT:
		case FLOOR:
			String url = scene.getImageUrlByIndex(57 + floorPattern);
			scene.addBuildingViewImage(url, true, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
			Image image = new Image(url);
			
			ImageView imageView = new ImageView(image);
			layout.add(imageView, location.getX(), location.getY());
			imageView.toBack();
			break;
		case COUNTER:
			Rectangle counter = new Rectangle(imgSize, imgSize, Color.BLACK);
			layout.add(counter, location.getX(), location.getY());
			counter.toBack();
			break;
		default:
			createImage(BuildingTileType.FLOOR, location);
		}
	}

}
