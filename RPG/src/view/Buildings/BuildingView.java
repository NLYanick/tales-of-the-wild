package view.Buildings;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Direction;
import model.Size;
import view.MainScene;
import view.PlayerView;

public abstract class BuildingView extends BorderPane {
	
	private final static int EXTRA_In_BUILDING = 32;
	
	protected Size size;
	protected Direction exit;
	
	protected HashMap<ImageView, String> imagesWithType;
	
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
		switch(exit) {
		case NORTH: 
			setLayoutX(scene.getWidth()/2 - (size.getWidth()/2 * 128));
			setLayoutY(scene.getHeight()/2 - EXTRA_In_BUILDING);
			break;
		case EAST: 
			setLayoutX(scene.getWidth()/2 - EXTRA_In_BUILDING);
			setLayoutY(scene.getHeight()/2 - (size.getHeight()/2 * 128));
			break;
		case SOUTH: 
			setLayoutX(scene.getWidth()/2 - (size.getWidth()/2 * 128));
			setLayoutY(scene.getHeight()/2 - (size.getHeight() * 128) + EXTRA_In_BUILDING);
			break;
		case WEST: 
			setLayoutX(scene.getWidth()/2 - (size.getWidth() * 128) + EXTRA_In_BUILDING);
			setLayoutY(scene.getHeight()/2 - (size.getHeight()/2 * 128));
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
	
	public boolean nextStepIsOnExit(Direction dir, PlayerView playerView) {
		boolean playerIsOnNode = false;
		
		for(Node node : layout.getChildren()) {
			if(playerIsOnNode(node.getLayoutX() + getLayoutX(), node.getLayoutY() + getLayoutY(), playerView, dir)) {
				playerIsOnNode = true;
				break;
			}
		}
		
		return !playerIsOnNode;
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
	
	protected boolean playerIsOnNode(double x, double y, PlayerView playerView, Direction dir) {
		double playerX = playerView.getLayoutX();
		double playerY = playerView.getLayoutY();
		if(dir == Direction.EAST) {
			playerX += playerView.getBoundsInParent().getWidth()/3;
		}
		if(dir == Direction.SOUTH) {
			playerY += playerView.getBoundsInParent().getHeight()/4;
		}
		return x - 32 <= playerX + dir.getX() && x - 32 + 127 >= playerX + dir.getX()
		&& y - 32 <= playerY + dir.getY() && y - 32 + 127 >= playerY + dir.getY();
	}
	
	protected boolean isOnExit(int x, int y) {
		switch(exit) {
			case NORTH: 
				return isXMiddle(x) && y == 0;
			case EAST: 
				return isYMiddle(y) && x == 0;
			case SOUTH: 
				return isXMiddle(x) && y == size.getHeight() - 1;
			case WEST: 
				return isYMiddle(y) && x == size.getWidth() - 1;
			default: return false;
		}
	}
	
}
