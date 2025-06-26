package view;

import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class DialogView extends BorderPane {

	protected MainScene scene;
	protected BorderPane dialogPane;
	
	protected String dialogText;
	
	public DialogView(String dialogText, MainScene scene) {
		this.dialogText = dialogText;
		this.scene = scene;
		
		setUpLayout();
		setOnKeyPressed(e -> handleButtonKeyPressed(e));
		setOnMouseClicked(e -> scene.removeDialog(this));
	}
	
	protected void setUpLayout() {
		
		int width = 500;
		int height = 300;
		
		dialogPane = new BorderPane();
		dialogPane.setMinSize(width, height);
		dialogPane.setMaxSize(width, height);
		
		HBox textBox = this.getTextBox();
		
		dialogPane.setCenter(textBox);
		
		setCenter(dialogPane);
		BorderPane.setAlignment(dialogPane, Pos.BOTTOM_CENTER);
		
		setInvisableRectangleBottom();
		
	}
	
	protected void setInvisableRectangleBottom() {
		Rectangle rect = new Rectangle();
		
		rect.setFill(Color.TRANSPARENT);
		rect.setHeight(scene.getHeight()/10);
		
		setBottom(rect);
	}
	
	protected HBox getTextBox() {
		
		int width = 500;
		
		HBox textBox = new HBox();
		textBox.getStyleClass().add("dialog");
		textBox.setAlignment(Pos.CENTER);
		
		Text text = getText(dialogText);
		text.setWrappingWidth(width - width/10);
		
		textBox.getChildren().add(text);
		
		return textBox;
	}
	
	protected Text getText(String string) {
		Text text = new Text(string);
		text.getStyleClass().add("dialog-text");
		text.setTextAlignment(TextAlignment.CENTER);
		
		return text;
	}
	
	protected void handleButtonKeyPressed(KeyEvent e) {
		if(e.getCode().equals(KeyCode.ENTER)) {
			scene.removeDialog(this);
		}
	}
	
}
