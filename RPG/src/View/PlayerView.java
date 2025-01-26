package View;

import Model.Direction;
import javafx.geometry.Bounds;

public class PlayerView extends EntityView {
	
	public PlayerView(String imgURL) {
		super(imgURL);
	}
	
	public boolean nextStepIsOnNPCView(NPCView npcView, Direction dir) {
		
		boolean isOnNPCView = false;
		int lessVerticalPersonalSpace = 20;
		int lessHorizontalPersonalSpace = 10;
		
		setLayoutX(getLayoutX() - dir.getX());
		setLayoutY(getLayoutY() - dir.getY());
		
		Bounds bounds = getBoundsInParent();
		Bounds npcBounds = npcView.getBoundsInParent();

		if(bounds.intersects(npcBounds.getMinX() - lessHorizontalPersonalSpace, npcBounds.getMinY() - lessVerticalPersonalSpace * 3,
				npcBounds.getWidth()/2, npcBounds.getHeight())
			&& npcBounds.intersects(bounds.getMinX(), bounds.getMinY(), 
					bounds.getWidth()/2 - lessHorizontalPersonalSpace, bounds.getHeight() - lessVerticalPersonalSpace)) {
			isOnNPCView = true;
		}
		
		setLayoutX(getLayoutX() + dir.getX());
		setLayoutY(getLayoutY() + dir.getY());
		
		return isOnNPCView;
	}
	
}
