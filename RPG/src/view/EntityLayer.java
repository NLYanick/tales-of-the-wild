package view;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class EntityLayer extends Pane {
	
	private ArrayList<ImageView> entityViews;
	private ArrayList<ImageView> buildingViews;
	
	public EntityLayer() {
		entityViews = new ArrayList<ImageView>(); // Entities (Player and NPCs) and Items
		buildingViews = new ArrayList<ImageView>();
	}
	
	public void addEntity(ImageView imageView) {
		entityViews.add(imageView);
		getChildren().add(imageView);
	}
	
	public ImageView addBuilding(int x, int y, String url) {
		ImageView imgView = makeImageView(x, y, url);
		imgView.setUserData(url);
		
		buildingViews.add(imgView);
		getChildren().add(imgView);
		
		return imgView;
	}
	
	private ImageView makeImageView(int x, int y, String url) {		
		Image img = new Image(url);
		ImageView imgView = new ImageView(img);
		
		imgView.setFitWidth(img.getWidth());
		imgView.setFitHeight(img.getHeight());
		
		imgView.setLayoutX(x);
		imgView.setLayoutY(y);
		
		imgView.setTranslateY(EntityView.IMAGE_SIZE/3); // So the entity images don't go into other images
		
		return imgView;
	}
	
}
