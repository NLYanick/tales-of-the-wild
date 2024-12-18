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
				placeBackground(x, y, "Images/Background/Grass.png", false);
			}
		}
		placeBackground(0, 0, "Images/Background/Water.png", false);
		placeBackground(1, 0, "Images/Background/WaterGrassCorner.png", false);
		placeBackground(2, 0, "Images/Background/WaterGrassSidesAndTopBottom.png", false);
		placeBackground(3, 0, "Images/Background/Grass.png", false);
		placeBackground(0, 1, "Images/Background/WaterGrassCorner.png", false);
		placeBackground(1, 1, "Images/Background/WaterGrassSidesAndTopBottom.png", false);
		placeBackground(2, 1, "Images/Background/Grass.png", false);
		placeBackground(3, 1, "Images/Background/Grass.png", false);
		placeBackground(0, 2, "Images/Background/WaterGrassSides.png", true);
		placeBackground(1, 2, "Images/Background/Grass.png", false);
		placeBackground(2, 2, "Images/Background/Grass.png", false);
		placeBackground(3, 2, "Images/Background/Grass.png", false);
		placeBackground(0, 3, "Images/Background/WaterGrassSides.png", true);
		placeBackground(1, 3, "Images/Background/Grass.png", false);
		placeBackground(2, 3, "Images/Background/Grass.png", false);
		placeBackground(3, 3, "Images/Background/Grass.png", false);
	}
	
//	private void placeBackground(int x, int y, String url) {
	private void placeBackground(int x, int y, String url, boolean flip) {
		Image img = new Image(url);
		ImageView imgView = new ImageView(img);
		
		imgView.setLayoutX(img.getWidth() * x);
		imgView.setLayoutY(img.getHeight() * y);
		
		if(flip) {
			imgView.setRotate(180);
		}
		
		getChildren().add(imgView);
	}
	
}
