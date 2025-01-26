package View;

import Model.Direction;
import javafx.geometry.Bounds;

public class NPCView extends EntityView {

	public NPCView(String imageURL, int startX, int startY) {
		super(imageURL);
		move(startX, startY);
	}
	
	public boolean nextStepIsOnPlayerView(PlayerView playerView, Direction dir) {
		if(playerView == null) {
			return false;
		}
		
		boolean isOnNPCView = false;
		int lessVerticalPersonalSpace = 20;
		int lessHorizontalPersonalSpace = 10;
		
		setLayoutX(getLayoutX() + dir.getX());
		setLayoutY(getLayoutY() + dir.getY());
		
		Bounds bounds = getBoundsInParent();
		Bounds playerBounds = playerView.getBoundsInParent();

		if(bounds.intersects(playerBounds.getMinX(), playerBounds.getMinY(),
				playerBounds.getWidth()/2 - lessHorizontalPersonalSpace, playerBounds.getHeight() - lessVerticalPersonalSpace)
			&& playerBounds.intersects(bounds.getMinX() - lessHorizontalPersonalSpace, bounds.getMinY() - lessVerticalPersonalSpace * 3, 
					bounds.getWidth()/2, bounds.getHeight())) {
			isOnNPCView = true;
		}
		
		setLayoutX(getLayoutX() - dir.getX());
		setLayoutY(getLayoutY() - dir.getY());
		
		return isOnNPCView;
	}
}
