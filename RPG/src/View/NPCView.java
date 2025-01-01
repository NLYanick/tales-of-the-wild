package View;

public class NPCView extends EntityView {

	public NPCView(String imageURL, int startX, int startY) {
		super(imageURL);
		move(startX, startY);
	}

	public void fixImage() {
		int fixX = (int) (getLayoutX() - imageView.getFitWidth()/2);
		int fixY = (int) (getLayoutY() - imageView.getFitHeight()/2);
		
		move(fixX, fixY);
	}

}
