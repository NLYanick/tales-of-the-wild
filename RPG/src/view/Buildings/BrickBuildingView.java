package view.Buildings;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Building;
import model.Direction;
import model.Location;
import model.Size;
import view.MainScene;

public class BrickBuildingView extends BuildingView {
	
	public BrickBuildingView(Size size, MainScene scene, Direction exit) {
		super(size, scene, exit);
		
		setUpLayout();
	}

	private void setUpLayout() {
		setPrefSize(size.getWidth() * 128, size.getHeight() * 128);
				
		for(int x = 0; x < size.getWidth(); x++) {
			for(int y = 0; y < size.getHeight(); y++) { 
				if(isWall(x, y) && isOnExit(x, y)) {
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
		int buildingX = -Building.INSIDE_SPAWN_X;
		int buildingY = -Building.INSIDE_SPAWN_Y;
		if(type.equals("Wall")) {
			String url = scene.getImageUrlByIndex(56);
			scene.addBuildingViewImage(url, false, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
			Image image = new Image(url);
			
			ImageView imageView = new ImageView(image);
			imagesWithType.put(imageView, type);
			layout.add(imageView, location.getX(), location.getY());
			imageView.toBack();
		} else if(type.equals("Floor")) {
			Rectangle redFloor = new Rectangle(imgSize, imgSize, Color.RED);
			layout.add(redFloor, location.getX(), location.getY());
			redFloor.toBack();
		}
	}
	
}
