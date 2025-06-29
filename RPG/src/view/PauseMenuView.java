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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PauseMenuView extends BorderPane {
	
	private MainScene scene;
	private BorderPane pauseMenu;
	
	private ControlsPane controlsPane;
	private VBox buttonsPane;
	private BorderPane savedPane;
	
	private final int buttonWidth = 150;
	private final int buttonSpacing = 30;
	private final int buttonBorderWidth = 3;
	
	private final int imageDifference = 4;
		
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
		controlsPane = new ControlsPane(this);
		
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
	
	public Button getButton(String text) {
				
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
	
	public void goBack() {
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
