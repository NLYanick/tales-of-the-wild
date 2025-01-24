package View;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
	
	private int buttonWidth = 150;
	private int buttonHeight = buttonWidth/2;
	private int buttonSpacing = 30;
	private int buttonBorderWidth = 3;
	
	private int imageDifference = 4;
	private int buttonCounter = 0;
	
	private Button[] buttons;
	
	private ImageView arrowView;
	
	public PauseMenuView(MainScene scene) {
		this.scene = scene;
		
		setUpLayout();
		setOnKeyPressed(e -> handleKeyInput(e));
	}
	
	private void setUpLayout() {
		
		Color color = new Color(0, 0, 0, 0.5);
		
		pauseMenu = new BorderPane();
		pauseMenu.setBackground(new Background(new BackgroundFill(color, null, null)));
		
		buttonsPane = createButtonsVBox();
		controlsPane = createControlsPane();
		
		buttons = new Button[buttonsPane.getChildren().size()];
		fillButtonsArray();
		pauseMenu.setCenter(buttonsPane);
		
		arrowView = getArrow();
		pauseMenu.getChildren().add(arrowView);
		
		setCenter(pauseMenu);
	}
	
	private void fillButtonsArray() {
		int i = 0;
		for(Node node : buttonsPane.getChildren()) {
			buttons[i] = (Button) node;
			i++;
		}
	}
	
	private ImageView getArrow() {
		ImageView arrowView = new ImageView(new Image("Images/SelectArrow.png"));
		
		int x = 810;
		int y = 450 + imageDifference;
		
		arrowView.setLayoutX(x);
		arrowView.setLayoutY(y);
		return arrowView;
	}
	
	private Button getButton(String text) {
		
		int fontSize = 24;
		
		Button button = new Button(text);
		button.setPrefSize(buttonWidth, buttonHeight);
		
		button.setFont(Font.font("Times New Roman", fontSize));
		button.setTextFill(Color.WHITE);
		button.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		button.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(buttonBorderWidth))));
		button.setCursor(Cursor.HAND);
		
		button.setOnKeyPressed(e -> handleButtonKeyPressed(e, button));
		
		return button;
	}
	
	private void exit() {
		scene.stopNPCThreads();
		Platform.exit();
	}

	private VBox createButtonsVBox() {
		
		VBox buttonsPane = new VBox();
		
		Button controlsButton = getButton("Controls");
		controlsButton.setOnAction(e -> openControls());
		
		Button exitButton = getButton("Exit Game");
		exitButton.setOnAction(e -> exit());
		
		buttonsPane.getChildren().addAll(controlsButton, exitButton);
		
		buttonsPane.setSpacing(buttonSpacing);
		buttonsPane.setAlignment(Pos.CENTER);
		
		return buttonsPane;
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
		
		textBoxOne.getChildren().addAll(textE, textEsc, textI);
		
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
		pauseMenu.setCenter(buttonsPane);
	}
	
	public void resetArrow() {
		if(!pauseMenu.getChildren().contains(arrowView)) {
			pauseMenu.getChildren().add(arrowView);
		}
		arrowView.setLayoutX(buttons[0].getLayoutX() - buttonWidth/2);
		arrowView.setLayoutY(buttons[0].getLayoutY() + imageDifference);
	}
	
	private void handleKeyInput(KeyEvent e) {
		switch(e.getCode()) {
		case UP:
			moveArrow("Up", e);
			break;
		case DOWN:
			moveArrow("Down", e);
			break;
			default: 
		}
	}
	
	private void handleButtonKeyPressed(KeyEvent e, Button button) {
		if(e.getCode().equals(KeyCode.ENTER) && button.isFocused()) {
			button.fire();
		}
	}
	
	private void moveArrow(String direction, KeyEvent event) {
		
		event.consume();
		setButtonFocus(direction);
		
		pauseMenu.getChildren().remove(arrowView);
		for(Button button : buttons) {
			if(button.isFocused()) {
				arrowView.setLayoutX((button.getLayoutX() - buttonWidth/2));
				arrowView.setLayoutY(button.getLayoutY() + imageDifference);
				pauseMenu.getChildren().add(arrowView);
				break;
			}
		}
	}
	
	private void setButtonFocus(String direction) {
		if(direction.toLowerCase().equals("up")) {
			buttonCounter--;
			if(buttonCounter < 0) {
				buttonCounter = 0;
			}
		} else if(direction.toLowerCase().equals("down")) {
			buttonCounter++;
			if(buttonCounter >= buttons.length) {
				buttonCounter = buttons.length - 1;
			}
		}
		buttons[buttonCounter].requestFocus();
	}

	public void requestFocusForButtons() {
		buttons[0].requestFocus();
		buttonCounter = 0;
	}

}
