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

public class BrickBuildingView extends BuildingView {
	
	public BrickBuildingView(Size size, MainScene scene, Direction exit, ArrayList<BuildingTile> tiles, 
			HashMap<String, String> tileSettings, Location spawnLocation) {
		super(size, scene, exit, tiles, tileSettings, spawnLocation);
		
		loadTiles();
	}
	
	protected void createTile(BuildingTileType type, Location location) {
		int imgSize = MainScene.STANDARD_IMAGE_SIZE;
		int buildingX = -INSIDE_SPAWN_X;
		int buildingY = -INSIDE_SPAWN_Y;
		
		switch (type) {
		case WALL:
			String url = scene.getImageUrlByIndex(56);
			scene.addBuildingViewImage(url, false, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
			Image image = new Image(url);
			
			ImageView imageView = new ImageView(image);
			layout.add(imageView, location.getX(), location.getY());
			imageView.toBack();
			break;
		case EXIT:
		case FLOOR:
			Rectangle floor = new Rectangle(imgSize, imgSize, Color.rgb(204, 65, 37));
			layout.add(floor, location.getX(), location.getY());
			floor.toBack();
			break;
			
		default:
			createTile(BuildingTileType.FLOOR, location);
		}
	}
	
}
