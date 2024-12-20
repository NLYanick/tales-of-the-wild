package View;

import java.io.File;

import Controller.FileIO;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StartUpView extends BorderPane {
	
	private FileIO fileIO;
	private MainScene scene;
	
	private VBox layout;
	
	public StartUpView(FileIO fileIO, MainScene scene) {
		this.fileIO = fileIO;
		this.scene = scene;
		
		setUpStarterView();
	}

	private void setUpStarterView() {
		
		setBackground(new Background(new BackgroundImage(new Image("Images/Background/Grass.png"), null, null, null, null)));
		
		setMinSize(MainScene.SCENEWIDTH, MainScene.SCENEHEIGHT);
		
		setUpLayout();
		setUpTopText();
		setUpButton();
		
		setCenter(layout);
		
	}
	
	private void setUpLayout() {
		
		int spacing = 200;
		
		layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(spacing);
	}
	
	private void setUpTopText() {
		
		int fontSize = 50;
		
		Label welcomeText = new Label("Welcome!");
		welcomeText.setFont(Font.font(fontSize));
		welcomeText.setTextFill(Color.WHITE);
		
		HBox topPane = new HBox();
		topPane.getChildren().add(welcomeText);
		topPane.setAlignment(Pos.CENTER);
		
		layout.getChildren().add(topPane);
	}
	
	private void setUpButton() {
		
		int buttonWidth = 120;
		int buttonHeight= buttonWidth / 2;
		
		Button button = new Button("Continue");
		button.setPrefSize(buttonWidth, buttonHeight);
		
		button.setOnAction(e -> loadBackground());
		
		layout.getChildren().add(button);
	}
	
	private void loadBackground() {
		fileIO.readText(new File(FileIO.BACKGROUNDFILEPATH));
		scene.loadBackground();
	}
}
