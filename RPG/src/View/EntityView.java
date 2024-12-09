package View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public abstract class EntityView extends BorderPane {
	
	protected Image image;
	protected ImageView imageView;
	
	protected int imageSize;
	
	public EntityView(String imgPath) {
		image = new Image(imgPath);
		imageView = new ImageView(image);
		
		setCenter(imageView);
		
		imageSize = (int) image.getWidth();
	}

	public void move(int x, int y) {
		setLayoutX(x);
		setLayoutY(y);
	}
	
}
