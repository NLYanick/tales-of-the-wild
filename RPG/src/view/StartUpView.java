package view;

import controller.ApplicationController;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StartUpView extends BorderPane {
	
	private final static int SPACING = 60;
	
	private MainScene scene;
	
	private VBox layout;
	
	public StartUpView(MainScene scene) {
		this.scene = scene;
		
		setUpStarterView();
	}

	private void setUpStarterView() {
		
		setBackground(new Background(new BackgroundImage(new Image("Images/Background/Grass/Grass.png"), null, null, null, null)));
		
		setMinSize(MainScene.SCENEWIDTH, MainScene.SCENEHEIGHT);
		
		setUpLayout();
		
		setCenter(layout);
		
	}
	
	private void setUpLayout() {
		layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(SPACING);
		
		setUpTopText();
		setUpImage();
		setUpButtons();
	}
	
	private void setUpTopText() {
		
		Label welcomeText = new Label(ApplicationController.APPLICATIONNAME);
		welcomeText.getStyleClass().add("start-title");
		
		HBox topPane = new HBox(welcomeText);
		topPane.setAlignment(Pos.CENTER);
		
		layout.getChildren().add(topPane);
	}
	
	private void setUpButtons() {
		Button continueButton = getButton("Load Game");
		continueButton.setOnAction(e -> loadGame());
		Button newGameButton = getButton("New Game");
		newGameButton.setOnAction(e -> makeNewGame());
		
		HBox buttons = new HBox(continueButton, newGameButton);
		buttons.setSpacing(SPACING);
		buttons.setAlignment(Pos.CENTER);
		
		Button exitButton = getButton("Exit");
		exitButton.setOnAction(e -> exit());
		
		layout.getChildren().addAll(buttons, exitButton);
	}
	
	private Button getButton(String text) {
		Button button = new Button(text);
		button.getStyleClass().add("startup-button");
		button.setOnKeyPressed(e -> handleButtonKeyPressed(e, button));
		
		return button;
	}
	
	private void handleButtonKeyPressed(KeyEvent e, Button button) {
		if(e.getCode().equals(KeyCode.ENTER) && button.isFocused()) {
			button.fire();
		}
	}
	
	private void setUpImage() {
		Image foxImage = new Image("Images/Fox/FoxStandingStill.gif");
        ImageView foxImageView = new ImageView(foxImage);
        
        layout.getChildren().add(foxImageView);
	}
	
	private void loadGame() {
		scene.addLoadGameView();
	}
	
	private void makeNewGame() {
		scene.addNewGameView();
	}
	
	private void exit() {
		Platform.exit();
	}
}
