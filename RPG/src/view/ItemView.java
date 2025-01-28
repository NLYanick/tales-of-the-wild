package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import model.Location;

public class ItemView extends BorderPane {

	public ItemView(Location location, String url) {
		setImage(url);
		move(location);
	}
	
	private void setImage(String url) {
		Image image = new Image(url);
		ImageView imageView = new ImageView(image);
		
		setCenter(imageView);
	}

	public void move(Location location) {
		setLayoutX(location.getX());
		setLayoutY(location.getY());
	}
	
}
