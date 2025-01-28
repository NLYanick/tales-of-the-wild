package view;

import javafx.geometry.Bounds;
import model.Direction;

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
		
		Bounds bounds = getBoundsInParent();
		Bounds playerBounds = playerView.getBoundsInParent();

		if(bounds.intersects(playerBounds.getMinX() - dir.getX(), playerBounds.getMinY() - dir.getY(),
				playerBounds.getWidth()/2 - lessHorizontalPersonalSpace - dir.getX(), 
				playerBounds.getHeight() - lessVerticalPersonalSpace - dir.getY())
			&& playerBounds.intersects(bounds.getMinX() - lessHorizontalPersonalSpace + dir.getX(), 
					bounds.getMinY() - lessVerticalPersonalSpace * 3 + dir.getY(), 
					bounds.getWidth()/2 + dir.getX(), bounds.getHeight() + dir.getY())) {
			isOnNPCView = true;
		}
		
		return isOnNPCView;
	}
}
