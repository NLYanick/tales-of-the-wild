package View;

import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class DialogView extends BorderPane {

	private MainScene scene;
	private BorderPane dialogPane;
	
	private String dialogText;
	
	public DialogView(String dialogText, MainScene scene) {
		this.dialogText = dialogText;
		this.scene = scene;
		
		setUpLayout();
		setOnMouseClicked(e -> scene.removeDialog(this));
	}
	
	private void setUpLayout() {
		dialogPane = new BorderPane();
		
		HBox textBox = getTextBox();
		
		dialogPane.setCenter(textBox);
		
		setBottom(dialogPane);
		BorderPane.setAlignment(textBox, Pos.CENTER);
		
	}
	
	private HBox getTextBox() {
		
		int textBorderWidth = 3;
		
		HBox textBox = new HBox();
		textBox.setAlignment(Pos.CENTER);
		textBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		textBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(textBorderWidth))));
		
		Text text = getText(dialogText);
		
		textBox.getChildren().add(text);
		
		return textBox;
	}
	
private Text getText(String string) {
		
		int fontSize = 28;
		
		Text text = new Text(string);
		
		text.setFont(Font.font("Times New Roman", fontSize));
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFill(Color.BLACK);
		
		return text;
	}
	
}
