package view;

import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ItemDialogView extends DialogView {

	public ItemDialogView(String dialogText, MainScene scene) {
		super(dialogText, scene);
	}
	
	@Override
	protected HBox getTextBox() {
		
		int textBorderWidth = 10;
		int width = 500;
		int height = 300;
		
		HBox textBox = new HBox();
		textBox.setAlignment(Pos.CENTER);
		textBox.setBackground(new Background(new BackgroundFill(Color.GOLD, null, null)));
		textBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(textBorderWidth))));
		textBox.setMinSize(width, height);
		textBox.setMaxSize(width, height);
		
		Text text = getText(dialogText);
		text.setWrappingWidth(width - width/10);
		
		textBox.getChildren().add(text);
		
		return textBox;
	}

}
