package View;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class MenuView extends BorderPane {
	
	private BorderPane menu;
	
	private HBox arrowAndButtons;
	private BorderPane controlsPane;
	private VBox buttonsPane;
	
	private Polygon arrow;
	
	private int buttonWidth = 150;
	private int buttonHeight = buttonWidth/2;
	private int buttonSpacing = 30;
	private int buttonBorderWidth = 3;
	
	private int buttonCounter = 0;
	
	private ArrayList<Button> buttons;
	
	public MenuView() {
		buttons = new ArrayList<Button>();
		
		setUpLayout();
		setOnKeyPressed(e -> handleKeyInput(e));
	}
	
	private void setUpLayout() {
		
		Color color = new Color(0, 0, 0, 0.5);
		
		menu = new BorderPane();
		menu.setBackground(new Background(new BackgroundFill(color, null, null)));
		
		arrowAndButtons = createButtonsVBox();
		controlsPane = createControlsPane();
		
		menu.setCenter(arrowAndButtons);
		
		setCenter(menu);
		
	}
	
	private Button getButton(String text) {
		
		int buttonWidth = 150;
		int fontSize = 24;
		
		Button button = new Button(text);
		button.setPrefSize(buttonWidth, buttonHeight);
		
		button.setFont(Font.font("Times New Roman", fontSize));
		button.setTextFill(Color.WHITE);
		button.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		button.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(buttonBorderWidth))));
		button.setCursor(Cursor.HAND);
		
		return button;
	}
	
	private HBox createButtonsVBox() {
		
		int spacing = 40;
		
		HBox arrowAndButtons = new HBox();
		
		arrow = new Polygon(0, 0, 30, 30, 0, 60);
		arrow.setFill(Color.WHITE);
		arrow.setTranslateY(-54);
		
		arrowAndButtons.getChildren().add(arrow);
		
		buttonsPane = new VBox();
		
		Button controlsButton = getButton("Controls");
		controlsButton.setOnMouseClicked(e -> openControls());
		buttons.add(controlsButton);
		
		Button exitButton = getButton("Exit Game");
		exitButton.setOnMouseClicked(e -> Platform.exit());
		buttons.add(exitButton);
		
		buttonsPane.getChildren().addAll(controlsButton, exitButton);
		
		buttonsPane.setSpacing(buttonSpacing);
		buttonsPane.setAlignment(Pos.CENTER);
		
		arrowAndButtons.setAlignment(Pos.CENTER);
		arrowAndButtons.setSpacing(spacing);
		arrowAndButtons.setPadding(new Insets(0, 30 + spacing, 0, 0));
		arrowAndButtons.getChildren().add(buttonsPane);
		
		return arrowAndButtons;
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
		button.setOnMouseClicked(e -> goBack());
		
		buttonBox.setAlignment(Pos.TOP_CENTER);
		buttonBox.getChildren().add(button);
		buttonBox.setMinHeight(height);
		
		return buttonBox;
	}
	
	private HBox getTextVBoxes() {
		HBox textVBoxes = new HBox();
		textVBoxes.setAlignment(Pos.CENTER);
		
		VBox textBoxOne = new VBox();
		textBoxOne.setAlignment(Pos.CENTER);
		
		Text textE = getText("E - Interact");
		
		textBoxOne.getChildren().addAll(textE);
		
		VBox textBoxTwo = new VBox(20);
		textBoxTwo.setAlignment(Pos.CENTER_LEFT);
		
		Text textUp = getText("Arrow Up - Walk Up");
		Text textLeft = getText("Arrow Left - Walk To Left");
		Text textDown = getText("Arrow Down - Walk Down");
		Text textRight = getText("Arrow Right - Walk To Right");
		
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
		menu.setCenter(controlsPane);
	}
	
	private void goBack() {
		menu.setCenter(arrowAndButtons);
	}

	public void resetView() {
		menu.setCenter(arrowAndButtons);
	}
	
	private void handleKeyInput(KeyEvent e) {
		switch(e.getCode()) {
		case UP:
			moveArrow("Up");
			break;
		case DOWN:
			moveArrow("Down");
			break;
		case ENTER:
//			checkForButton();
			break;
			default: System.out.println("Input not valid");
		}
	}
	
	private void moveArrow(String direction) {
		
		if(direction.toLowerCase().equals("up")) {
			buttonCounter--;
			if(buttonCounter < 0) {
				buttonCounter = 0;
			}
			buttonsPane.getChildren().get(buttonCounter).requestFocus();
		} else if(direction.toLowerCase().equals("down")) {
			buttonCounter++;
			if(buttonCounter >= buttonsPane.getChildren().size()) {
				buttonCounter = buttonsPane.getChildren().size() - 1;
			}
			buttonsPane.getChildren().get(buttonCounter).requestFocus();
		}
		
		int halfAButtonUp = buttonHeight/2 + buttonBorderWidth + buttonSpacing/2;
		
		arrowAndButtons.getChildren().clear();
		for(Node node : buttonsPane.getChildren()) {
			if(node.isFocused()) {
				arrow.setTranslateY(node.getLayoutY() - buttonsPane.getChildren().get(0).getLayoutY() - halfAButtonUp);
				arrowAndButtons.getChildren().add(arrow);
				break;
			}
		}
		arrowAndButtons.getChildren().add(buttonsPane);
	}

	public void requestFocusForButtons() {
		buttonsPane.getChildren().get(0).requestFocus();
	}
	
}
