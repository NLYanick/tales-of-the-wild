package view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ItemDialogView extends DialogView {

	public ItemDialogView(String dialogText, MainScene scene) {
		super(dialogText, scene);
	}
	
	@Override
	protected HBox getTextBox() {
		
		int width = 500;
		
		HBox textBox = new HBox();
		textBox.getStyleClass().add("item-dialog");
		textBox.setAlignment(Pos.CENTER);
		
		Text text = getText(dialogText);
		text.setWrappingWidth(width - width/10);
		
		textBox.getChildren().add(text);
		
		return textBox;
	}

}
