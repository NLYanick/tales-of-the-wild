package view.Buildings;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Building;
import model.Direction;
import model.Location;
import model.Size;
import view.MainScene;
import view.PlayerView;

public class BrickBuildingView extends BuildingView {
	
	private HashMap<ImageView, String> imagesWithType;
	
	public BrickBuildingView(Size size, MainScene scene, Direction exit) {
		super(size, scene, exit);
		
		imagesWithType = new HashMap<ImageView, String>();
		setUpLayout();
	}

	private void setUpLayout() {
		setPrefSize(size.getWidth() * 128, size.getHeight() * 128);
		
		for(int x = 0; x < size.getWidth(); x++) {
			for(int y = 0; y < size.getHeight(); y++) {
				if(isWall(x, y) && isXMiddle(x) && y == size.getHeight() - 1) {
					createImage("Floor", new Location(x, y));
				} else if(isWall(x, y)) {
					createImage("Wall", new Location(x, y));
				} else {
					createImage("Floor", new Location(x, y));
				}
			}
		}
	}
	
	private void createImage(String type, Location location) {
		int imgSize = 128;
		int buildingX = -Building.BUILDING_LOCATION.getX();
		int buildingY = -Building.BUILDING_LOCATION.getY();
		if(type.equals("Wall")) {
			String url = scene.getImageUrlByIndex(56);
			scene.addBuildingViewImage(url, false, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
			Image image = new Image(url);
			
			ImageView imageView = new ImageView(image);
			imagesWithType.put(imageView, type);
			layout.add(imageView, location.getX(), location.getY());
		} else if(type.equals("Floor")) {
			Rectangle redFloor = new Rectangle(128, 128, Color.RED);
			layout.add(redFloor, location.getX(), location.getY());
		}
	}
	

	public boolean locationIsOnWall(PlayerView playerView, Direction dir) {		
		for(Node node : layout.getChildren()) {
			double x = node.getLayoutX();
			double y = node.getLayoutY();
			for (Map.Entry<ImageView, String> entry : imagesWithType.entrySet()) {
				ImageView key = entry.getKey();
				if(key.getLayoutX() == x && key.getLayoutY() == y
					&& playerIsOnNode(x + getLayoutX(), y + getLayoutY(), playerView, dir)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
