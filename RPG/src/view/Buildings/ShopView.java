package view.Buildings;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Direction;
import model.Location;
import model.Size;
import view.MainScene;

public class ShopView extends BuildingView {

	private final static int INSIDE_SPAWN_X = -2000;
	private final static int INSIDE_SPAWN_Y = -1000;
	
	private Color color;
	
	public ShopView(Size size, MainScene scene, Direction exit, Color color) {
		super(size, scene, exit);
		this.color = color;
		
		setUpLayout();
	}
	
	private void setUpLayout() {
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
	
	protected void createImage(String type, Location location) {
		int imgSize = MainScene.STANDARD_IMAGE_SIZE;
		int buildingX = -INSIDE_SPAWN_X;
		int buildingY = -INSIDE_SPAWN_Y;
		if(type.equals("Wall")) {
			Rectangle wall = new Rectangle(imgSize, imgSize, color);
			layout.add(wall, location.getX(), location.getY());
			wall.toBack();
		} else if(type.equals("Floor")) {
			String url = scene.getImageUrlByIndex(57);
			scene.addBuildingViewImage(url, false, new Location(location.getX() * imgSize - buildingX, location.getY() * imgSize - buildingY));
			Image image = new Image(url);
			
			ImageView imageView = new ImageView(image);
			imagesWithType.put(imageView, type);
			layout.add(imageView, location.getX(), location.getY());
			imageView.toBack();
		}
	}

}
