package view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PauseMenuView extends BorderPane {
	
	private MainScene scene;
	private BorderPane pauseMenu;
	
	private BorderPane controlsPane;
	private VBox buttonsPane;
	private BorderPane savedPane;
	
	private int buttonWidth = 150;
	private int buttonSpacing = 30;
	private int buttonBorderWidth = 3;
	
	private int imageDifference = 4;
		
	private ImageView arrowView;
	
	public PauseMenuView(MainScene scene) {
		this.scene = scene;
		
		setUpLayout();
	}
	
	private void setUpLayout() {
		
		Color color = new Color(0, 0, 0, 0.5);
		
		pauseMenu = new BorderPane();
		pauseMenu.setBackground(new Background(new BackgroundFill(color, null, null)));
		
		buttonsPane = createButtonsVBox();
		controlsPane = createControlsPane();
		
		pauseMenu.setCenter(buttonsPane);
		
		arrowView = getArrow();
		pauseMenu.getChildren().add(arrowView);
		
		setCenter(pauseMenu);
		
		setUpSavedPane();
	}
	
	private void setUpSavedPane() {

		String saved = "Saved";
		int fontSize = 32;
		int minWidth = 100;
		
		Label savedLabel = new Label(saved);
		savedLabel.setFont(Font.font(MainScene.FONTNAME, fontSize));
		savedLabel.setTextFill(Color.WHITE);
		
		savedPane = new BorderPane(savedLabel);
		savedPane.setMinWidth(minWidth);
		savedPane.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(buttonBorderWidth))));
		
	}
	
	private ImageView getArrow() {
		ImageView arrowView = new ImageView(new Image("Images/SelectArrow.png"));
		
		int x = 810;
		int y = 402 + imageDifference;
		
		arrowView.setLayoutX(x);
		arrowView.setLayoutY(y);
		return arrowView;
	}
	
	private Button getButton(String text) {
				
		Button button = new Button(text);
		button.getStyleClass().add("menu-button");
		button.setOnKeyPressed(e -> handleButtonKeyPressed(e, button));
		
		return button;
	}
	
	private void exit() {
		scene.stopNPCThreads();
		scene.saveGame();
		Platform.exit();
	}

	private VBox createButtonsVBox() {
		
		VBox buttonsPane = new VBox();
		
		Button controlsButton = getButton("Controls");
		controlsButton.setOnAction(e -> openControls());
		addListenersToButton(controlsButton);
		
		Button saveButton = getButton("Save");
		saveButton.setOnAction(e -> saveGame());
		addListenersToButton(saveButton);
		
		Button exitButton = getButton("Exit Game");
		exitButton.setOnAction(e -> exit());
		addListenersToButton(exitButton);
		
		buttonsPane.getChildren().addAll(controlsButton, saveButton, exitButton);
		
		buttonsPane.setSpacing(buttonSpacing);
		buttonsPane.setAlignment(Pos.CENTER);
		
		return buttonsPane;
	}
	
	private void addListenersToButton(Button button) {
		button.hoverProperty().addListener(((observableValue, isNotHovered, isHovered) -> {
		 	if(isHovered) {
	 			button.requestFocus();
		 	} 
		}));
		button.focusedProperty().addListener(((observableValue, isNotFocused, isFocused) -> {
		 	if(isFocused) {
		 		double newX = (button.getLayoutX() - buttonWidth/2 + pauseMenu.getCenter().getLayoutX());
		 		if(newX > 0) {	 			
		 			pauseMenu.getChildren().remove(arrowView);
		 			arrowView.setLayoutX(newX);
		 			arrowView.setLayoutY(button.getLayoutY() + imageDifference);
		 			pauseMenu.getChildren().add(arrowView);
		 		}
		 	} 
		}));
	}
	
	private BorderPane createControlsPane() {
		
		int spacing = 100;
		
		BorderPane controlsPane = new BorderPane();
		
		VBox titleBox = getTitleBox();

		HBox textVBoxes = getTextVBoxes();
		textVBoxes.setSpacing(spacing);
		
		HBox buttonBox = getButtonBox();

		controlsPane.setTop(titleBox);
		controlsPane.setCenter(textVBoxes);
		controlsPane.setBottom(buttonBox);
		
		return controlsPane;
	}
	
	private VBox getTitleBox() {
		int height = 200;
		int fontSize = 64;
		
		VBox titleBox = new VBox();
		Label title = new Label("Controls");
		title.setFont(Font.font("Times New Roman", fontSize));
		title.setTextFill(Color.WHITE);
		
		titleBox.setAlignment(Pos.CENTER);
		titleBox.getChildren().add(title);
		titleBox.setMinHeight(height);
		
		return titleBox;
	}
	
	private HBox getButtonBox() {
		int height = 200;
		
		HBox buttonBox = new HBox();
		Button button = getButton("Go Back");
		button.setOnAction(e -> goBack());
		
		buttonBox.setAlignment(Pos.TOP_CENTER);
		buttonBox.getChildren().add(button);
		buttonBox.setMinHeight(height);
		
		return buttonBox;
	}
	
	private HBox getTextVBoxes() {
		
		int textSpacing = 20;
		
		HBox textVBoxes = new HBox();
		textVBoxes.setAlignment(Pos.CENTER);
		
		VBox textBoxOne = new VBox(textSpacing);
		textBoxOne.setAlignment(Pos.CENTER_LEFT);
		
		Text textE = getText("E - Interact");
		Text textEsc = getText("Esc - Toggle Pause Menu");
		Text textI = getText("I - Toggle In Game Menu");
		Text textQ = getText("Q - Drop Selected Item");
		Text textAlt = getText("Alt - Toggle Button Focus (Only in some views)");
		
		textBoxOne.getChildren().addAll(textE, textEsc, textI, textQ, textAlt);
		
		VBox textBoxTwo = new VBox(textSpacing);
		textBoxTwo.setAlignment(Pos.CENTER_LEFT);
		
		Text textUp = getText("Arrow Up | W - Walk Up");
		Text textLeft = getText("Arrow Left | A - Walk To Left");
		Text textDown = getText("Arrow Down | S - Walk Down");
		Text textRight = getText("Arrow Right | D - Walk To Right");
		
		textBoxTwo.getChildren().addAll(textUp, textLeft, textDown, textRight);
		
		textVBoxes.getChildren().addAll(textBoxOne, textBoxTwo);
		
		return textVBoxes;
	}
	
	private Text getText(String string) {
		
		int fontSize = 28;
		
		Text text = new Text(string);
		
		text.setFont(Font.font("Times New Roman", fontSize));
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFill(Color.WHITE);
		
		return text;
	}
	
	private void openControls() {
		pauseMenu.getChildren().remove(arrowView);
		pauseMenu.getChildren().remove(savedPane);
		pauseMenu.setCenter(controlsPane);
		for(Node node : controlsPane.getChildren()) {
			if(node instanceof Button) {
				node.requestFocus();
				break;
			}
		}
	}
	
	private void goBack() {
		resetArrow();
		pauseMenu.setCenter(buttonsPane);
		requestFocusForButtons();
	}

	public void resetView() {
		resetArrow();
		pauseMenu.getChildren().remove(savedPane);
		pauseMenu.setCenter(buttonsPane);
		pauseMenu.setLeft(null);
	}
	
	public void resetArrow() {
		if(!pauseMenu.getChildren().contains(arrowView)) {
			pauseMenu.getChildren().add(arrowView);
		}
	}
		
	private void handleButtonKeyPressed(KeyEvent e, Button button) {
		if(e.getCode().equals(KeyCode.ENTER) && button.isFocused()) {
			button.fire();
		}
	}
	
	public void requestFocusForButtons() {
		buttonsPane.getChildren().get(0).requestFocus();
	}
	
	private void saveGame() {
		scene.saveGame();
		pauseMenu.getChildren().remove(savedPane);
		pauseMenu.getChildren().add(savedPane);
	}

}
