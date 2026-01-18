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
import view.ShopInventoryView;

public class ShopView extends BuildingView {

	private Color color;
	private int floorPattern;
	
	private ShopInventoryView inventoryView;
	
	public ShopView(MainScene scene, ArrayList<BuildingTile> tiles, int tileSize, Color color, int floorPattern, Location spawnLocation) {
		super(scene, tiles, tileSize, spawnLocation);
		this.color = color;
		this.floorPattern = floorPattern;
		
		inventoryView = new ShopInventoryView(scene);
		
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
			String floorUrl = scene.getImageUrlByIndex(58 + floorPattern);
			scene.addBuildingViewImage(floorUrl, true, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
			
			createImage(floorUrl, location);
			break;
		case COUNTER:
			String counterUrl = scene.getImageUrlByIndex(61);
			scene.addBuildingViewImage(counterUrl, true, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
			
			createImage(counterUrl, location);
			
			// Put a floor beneath it
			String floorUnderUrl = scene.getImageUrlByIndex(58 + floorPattern);
			scene.addBuildingViewImage(floorUnderUrl, true, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
			
			createImage(floorUnderUrl, location);
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

	public ShopInventoryView getInventoryView() {
		return inventoryView;
	}

}
