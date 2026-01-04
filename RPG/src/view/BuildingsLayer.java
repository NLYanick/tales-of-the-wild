package view;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class BuildingsLayer extends Pane {
	
	private ArrayList<ImageView> buildingViews;
	
	public BuildingsLayer() {
		buildingViews = new ArrayList<ImageView>();
	}
	
	public void move(int x, int y) {
		setLayoutX(x);
		setLayoutY(y);
	}
	
	public void placeBuilding(int x, int y, String url) {
		ImageView imgView = makeImageView(x, y, url);
		imgView.setUserData(url);
		
		buildingViews.add(imgView);
		getChildren().add(imgView);
	}
	
	private ImageView makeImageView(int x, int y, String url) {
		int standardImageSize = MainScene.STANDARD_IMAGE_SIZE;
		
		Image img = new Image(url);
		ImageView imgView = new ImageView(img);
		
		imgView.setFitWidth(img.getWidth());
		imgView.setFitHeight(img.getHeight());
		
		imgView.setLayoutX(standardImageSize * x);
		imgView.setLayoutY(standardImageSize * y);
		
		imgView.setTranslateY(EntityView.IMAGE_SIZE/3); // So the entity images don't go into other images
		
		return imgView;
	}
	
}
