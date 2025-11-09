package view.Buildings;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.BuildingTile;
import model.BuildingTileType;
import model.Direction;
import model.Location;
import model.Size;
import view.MainScene;
import view.NPCView;

public abstract class BuildingView extends BorderPane {
		
	protected final static int INSIDE_SPAWN_X = -2000;
	protected final static int INSIDE_SPAWN_Y = -1000;
	
	protected Size size;
	protected Direction exit;
	
	protected ArrayList<NPCView> npcViews;
	protected ArrayList<BuildingTile> tiles;
	
	protected MainScene scene;
	protected GridPane layout;
	
	public BuildingView(Size size, MainScene scene, Direction exit, ArrayList<BuildingTile> tiles, HashMap<String, String> tileSettings) {
		this.size = size;
		this.exit = exit;
		this.tiles = tiles;
		
		this.scene = scene;
		this.layout = new GridPane();
		
		setLocation();
		
		if(size.getHeight() % 2 != 0) {
			setLayoutY(getLayoutY() - 64);
		}
		
		setCenter(layout);
		
		setPrefSize(size.getWidth() * MainScene.STANDARD_IMAGE_SIZE, size.getHeight() * MainScene.STANDARD_IMAGE_SIZE);
	}
	
	private void setLocation() {
		int imgSize = MainScene.STANDARD_IMAGE_SIZE;
		int entranceSpacing = 64;
		switch(exit) { 
		case NORTH: 
			setLayoutX(scene.getWidth()/2 - (size.getWidth()/2 * imgSize));
			setLayoutY(scene.getHeight()/2 - entranceSpacing);
			break;
		case EAST: 
			setLayoutX(scene.getWidth()/2 - (size.getWidth() * imgSize) + entranceSpacing);
			setLayoutY(scene.getHeight()/2 - (size.getHeight()/2 * imgSize));
			break;
		case SOUTH: 
			setLayoutX(scene.getWidth()/2 - (size.getWidth()/2 * imgSize));
			setLayoutY(scene.getHeight()/2 - (size.getHeight() * imgSize) + entranceSpacing);
			break;
		case WEST: 
			setLayoutX(scene.getWidth()/2 - entranceSpacing);
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
	
	protected void loadTiles() {
		for(BuildingTile tile : tiles) {
			createImage(tile.getType(), new Location(tile.getX(), tile.getY()));
		}
	}
	
	protected abstract void createImage(BuildingTileType type, Location location);
	
}
