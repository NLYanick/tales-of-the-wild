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

public class TentView extends BuildingView {

	private Color color;
	
	public TentView(Size size, MainScene scene, Direction exit, Color color, ArrayList<BuildingTile> tiles, 
			HashMap<String, String> tileSettings, Location spawnLocation) {
		super(size, scene, exit, tiles, tileSettings, spawnLocation);
		this.color = color;
		
		loadTiles();
	}
		
	protected void createImage(BuildingTileType type, Location location) {
		int imgSize = MainScene.STANDARD_IMAGE_SIZE;
		int buildingX = -INSIDE_SPAWN_X;
		int buildingY = -INSIDE_SPAWN_Y;
		
		switch (type) {
		case WALL:
//			String url = scene.getImageUrlByIndex(56);
//			scene.addBuildingViewImage(url, false, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
//			Image image = new Image(url);
//			
//			ImageView imageView = new ImageView(image);
//			imagesWithType.put(imageView, type);
//			layout.add(imageView, location.getX(), location.getY());
//			imageView.toBack();
			Rectangle wall = new Rectangle(imgSize, imgSize, Color.DODGERBLUE);
			layout.add(wall, location.getX(), location.getY());
			wall.toBack();
			break;
		case FLOOR:
			Rectangle floor = new Rectangle(imgSize, imgSize, color);
			layout.add(floor, location.getX(), location.getY());
			floor.toBack();
			break;
			
		default:
			createImage(BuildingTileType.FLOOR, location);
		}
	}

}
