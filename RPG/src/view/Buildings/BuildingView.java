package view.Buildings;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Direction;
import model.Size;
import view.MainScene;
import view.PlayerView;

public abstract class BuildingView extends BorderPane {

	protected Size size;
	
	protected MainScene scene;
	protected GridPane layout;
	protected Direction exit;
	
	private int upInBuilding = 32;
	
	public BuildingView(Size size, MainScene scene, Direction exit) {
		this.size = size;
		
		this.scene = scene;
		this.layout = new GridPane();
		
		setLayoutX(scene.getWidth()/2 - (size.getWidth()/2 * 128));
		setLayoutY(scene.getHeight()/2 - (size.getHeight() * 128) + upInBuilding);
		
		setCenter(layout);
	}
	
	public void move(Direction dir) {
		setLayoutX(getLayoutX() + dir.getX());
		setLayoutY(getLayoutY() + dir.getY());
	}
	
	public abstract boolean locationIsOnWall(PlayerView playerView, Direction dir);
	
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
		return x + 1 == size.getWidth()/2 || x + 1 == size.getWidth()/2 + 1;
//		return (size.getWidth()/2 % 2 != 0 && x + 1 == size.getWidth()/2 + 1)
//				|| (size.getWidth()/2 % 2 == 0 && x + 1 == size.getWidth()/2)
//				|| (size.getWidth()/2 % 2 == 0 && x + 1 == size.getWidth()/2 + 1);
	}
	
	protected boolean isYMiddle(int y) {
		return y + 1 == size.getHeight()/2 || y + 1 == size.getHeight()/2 + 1;
//		return (size.getWidth()/2 % 2 != 0 && x + 1 == size.getWidth()/2 + 1)
//				|| (size.getWidth()/2 % 2 == 0 && x + 1 == size.getWidth()/2)
//				|| (size.getWidth()/2 % 2 == 0 && x + 1 == size.getWidth()/2 + 1);
	}
	
	protected boolean playerIsOnNode(double x, double y, PlayerView playerView, Direction dir) {
		double playerX = playerView.getLayoutX();
		double playerY = playerView.getLayoutY();
		if(dir == Direction.EAST) {
			playerX += playerView.getBoundsInParent().getWidth()/3;
		}
		if(dir == Direction.SOUTH) {
			playerY += playerView.getBoundsInParent().getHeight()/5;
		}
		return x - 32 <= playerX + dir.getX() && x - 32 + 127 >= playerX + dir.getX()
		&& y - 32 <= playerY + dir.getY() && y - 32 + 127 >= playerY + dir.getY();
	}

}
