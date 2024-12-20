package View;

import java.io.File;

import Controller.FileIO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class StartUpView extends BorderPane {
	
	private FileIO fileIO;
	private MainScene scene;
	
	public StartUpView(FileIO fileIO, MainScene scene) {
		this.fileIO = fileIO;
		this.scene = scene;
		
		setUpStarterView();
	}

	private void setUpStarterView() {
		
		setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, null, null)));
		setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
		
		setMinSize(200, 200);
		
		Label welcomeText = new Label("Welcome!");
		setTop(welcomeText);
		
		Button button = new Button("Continue");
		button.setOnAction(e -> loadBackground());
		setCenter(button);
	}
	
	private void loadBackground() {
		fileIO.readText(new File(FileIO.BACKGROUNDFILEPATH));
		scene.loadBackground();
	}
	
}
