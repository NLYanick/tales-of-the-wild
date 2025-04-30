package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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

public class LoadGameView extends BorderPane {
		
	private MainScene scene;
	
	private VBox layout;
	
	public LoadGameView(MainScene scene) {
		this.scene = scene;
		
		setUpLoadGameView();
	}

	private void setUpLoadGameView() {
		
		setBackground(new Background(new BackgroundImage(new Image("Images/Background/Grass/Grass.png"), null, null, null, null)));
		
		setMinSize(MainScene.SCENEWIDTH, MainScene.SCENEHEIGHT);
		
		setUpLayout();
		setUpTopText();
		setUpGames();
		setUpButton();
		
		setCenter(layout);
		
	}
	
	private void setUpLayout() {
		
		int spacing = 50;
		
		layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(spacing);
	}
	
	private void setUpTopText() {
		
		int fontSize = 60;
		
		Label topText = new Label("Load Game");
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
	
	private void setUpGames() {
		
		int height = 500;
		
		VBox content = getScrollBarContent();
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, null, null)));
		scrollPane.setMinSize(content.getMinWidth(), height);
		scrollPane.setMaxSize(content.getMaxWidth(), height);
		scrollPane.setStyle("-fx-background: #32CD32;\n -fx-border-color: #32CD32;");
		
		scrollPane.setContent(content);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		
		layout.getChildren().add(scrollPane);
	}
	
	private VBox getScrollBarContent() {
		int width = 800;
		int extraSpce = 5;
		
		VBox content = new VBox();
		for(String playerName : scene.getAllPlayerNames()) {
			content.getChildren().add(getGamePane(playerName));
		}
		
		content.setMinWidth(width);
		content.setMaxWidth(width + extraSpce);
		
		return content;
	}
	
	private BorderPane getGamePane(String playerName) {
		int borderWidths = 5;
		int fontSize = 24;
		int padding = 15;
		
		BorderPane gamePane = new BorderPane();
		gamePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(borderWidths))));
		gamePane.setPadding(new Insets(padding, padding, padding, 0));
		
		Label nameLabel = new Label(playerName);
		nameLabel.setFont(Font.font(MainScene.FONTNAME, fontSize));
		
		HBox buttons = getButtonsOfGamePane(playerName);
		
		BorderPane playerBox = new BorderPane();
		playerBox.setCenter(nameLabel);
		playerBox.setRight(buttons);
		BorderPane.setAlignment(buttons, Pos.CENTER);
		gamePane.setCenter(playerBox);
		
		return gamePane;
	}
	
	private HBox getButtonsOfGamePane(String playerName) {
		int spacing = 30;
		
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(spacing);
		
		Button loadButton = getButton("Load");
		loadButton.setOnAction(e -> scene.loadGame(playerName));
		loadButton.setPrefWidth(loadButton.getPrefWidth()/3 * 2);
		loadButton.setPrefHeight(loadButton.getPrefHeight()/2);
		loadButton.setStyle("-fx-background-color: forestgreen;");
		
		Button deleteButton = getButton("Delete");
		deleteButton.setOnAction(e -> scene.deletePlayer(playerName));
		deleteButton.setPrefWidth(deleteButton.getPrefWidth()/3 * 2);
		deleteButton.setPrefHeight(deleteButton.getPrefHeight()/2);
		deleteButton.setStyle("-fx-background-color: forestgreen;");
		
		buttons.getChildren().addAll(loadButton, deleteButton);
		
		return buttons;
	}
	
	private void setUpButton() {
		
		Button goBackButton = getButton("Go Back");
		goBackButton.setOnAction(e -> goBack());
		
		layout.getChildren().addAll(goBackButton);
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
	
}
