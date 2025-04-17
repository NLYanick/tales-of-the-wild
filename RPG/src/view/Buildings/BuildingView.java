package view.Buildings;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Direction;
import model.Size;
import view.MainScene;
import view.NPCView;

public abstract class BuildingView extends BorderPane {
		
	protected Size size;
	protected Direction exit;
	
	protected HashMap<ImageView, String> imagesWithType;
	protected ArrayList<NPCView> npcViews;
	
	protected MainScene scene;
	protected GridPane layout;
	
	public BuildingView(Size size, MainScene scene, Direction exit) {
		this.size = size;
		this.exit = exit;
		
		this.scene = scene;
		this.layout = new GridPane();
		imagesWithType = new HashMap<ImageView, String>();
		
		setLocation();
		
		if(size.getWidth() % 2 != 0) {
			setLayoutX(getLayoutX() - 64);
		}
		
		setCenter(layout);
	}
	
	private void setLocation() {
		int imgSize = 128;
		switch(exit) { 
		case NORTH: 
			setLayoutX(scene.getWidth()/2 - (size.getWidth()/2 * imgSize));
			setLayoutY(scene.getHeight()/2);
			break;
		case EAST: 
			setLayoutX(scene.getWidth()/2 - (size.getWidth() * imgSize));
			setLayoutY(scene.getHeight()/2 - (size.getHeight()/2 * imgSize));
			break;
		case SOUTH: 
			setLayoutX(scene.getWidth()/2 - (size.getWidth()/2 * imgSize));
			setLayoutY(scene.getHeight()/2 - (size.getHeight() * imgSize));
			break;
		case WEST: 
			setLayoutX(scene.getWidth()/2);
			setLayoutY(scene.getHeight()/2 - (size.getHeight()/2 * imgSize));
			break;
			default: return;
		}
	}
	
	public void move(Direction dir) {
		setLayoutX(getLayoutX() + dir.getX());
		setLayoutY(getLayoutY() + dir.getY());
	}
	
	public void move(int x, int y) {
		setLayoutX(x);
		setLayoutY(y);
	}
	
	protected boolean isWall(int x, int y) {
		return x == 0 || y == 0 || x == size.getWidth() - 1 || y == size.getHeight() - 1;
	}
	
	protected boolean isXMiddle(int x) {
		int width = size.getWidth();
		return (width % 2 != 0 && x + 1 == width/2 + 1) ||
			   (width % 2 == 0 && (x + 1 == width/2 || x + 1 == width/2 + 1));
	}
	
	protected boolean isYMiddle(int y) {
		int height = size.getHeight();
		return (height % 2 != 0 && y + 1 == height/2 + 1) ||
			   (height % 2 == 0 && (y + 1 == height/2 || y + 1 == height/2 + 1));
	}
	
	protected boolean isOnExit(int x, int y) {
		switch(exit) {
			case NORTH: 
				return isXMiddle(x) && y == 0;
			case EAST: 
				return isYMiddle(y) && x == size.getWidth() - 1;
			case SOUTH: 
				return isXMiddle(x) && y == size.getHeight() - 1;
			case WEST: 
				return isYMiddle(y) && x == 0;
			default: return false;
		}
	}
	
}
