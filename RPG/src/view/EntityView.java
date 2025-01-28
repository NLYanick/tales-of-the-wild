package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class EntityView extends BorderPane {
	
	protected final static int IMAGESIZE = 96;
	
	protected Image image;
	protected ImageView imageView;
	
	protected Rectangle hitBox;
	
	protected String imageURL;
	
	protected int yUp = 30;
	
	public EntityView(String imageURL) {
		this.imageURL = imageURL;
		
		setUpImage();
		setHitBox();
	}
	
	private void setUpImage() {
		image = new Image(imageURL);
		imageView = new ImageView(image);
		
		imageView.setFitWidth(IMAGESIZE);
		imageView.setFitHeight(IMAGESIZE);
		
		imageView.setTranslateY(-yUp);
		
		setCenter(imageView);
	}
	
	private void setHitBox() {
		hitBox = new Rectangle(IMAGESIZE/2, IMAGESIZE);
		hitBox.setFill(Color.TRANSPARENT);
		
		int strokeWidth = 5;
//		hitBox.setStroke(Color.BLACK);
		hitBox.setStrokeWidth(strokeWidth);
		
		hitBox.setX(getLayoutX() + IMAGESIZE/4);
		hitBox.setY(getLayoutY() - yUp);
		
		getChildren().add(hitBox);
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
