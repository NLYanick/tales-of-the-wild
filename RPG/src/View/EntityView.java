package View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public abstract class EntityView extends BorderPane {
	
	protected Image image;
	protected ImageView imageView;
	
	protected int imageSize;
	protected String imageURL;
	
	public EntityView(String imageURL) {
		this.imageURL = imageURL;
		
		image = new Image(imageURL);
		imageView = new ImageView(image);
		
		setCenter(imageView);
		
		imageSize = (int) image.getWidth();
	}

	public void move(int x, int y) {
		setLayoutX(x);
		setLayoutY(y);
	}
	
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public void refreshImage() {
		image = new Image(imageURL);
		imageView.setImage(image);
		
		setCenter(null);
		setCenter(imageView);
	}
	
}
