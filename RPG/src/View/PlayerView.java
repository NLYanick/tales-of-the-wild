package View;

import Model.Direction;
import javafx.geometry.Bounds;

public class PlayerView extends EntityView {
	
	public PlayerView(String imgURL) {
		super(imgURL);
	}
	
	public boolean nextStepIsOnNPCView(NPCView npcView, Direction dir) {
		boolean isOnNPCView = false;
		
		setLayoutX(getLayoutX() - dir.getX());
		setLayoutY(getLayoutY() - dir.getY());
		
		Bounds bounds = getBoundsInParent();
		Bounds npcBounds = npcView.getBoundsInParent();

		if(bounds.intersects(npcBounds.getMinX(), npcBounds.getMinY(),
				npcBounds.getWidth()/2, npcBounds.getHeight())
			&& npcView.getBoundsInParent().intersects(bounds.getMinX(), bounds.getMinY(), 
					bounds.getWidth()/2, bounds.getHeight())) {
			isOnNPCView = true;
		}
		
		setLayoutX(getLayoutX() + dir.getX());
		setLayoutY(getLayoutY() + dir.getY());
		
		return isOnNPCView;
	}
	
}
