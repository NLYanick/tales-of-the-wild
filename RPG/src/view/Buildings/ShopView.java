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

public class ShopView extends BuildingView {

	private Color color;
	private int floorPattern;
	
	public ShopView(MainScene scene, ArrayList<BuildingTile> tiles, int tileSize, Color color, int floorPattern, Location spawnLocation) {
		super(scene, tiles, tileSize, spawnLocation);
		this.color = color;
		this.floorPattern = floorPattern;
		
		loadTiles();
	}
	
	protected void createTile(BuildingTileType type, Location location) {
		int imgSize = tileSize;
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
			String floorUrl = scene.getImageUrlByIndex(57 + floorPattern);
			scene.addBuildingViewImage(floorUrl, true, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
			Image floor = new Image(floorUrl);
			
			ImageView floorView = new ImageView(floor);
			floorView.setFitWidth(imgSize);
			floorView.setFitHeight(imgSize);
			layout.add(floorView, location.getX(), location.getY());
			floorView.toBack();
			break;
		case COUNTER:
//			Rectangle counter = new Rectangle(imgSize, imgSize, Color.BLACK);
//			layout.add(counter, location.getX(), location.getY());
//			counter.toBack();
			String counterUrl = "Images/Background/Building/TempCounter.png";
			scene.addBuildingViewImage(counterUrl, true, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
			Image counter = new Image(counterUrl);
			
			ImageView counterView = new ImageView(counter);
			counterView.setFitWidth(imgSize);
			counterView.setFitHeight(imgSize);
			layout.add(counterView, location.getX(), location.getY());
			counterView.toBack();
			break;
		default:
			createTile(BuildingTileType.FLOOR, location);
		}
	}

}
