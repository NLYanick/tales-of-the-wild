package view.Buildings;

import java.util.ArrayList;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Building;
import model.BuildingTile;
import model.BuildingTileType;
import model.Direction;
import model.Location;
import view.MainScene;
import view.NPCView;

public abstract class BuildingView extends BorderPane {
		
	protected final static int INSIDE_SPAWN_X = -2000;
	protected final static int INSIDE_SPAWN_Y = -1000;
	
	protected Location spawnLocation;
	protected int tileSize;
	
	protected ArrayList<NPCView> npcViews;
	protected ArrayList<BuildingTile> tiles;
	
	protected MainScene scene;
	protected GridPane layout;
	
	public BuildingView(MainScene scene, ArrayList<BuildingTile> tiles, int tileSize, Location spawnLocation) {
		this.tiles = tiles;
		this.tileSize = tileSize;
		this.spawnLocation = spawnLocation;
		
		this.scene = scene;
		this.layout = new GridPane();
		
		setLocation();
		
		setCenter(layout);
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
