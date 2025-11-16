package view.Buildings;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Building;
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
	protected Location spawnLocation;
	
	protected ArrayList<NPCView> npcViews;
	protected ArrayList<BuildingTile> tiles;
	
	protected MainScene scene;
	protected GridPane layout;
	
	public BuildingView(Size size, MainScene scene, ArrayList<BuildingTile> tiles, HashMap<String, String> tileSettings, Location spawnLocation) {
		this.size = size;
		this.tiles = tiles;
		this.spawnLocation = spawnLocation;
		
		this.scene = scene;
		this.layout = new GridPane();
		
		setLocation();
		
		setCenter(layout);
		setPrefSize(size.getWidth() * MainScene.STANDARD_IMAGE_SIZE, size.getHeight() * MainScene.STANDARD_IMAGE_SIZE);
	}
	
	private void setLocation() {
		Location startLocation = Building.INSIDE_LOCATION;
		
		setLayoutX(scene.getWidth()/2 + (startLocation.getX() - spawnLocation.getX()));
		setLayoutY(scene.getHeight()/2 + (startLocation.getY() - spawnLocation.getY()));
	}
	
	public void move(Direction dir) {
		setLayoutX(getLayoutX() + dir.getX());
		setLayoutY(getLayoutY() + dir.getY());
	}
	
	public void move(int x, int y) {
		setLayoutX(x);
		setLayoutY(y);
	}
	
	protected void loadTiles() {
		for(BuildingTile tile : tiles) {
			createTile(tile.getType(), new Location(tile.getX(), tile.getY()));
		}
	}
	
	protected abstract void createTile(BuildingTileType type, Location location);
	
}
