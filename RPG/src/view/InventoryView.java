package view;

import javafx.geometry.Insets;
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
	
	private final static int GRIDWIDTH = 10;
	private final static int GRIDHEIGHT = 6;
	
	private MainScene scene;
	private GridPane inventorySlots;
	
	private Button close;
	
	private int newX;
	private int newY;
		
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
		
		
		int rectSize = 64;
		int rectStrokeWidth = 5;
		int gapSize = 10;
		
		int insets = 30;
		int width = GRIDWIDTH * rectSize + (GRIDWIDTH - 1) * gapSize + GRIDWIDTH * rectStrokeWidth + insets * 2;
		int height = GRIDHEIGHT * rectSize + (GRIDHEIGHT - 1) * gapSize + GRIDHEIGHT * rectStrokeWidth + insets * 2;
		
		inventorySlots.setMinSize(width, height);
		inventorySlots.setMaxSize(width, height);
		inventorySlots.setPadding(new Insets(insets));
		
		for(int x = 0; x < GRIDWIDTH; x++) {
			for(int y = 0; y < GRIDHEIGHT; y++) {
				Rectangle rect = new Rectangle(rectSize, rectSize);
				rect.setFill(Color.TRANSPARENT);
				rect.setStroke(Color.GRAY);
				rect.setStrokeWidth(rectStrokeWidth);
				inventorySlots.add(rect, x, y);
			}
		}
		
		inventorySlots.setHgap(gapSize);
		inventorySlots.setVgap(gapSize);
		inventorySlots.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
		inventorySlots.setBorder(new Border(new BorderStroke(Color.LIGHTSLATEGRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(rectStrokeWidth))));
		
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
	
	public void addItemView(ItemView itemView) {
		if(newX >= GRIDWIDTH) {
			newX = 0;
			newY++;
		}
		
		inventorySlots.add(itemView, newX, newY);
		newX++;
		
	}
	
	public void removeItemView(ItemView itemView) {
		inventorySlots.getChildren().remove(itemView);
	}
	
}
