package model;

public class BuildingTile extends Tile {
	
	private BuildingTileType type;
	private boolean spawn; 

	public BuildingTile(BuildingTileType type, Location location, boolean spawn) {
		super(location, getCanWalkOn(type));
		this.type = type; 
		this.spawn = spawn; 
	}

	private static boolean getCanWalkOn(BuildingTileType type) {
		switch (type) {
		case WALL:
		case COUNTER:
			return false;
		case FLOOR:
		case EXIT:
			return true;
		default:
			return false;
		}
	}

	public BuildingTileType getType() {
		return type;
	}
	
	public boolean isSpawn() {
		return spawn;
	}
		
}
