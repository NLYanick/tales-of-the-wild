package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
		
		setCenter(layout);
		
	}
	
	private void setUpLayout() {
		
		int spacing = 50;
		
		layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(spacing);
		
		setUpTopText();
		setUpGames();
		setUpButton();
	}
	
	private void setUpTopText() {
		
		Label topText = new Label("Load Game");
		topText.getStyleClass().add("start-title");
		
		HBox topPane = new HBox();
		topPane.getChildren().add(topText);
		topPane.setAlignment(Pos.CENTER);
		
		layout.getChildren().add(topPane);
	}
	
	private void setUpGames() {
		
		VBox content = getScrollBarContent();
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.getStyleClass().add("games-scrollpane");
		
		scrollPane.setContent(content);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		
		layout.getChildren().add(scrollPane);
	}
	
	private VBox getScrollBarContent() {
		int spacing = 15;
		
		VBox content = new VBox(spacing);
		for(String playerName : scene.getAllPlayerNames()) {
			content.getChildren().add(getGamePane(playerName));
		}
		content.getStyleClass().add("games-scrollpane-content");
		
		return content;
	}
	
	private BorderPane getGamePane(String playerName) {
		
		BorderPane gamePane = new BorderPane();
		gamePane.getStyleClass().add("game-pane");
		
		Label nameLabel = new Label(playerName);
		nameLabel.getStyleClass().add("game-pane-name");
		
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
		
		Button loadButton = new Button("Load");
		loadButton.getStyleClass().add("game-pane-button");
		loadButton.setOnAction(e -> scene.loadGame(playerName));
		loadButton.setOnKeyPressed(e -> handleButtonKeyPressed(e, loadButton));
		
		Button deleteButton = new Button("Delete");
		deleteButton.getStyleClass().add("game-pane-button");
		deleteButton.setOnAction(e -> scene.deletePlayer(playerName));
		deleteButton.setOnKeyPressed(e -> handleButtonKeyPressed(e, deleteButton));
		
		buttons.getChildren().addAll(loadButton, deleteButton);
		
		return buttons;
	}
	
	private void setUpButton() {
		Button goBackButton = new Button("Go Back");
		goBackButton.getStyleClass().add("startup-button");
		goBackButton.setOnAction(e -> goBack());
		goBackButton.setOnKeyPressed(e -> handleButtonKeyPressed(e, goBackButton));
		
		layout.getChildren().addAll(goBackButton);
	}
	
	private void handleButtonKeyPressed(KeyEvent e, Button button) {
		if(e.getCode().equals(KeyCode.ENTER) && button.isFocused()) {
			button.fire();
		}
	}
	
	private void goBack() {
		scene.goBackToStartUpView();
	}
	
}
