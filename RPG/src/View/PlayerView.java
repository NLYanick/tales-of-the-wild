package View;

import Controller.MainController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PlayerView extends Pane {

	private MainController controller;
	
	private ImageView imgView;
	private Image img;
	
	public PlayerView(MainController controller)
	{
		this.controller = controller;
		
		setUpImage();
		setMaxSize(96, 96);
	}
	
	private void setUpImage()
	{
		img = new Image("Images/Fox.png");
		imgView = new ImageView(img);
		imgView.setLayoutX(-16);
		imgView.setLayoutY(-5);
		
		getChildren().add(imgView);
	}
	
	public void move(int x, int y) {
		setLayoutX(x);
		setLayoutY(y);
	}
}
