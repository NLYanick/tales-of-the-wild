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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class PauseMenuView extends BorderPane {
	
	private MainScene scene;
	private BorderPane pauseMenu;
	
	private ControlsPane controlsPane;
	private VBox buttonsPane;
	private BorderPane savedPane;
	
	private final int buttonSpacing = 30;
	private final int arrowMargin = 75;
	private final int imageDifference = 4;
		
	private ImageView arrowView;
	
	public PauseMenuView(MainScene scene) {
		this.scene = scene;
		
		setUpLayout();
	}
	
	private void setUpLayout() {
		
		pauseMenu = new BorderPane();
		pauseMenu.getStyleClass().add("pause-menu");
		
		arrowView = new ImageView(new Image("Images/SelectArrow.png"));
		pauseMenu.getChildren().add(arrowView);
		
		buttonsPane = createButtonsVBox();
		controlsPane = new ControlsPane(this);
		
		pauseMenu.setCenter(buttonsPane);
		
		setCenter(pauseMenu);
		
		setUpSavedPane();
	}
	
	private void setUpSavedPane() {

		String saved = "Saved";
		
		Label savedLabel = new Label(saved);
		savedLabel.getStyleClass().add("saved-text");
		
		savedPane = new BorderPane(savedLabel);
		savedPane.getStyleClass().add("saved-text-pane");
		
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
	 			arrowView.layoutXProperty().unbind();
	 			arrowView.layoutYProperty().unbind();
	 			
	 			arrowView.layoutXProperty().bind(button.layoutXProperty().subtract(arrowMargin));
	 			arrowView.layoutYProperty().bind(button.layoutYProperty().add(imageDifference));
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
