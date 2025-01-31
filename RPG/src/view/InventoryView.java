package view;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class InventoryView extends BorderPane {

	private MainScene scene;
	private GridPane inventorySlots;
	
	private Button close;
	
	public InventoryView(MainScene scene) {
		this.scene = scene;
		setUpLayout();
	}
	
	private void setUpLayout() {
		inventorySlots = new GridPane();
		setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, null, null)));
		
		BorderPane leftPane = getLeftPane();
		setLeft(leftPane);
		
		BorderPane rightPane = getRightPane();
		setRight(rightPane);
		
		setUpInventorySlots();
		setCenter(inventorySlots);
	}
	
	private BorderPane getLeftPane() {
		int width = 300;
		
		BorderPane leftPane = new BorderPane();
		leftPane.setMinWidth(width);
		leftPane.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, null, null)));
		
		close = getButton("Close");
		close.setOnAction(e -> scene.removeInventoryView());
		leftPane.setCenter(close);
		
		return leftPane;
	}
	
	private BorderPane getRightPane() {
		int width = 300;
		
		BorderPane rightPane = new BorderPane();
		rightPane.setMinWidth(width);
		rightPane.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, null, null)));
		
		return rightPane;
	}
	
	private void setUpInventorySlots() {
		
		
		int gridWidth = 10;
		int gridHeight = 6;
		int rectSize = 64;
		int rectStrokeWidth = 5;
		int gapSize = 10;
		
		int width = gridWidth * rectSize + gridWidth * gapSize + gridWidth * rectStrokeWidth;
		int height = gridHeight * rectSize + gridHeight * gapSize + gridHeight * rectStrokeWidth;
		
		inventorySlots.setMinSize(width, height);
		inventorySlots.setMaxSize(width, height);
		
		for(int x = 0; x < gridWidth; x++) {
			for(int y = 0; y < gridHeight; y++) {
				Rectangle rect = new Rectangle(rectSize, rectSize);
				rect.setFill(Color.TRANSPARENT);
				rect.setStroke(Color.GRAY);
				rect.setStrokeWidth(rectStrokeWidth);
				inventorySlots.add(rect, x, y);
			}
		}
		
//		inventorySlots.setGridLinesVisible(true);
		inventorySlots.setHgap(gapSize);
		inventorySlots.setVgap(gapSize);
		inventorySlots.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
		
		BorderPane.setAlignment(inventorySlots, Pos.CENTER);
	}
	
	private Button getButton(String text) {
		
		int fontSize = 30;
		int buttonWidth = 150;
		int buttonHeight = buttonWidth/2;
		int buttonBorderWidth = 3;
		
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
	
	private void handleButtonKeyPressed(KeyEvent e, Button button) {
		if(e.getCode().equals(KeyCode.ENTER) && button.isFocused()) {
			button.fire();
		}
	}
	
	public void requestFocusForButton() {
		close.requestFocus();
	}
	
}
