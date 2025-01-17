package View;

import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MenuView extends BorderPane {
	
	public MenuView() {
		setUpLayout();
	}
	
	private void setUpLayout() {
		
		Color color = new Color(0, 0, 0, 0.4);
		
		Pane menu = new Pane();
		menu.setBackground(new Background(new BackgroundFill(color, null, null)));
		
		setCenter(menu);
		
	}
	
}
