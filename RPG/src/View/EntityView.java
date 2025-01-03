package View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public abstract class EntityView extends BorderPane {
	
	protected final static int IMAGESIZE = 96;
	
	protected Image image;
	protected ImageView imageView;
	
	protected String imageURL;
	
	public EntityView(String imageURL) {
		this.imageURL = imageURL;
		
		setUpImage();
	}
	
	private void setUpImage() {
		image = new Image(imageURL);
		imageView = new ImageView(image);
		
		imageView.setFitWidth(IMAGESIZE);
		imageView.setFitHeight(IMAGESIZE);
		
		imageView.setTranslateY(-30);
		
		setCenter(imageView);
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
	
	public ImageView getImageView() {
		return imageView;
	}
	
}
