package View;

import Model.Direction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Background extends Pane {
	
	public Background() {
		
	}
	
	public void move(int x, int y) {
		setLayoutX(x);
		setLayoutY(y);
	}
	
	public void placeBackground(int x, int y, String url, Direction dir) {
		ImageView imgView = makeImageView(x, y, url);
		

		int degrees = getDegrees(dir);
		imgView.setRotate(degrees);
		
		
		getChildren().add(imgView);
	}
	
	public void placeBackground(int x, int y, String url) {
		getChildren().add(makeImageView(x, y, url));
	}
	
	private ImageView makeImageView(int x, int y, String url) {
		Image img = new Image(url);
		ImageView imgView = new ImageView(img);
		
		imgView.setFitWidth(img.getWidth());
		imgView.setFitHeight(img.getHeight());
		
		imgView.setLayoutX(imgView.getFitWidth() * x);
		imgView.setLayoutY(imgView.getFitWidth() * y);
		
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
