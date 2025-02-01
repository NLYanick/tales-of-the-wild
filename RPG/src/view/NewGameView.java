package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class NewGameView extends BorderPane {

	private MainScene scene;
	
	private VBox layout;
	private TextField nameField;
	private BorderPane errorPane;
	
	public NewGameView(MainScene scene) {
		this.scene = scene;
		
		setUpErrorPane();
		
		setUpNewGameView();
	}

	private void setUpNewGameView() {
		
		setBackground(new Background(new BackgroundImage(new Image("Images/Background/Grass/Grass.png"), null, null, null, null)));
		
		setMinSize(MainScene.SCENEWIDTH, MainScene.SCENEHEIGHT);
		
		setUpLayout();
		setUpTopText();
		setUpNewGame();
		setUpButtons();
		
		setCenter(layout);
		
	}
	
	private void setUpLayout() {
		
		int spacing = 80;
		
		layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(spacing);
	}
	
	private void setUpTopText() {
		
		int fontSize = 60;
		
		Label topText = new Label("New Player");
		topText.setFont(Font.font(MainScene.FONTNAME, FontWeight.BOLD, fontSize));
		topText.setTextFill(Color.WHITE);
		
		topText.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(10))));
		topText.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, null, null)));
		topText.setPadding(new Insets(50));
		
		HBox topPane = new HBox();
		topPane.getChildren().add(topText);
		topPane.setAlignment(Pos.CENTER);
		
		layout.getChildren().add(topPane);
	}
	
	private void setUpNewGame() {
		int fontSize = 28;
		int textFieldWidth = 300;
		
		BorderPane namePane = new BorderPane();
		
		Label nameLabel = new Label("Name");
		nameLabel.setTextFill(Color.WHITE);
		nameLabel.setFont(Font.font(MainScene.FONTNAME, fontSize));
		namePane.setTop(nameLabel);
		BorderPane.setAlignment(nameLabel, Pos.CENTER);
		
		nameField = new TextField();
		nameField.setMaxWidth(textFieldWidth);
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
		int buttonWidth = 200;
		int buttonHeight= buttonWidth / 2;
		int fontSize = 30;
		
		Button button = new Button(text);
		button.setPrefSize(buttonWidth, buttonHeight);
		
		button.setFont(Font.font(MainScene.FONTNAME, fontSize));
		button.setTextFill(Color.WHITE);
		button.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, null, null)));
		button.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
		button.setCursor(Cursor.HAND);
		
		return button;
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
		
		int fontSize = 28;
		
		Label errorLabel = new Label(text);
		errorLabel.setTextFill(Color.RED);
		errorLabel.setFont(Font.font(MainScene.FONTNAME, FontWeight.BOLD, fontSize));
		errorPane.setCenter(errorLabel);
		
		layout.getChildren().add(2, errorPane);
	}
	
	private void setUpErrorPane() {
		int width = 600;
		int height = 100;
		int borderWidth = 10;
		
		errorPane = new BorderPane();
		errorPane.setMinSize(width, height);
		errorPane.setMaxSize(width, height);
		
		Color backgroundColor = new Color(0.3, 0, 0, 1);
		errorPane.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
		errorPane.setBorder(new Border(new BorderStroke(Color.FIREBRICK, BorderStrokeStyle.SOLID, null, new BorderWidths(borderWidth))));
	}
	
}
