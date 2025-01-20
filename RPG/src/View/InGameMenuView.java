package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class InGameMenuView extends BorderPane {

	private GridPane menuPane;
	private BorderPane buttonsMenu;
	private VBox buttonsBox;
	
	private Button[] buttons;
	
	private ImageView arrowView;
	
	private int imageDifference = 4;
	private int buttonCounter = 0;
	
	public InGameMenuView() {
		setUpLayout();
		setOnKeyPressed(e -> handleKeyInput(e));
	}

	private void setUpLayout() {
		menuPane = new GridPane();
		
		buttonsMenu = GetFirstMenu();
		fillButtonsArray();
		
		fillGrid();
		menuPane.add(buttonsMenu, 1, 1);
		
		setCenter(menuPane);
		
	}
	
	private void fillGrid() {
		for(int x = 0; x < 2; x++) {
			for(int y = 0; y < 2; y++) {
				Rectangle invisableRect = new Rectangle(1920/4, 1080/4);
				invisableRect.setFill(Color.TRANSPARENT);
				if(!(x == 1 && y == 1)) {
					menuPane.add(invisableRect, x, y);
				}
			}
		}
	}
	
	private void fillButtonsArray() {
		buttons = new Button[buttonsBox.getChildren().size()];
		
		int i = 0;
		for(Node node : buttonsBox.getChildren()) {
			buttons[i] = (Button) node;
			i++;
		}
	}
	
	private ImageView getArrow() {
		ImageView arrowView = new ImageView(new Image("Images/SelectArrow.png"));
		
		int x = 410;
		int y = 250 + imageDifference;
		
		arrowView.setLayoutX(x);
		arrowView.setLayoutY(y);
		return arrowView;
	}

	private BorderPane GetFirstMenu() {
		
		int borderWidth = 10;
		int width = 400;
		int height = 500;
		
		BorderPane buttonsMenu = new BorderPane();
		
		buttonsMenu.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		buttonsMenu.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(borderWidth))));
		buttonsMenu.setMinSize(width, height);
		buttonsMenu.setMaxSize(width, height);
		
		VBox buttonsBox = getButtonsBox();
		
		buttonsMenu.setCenter(buttonsBox);
		
		arrowView = getArrow();
		buttonsMenu.getChildren().add(arrowView);
		
		return buttonsMenu;
	}
	
	private VBox getButtonsBox() {
		
		int spacing = 20;
		
		buttonsBox = new VBox(spacing);
		buttonsBox.setAlignment(Pos.CENTER);
		
		Button inventoryButton = getButton("Inventory");
		inventoryButton.setOnAction(e -> openInventory());
		Button tempButtonOne = getButton("Temp Text");
		tempButtonOne.setOnAction(e -> System.out.println("1 1 1 1"));
		Button tempButtonTwo = getButton("Temp Text");
		tempButtonTwo.setOnAction(e -> System.out.println("2 2 2 2"));
		Button tempButtonThree = getButton("Temp Text");
		tempButtonThree.setOnAction(e -> System.out.println("3 3 3 3"));
		
		buttonsBox.getChildren().addAll(inventoryButton, tempButtonOne, tempButtonTwo, tempButtonThree);
		
		return buttonsBox;
	}
	
	private Button getButton(String text) {
		
		int fontSize = 50;
		
		Button button = new Button(text);
		button.setPadding(new Insets(0));
		
		button.setFont(Font.font("Times New Roman", fontSize));
		button.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		button.setTextFill(Color.WHITE);
			
		button.setOnKeyPressed(e -> handleButtonKeyPressed(e, button));
		
		return button;
	}
	
	private void handleKeyInput(KeyEvent e) {
		switch(e.getCode()) {
		case UP:
			moveArrow("Up", e);
			break;
		case DOWN:
			moveArrow("Down", e);
			break;
			default: System.out.println("Input not valid");
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
		
		int toRight = 10;
		
		buttonsMenu.getChildren().remove(arrowView);
		for(Button button : buttons) {
			if(button.isFocused()) {
				arrowView.setLayoutX(buttonsBox.getLayoutX() + toRight);
				arrowView.setLayoutY(button.getLayoutY() + imageDifference);
				buttonsMenu.getChildren().add(arrowView);
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
	
	public void resetView() {
		resetArrow();
		buttonsMenu.setCenter(buttonsBox);
	}
	
	public void resetArrow() {
		if(!buttonsMenu.getChildren().contains(arrowView)) {
			buttonsMenu.getChildren().add(arrowView);
		}
		int toRight = 10;
		arrowView.setLayoutX(buttonsBox.getLayoutX() + toRight);
		arrowView.setLayoutY(buttons[0].getLayoutY() + imageDifference);
	}
	
	private void openInventory() {
		System.out.println("Open Inventory");
	}
	
}
