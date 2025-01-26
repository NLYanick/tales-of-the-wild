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
		
		setLayoutX(getLayoutX() + dir.getX());
		setLayoutY(getLayoutY() + dir.getY());
		
		Bounds bounds = getBoundsInParent();
		Bounds npcBounds = playerView.getBoundsInParent();

		if(bounds.intersects(npcBounds.getMinX(), npcBounds.getMinY(),
				npcBounds.getWidth()/2, npcBounds.getHeight())
			&& playerView.getBoundsInParent().intersects(bounds.getMinX(), bounds.getMinY(), 
					bounds.getWidth()/2, bounds.getHeight())) {
			isOnNPCView = true;
		}
		
		setLayoutX(getLayoutX() - dir.getX());
		setLayoutY(getLayoutY() - dir.getY());
		
		return isOnNPCView;
	}
}
