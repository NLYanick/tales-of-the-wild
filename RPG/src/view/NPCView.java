package view;

public class NPCView extends EntityView {

	public NPCView(String imageURL, int startX, int startY) {
		super(imageURL);
		move(startX, startY);
	}
	
}
