package view;

import javafx.geometry.Bounds;
import model.Direction;

public class PlayerView extends EntityView {
	
	public PlayerView(String imgURL) {
		super(imgURL);
	}
	
	public boolean nextStepIsOnNPCView(NPCView npcView, Direction dir) {
		
		int lessVerticalPersonalSpace = 20;
		int lessHorizontalPersonalSpace = 10;
				
		Bounds bounds = getBoundsInParent();
		Bounds npcBounds = npcView.getBoundsInParent();

		if(bounds.intersects(npcBounds.getMinX() - lessHorizontalPersonalSpace + dir.getX(), 
				npcBounds.getMinY() - lessVerticalPersonalSpace * 3 + dir.getY(),
				npcBounds.getWidth()/2 + dir.getX(), npcBounds.getHeight() + dir.getY())
			&& npcBounds.intersects(bounds.getMinX() - dir.getX(), bounds.getMinY() - dir.getY(), 
					bounds.getWidth()/2 - lessHorizontalPersonalSpace - dir.getX(), 
					bounds.getHeight() - lessVerticalPersonalSpace - dir.getY())) {
			return true;
		}
				
		return false;
	}
	
}
