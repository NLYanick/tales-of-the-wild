package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import model.Entity;

public abstract class EntityView extends BorderPane {
	
	protected final static int IMAGESIZE = (int) (Entity.ENTITY_WIDTH * 0.75);
	
	protected Image image;
	protected ImageView imageView;
		
	protected String imageURL;
	
	protected int yUp = 30;
	
	public EntityView(String imageURL) {
		this.imageURL = imageURL;
		
		setUpImage();
	}
	
	private void setUpImage() {
		image = new Image(imageURL);
		imageView = new ImageView(image);
		
		imageView.setFitWidth(IMAGESIZE);
		imageView.setFitHeight(IMAGESIZE);
		
		imageView.setTranslateY(-yUp);
		
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
	
	public void fixImage() {
		int fixX = (int) (getLayoutX() - imageView.getFitWidth()/2);
		int fixY = (int) (getLayoutY() - imageView.getFitHeight()/2);
		
		move(fixX, fixY);
	}
	
}
