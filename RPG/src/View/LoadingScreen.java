package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LoadingScreen extends BorderPane {

	public LoadingScreen(int dots) {
		setUpLoadingScreen(dots);
	}

	private void setUpLoadingScreen(int dots) {
		
		setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		
		String text = "Loading";
		
		for(int i = 0; i < dots; i++) {
			text.concat(".");
		}
		
		Label loadingText = new Label(text);
		loadingText.setFont(Font.font(40));
		loadingText.setTextFill(Color.WHITE);
		
		HBox layout = new HBox(loadingText);
		layout.setAlignment(Pos.BASELINE_RIGHT);
		layout.setPadding(new Insets(0, 50, 50, 0));
		
		setBottom(layout);
	}
	
}
