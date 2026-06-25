package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NewGameView extends BorderPane {

	private MainScene scene;
	
	private VBox layout;
	private TextField nameField;
	private BorderPane errorPane;
	
	public NewGameView(MainScene scene) {
		this.scene = scene;
		
		errorPane = new BorderPane();
		errorPane.getStyleClass().add("name-error-pane");
		
		setUpNewGameView();
	}

	private void setUpNewGameView() {
		
		setBackground(new Background(new BackgroundImage(new Image("Images/Background/Grass/Grass.png"), null, null, null, null)));

		setMinSize(MainScene.SCENEWIDTH, MainScene.SCENEHEIGHT);
		
		setUpLayout();
		
		setCenter(layout);
		
	}
	
	private void setUpLayout() {
		
		int spacing = 80;
		
		layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(spacing);
		
		setUpTopText();
		setUpNewGame();
		setUpButtons();
	}
	
	private void setUpTopText() {
		
		Label topText = new Label("New Game");
		topText.getStyleClass().add("start-title");
		
		HBox topPane = new HBox();
		topPane.getChildren().add(topText);
		topPane.setAlignment(Pos.CENTER);
		
		layout.getChildren().add(topPane);
	}
	
	private void setUpNewGame() {
		BorderPane namePane = new BorderPane();
		
		Label nameLabel = new Label("Name");
		nameLabel.getStyleClass().add("name-label");
		namePane.setTop(nameLabel);
		BorderPane.setAlignment(nameLabel, Pos.CENTER);
		BorderPane.setMargin(nameLabel, new Insets(0, 0, 15, 0));
		
		nameField = new TextField();
		nameField.getStyleClass().add("name-field");
		namePane.setCenter(nameField);
		
		layout.getChildren().add(namePane);
	}
	
	private void setUpButtons() {
		int spacing = 50;
		
		HBox buttons = new HBox();
		buttons.setSpacing(spacing);
		buttons.setAlignment(Pos.CENTER);
		
		Button goBackButton = getButton("Go Back");
		goBackButton.setOnAction(e -> goBack());
		
		Button createButton = getButton("Create");
		createButton.setOnAction(e -> createPlayer());
		
		buttons.getChildren().addAll(goBackButton, createButton);
		
		layout.getChildren().add(buttons);
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
	
	private void goBack() {
		scene.goBackToStartUpView();
	}
	
	private void createPlayer() {
		String name = nameField.getText();
		if(!nameIsValid(name)) {
			return;
		}
		scene.createPlayer(name);
	}
	
	private boolean nameIsValid(String name) {
		if(name.equals(" ") || name.equals("") || name == null) {
			addError("Name can't be empty");
			return false;
		}
		if(name.length() > 50) {
			addError("Name must be under 50 characters");
			return false;
		}
		if(!scene.nameIsUnique(name)) {
			addError("Name must be unique");
			return false;
		}
		return true;
	}
	
	private void addError(String text) {
		
		if(layout.getChildren().contains(errorPane)) {
			layout.getChildren().remove(errorPane);
		}
		
		Label errorLabel = new Label(text);
		errorLabel.getStyleClass().add("name-error-label");
		errorPane.setCenter(errorLabel);
		
		layout.getChildren().add(2, errorPane);
	}
	
}
