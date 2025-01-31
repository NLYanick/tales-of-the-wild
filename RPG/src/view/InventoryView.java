package view;

import javafx.scene.layout.BackgroundFill;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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
		
		close = getButton("Close");
		close.setOnAction(e -> scene.removeInventoryView());
		setLeft(close);
	}
	
	private Button getButton(String text) {
		
		int fontSize = 40;
		
		Button button = new Button(text);
		button.setPadding(new Insets(0));
		
		button.setFont(Font.font("Times New Roman", fontSize));
		button.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		button.setTextFill(Color.WHITE);
			
		button.setOnKeyPressed(e -> handleButtonKeyPressed(e, button));
		button.setCursor(Cursor.HAND);
		
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
