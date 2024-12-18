package View;

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
	
	public void placeBackground(int x, int y, String url, boolean flip) {
		ImageView imgView = makeImageView(x, y, url);
		
		if(flip) {
			int degrees = 180;
			imgView.setRotate(degrees);
		}
		
		getChildren().add(imgView);
	}
	
	public void placeBackground(int x, int y, String url) {
		getChildren().add(makeImageView(x, y, url));
	}
	
	private ImageView makeImageView(int x, int y, String url) {
		Image img = new Image(url);
		ImageView imgView = new ImageView(img);
		
		imgView.setLayoutX(img.getWidth() * x);
		imgView.setLayoutY(img.getHeight() * y);
		
		return imgView;
	}
	
}
