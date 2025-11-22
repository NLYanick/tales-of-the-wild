package view.Buildings;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.BuildingTile;
import model.BuildingTileType;
import model.Location;
import view.MainScene;

public class TentView extends BuildingView {

	private Color color;
	
	public TentView(MainScene scene, Color color, ArrayList<BuildingTile> tiles, int tileSize, Location spawnLocation) {
		super(scene, tiles, tileSize, spawnLocation);
		this.color = color;
		
		loadTiles();
	}
		
	protected void createTile(BuildingTileType type, Location location) {
		int imgSize = tileSize;
		int buildingX = -INSIDE_SPAWN_X;
		int buildingY = -INSIDE_SPAWN_Y;
		
		switch (type) {
		case WALL:
//			String wallUrl = scene.getImageUrlByIndex(56);
//			scene.addBuildingViewImage(wallUrl, false, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
//			
//			createImage(wallUrl, location);
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
			createTile(BuildingTileType.FLOOR, location);
		}
	}
	
	private ImageView createImage(String url, Location location) {
		Image image = new Image(url);
		
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(tileSize);
		imageView.setFitHeight(tileSize);
		
		layout.add(imageView, location.getX(), location.getY());
		imageView.toBack();
		
		return imageView;
	}

}
