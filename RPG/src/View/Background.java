package View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Background extends Pane {
	
	public Background() {
		setBackground();
	}
	
	public void move(int x, int y) {
		setLayoutX(x);
		setLayoutY(y);
	}
	
	private void setBackground() {
		for(int x = -15; x < 15; x++) {
			for(int y = -15; y < 15; y++) {
				placeBackground(x, y);
			}
		}
		
	}
	
	private void placeBackground(int x, int y) {
		Image img = new Image("Images/Grass.png");
		ImageView imgView = new ImageView(img);
		
		imgView.setLayoutX(img.getWidth() * x);
		imgView.setLayoutY(img.getHeight() * y);
		
		getChildren().add(imgView);
	}
	
}
