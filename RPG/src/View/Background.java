package View;

import java.util.ArrayList;

import Model.Direction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

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
	
	private ImageView makeImageView(int x, int y, String url) {
		Image img = new Image(url);
		ImageView imgView = new ImageView(img);
		
		imgView.setFitWidth(img.getWidth());
		imgView.setFitHeight(img.getHeight());
		
		imgView.setLayoutX(imgView.getFitWidth() * x);
		if(img.getWidth() == 128 && img.getHeight() == 128) {
			imgView.setLayoutY(imgView.getFitWidth() * y);
		} else {
			imgView.setLayoutY(128 * y);
		}
		
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
