package view;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Direction;

public class Background extends Pane {
	
	private ArrayList<ImageView> imageViews;
	
	public Background() {
		imageViews = new ArrayList<ImageView>();
	}
	
	public void move(int x, int y) {
		setLayoutX(x);
		setLayoutY(y);
	}
	
	public void placeBackground(int x, int y, String url, Direction dir) {
		ImageView imgView = makeImageView(x, y, url);
		
		int degrees = getDegrees(dir);
		imgView.setRotate(degrees);
		
		imageViews.add(imgView);
		getChildren().add(imgView);
	}
	
	public void placeBackground(int x, int y, String url) {
		ImageView imgView = makeImageView(x, y, url);
		
		imageViews.add(imgView);
		getChildren().add(imgView);
	}
	
	public void setSize(int width, int height) {
		int imgSize = 128;
		setWidth(width * imgSize);
		setHeight(height * imgSize);
	}
	
	private ImageView makeImageView(int x, int y, String url) {
		int standardImageSize = 128;
		
		Image img = new Image(url);
		ImageView imgView = new ImageView(img);
		
		imgView.setFitWidth(img.getWidth());
		imgView.setFitHeight(img.getHeight());
		
		imgView.setLayoutX(standardImageSize * x);
		imgView.setLayoutY(standardImageSize * y);
		
		return imgView;
	}
	
	private int getDegrees(Direction dir) {
		switch(dir) {
		case NORTH:
			return 0;
		case EAST:
			return 90;
		case SOUTH:
			return 180;
		case WEST:
			return 270;
		default: return 0;
		}
		
	}
	
}
