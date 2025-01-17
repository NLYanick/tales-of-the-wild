package View;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class MenuView extends BorderPane {
	
	private BorderPane menu;
	
	private VBox buttonsPane;
	private BorderPane controllsPane;
	
	public MenuView() {
		setUpLayout();
	}
	
	private void setUpLayout() {
		
		Color color = new Color(0, 0, 0, 0.5);
		
		menu = new BorderPane();
		menu.setBackground(new Background(new BackgroundFill(color, null, null)));
		
		buttonsPane = createButtonsVBox();
		controllsPane = createControllsPane();
		
		menu.setCenter(buttonsPane);
		
		setCenter(menu);
		
	}
	
	private Button getButton(String text) {
		
		int buttonWidth = 120;
		int buttonHeight= buttonWidth / 2;
		int fontSize = 18;
		int borderWidth = 3;
		
		Button button = new Button(text);
		button.setPrefSize(buttonWidth, buttonHeight);
		
		button.setFont(Font.font("Times New Roman", fontSize));
		button.setTextFill(Color.WHITE);
		button.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		button.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(borderWidth))));
		button.setCursor(Cursor.HAND);
		
		return button;
	}
	
	private VBox createButtonsVBox() {
		
		int spacing = 20;
		
		VBox buttonsPane = new VBox();
		
		Button exitButton = getButton("Exit");
		exitButton.setOnMouseClicked(e -> Platform.exit());
		
		Button controllsButton = getButton("Controlls");
		controllsButton.setOnMouseClicked(e -> openControlls());
		
		buttonsPane.getChildren().addAll(exitButton, controllsButton);
		
		buttonsPane.setAlignment(Pos.CENTER);
		buttonsPane.setSpacing(spacing);
		
		return buttonsPane;
	}
	
	private BorderPane createControllsPane() {
		
		BorderPane controllsPane = new BorderPane();
		
		Label title = new Label("Controlls");
		title.setFont(Font.font("Times New Roman", 36));
		title.setTextFill(Color.WHITE);
		
		HBox textVBoxes = getTextVBoxes();
		
		Button button = getButton("Go Back");
		button.setOnMouseClicked(e -> goBack());
		
		controllsPane.setTop(title);
		controllsPane.setCenter(textVBoxes);
		controllsPane.setLeft(button);
		
		return controllsPane;
	}
	
	private HBox getTextVBoxes() {
		HBox textVBoxes = new HBox();
		textVBoxes.setAlignment(Pos.CENTER);
		
		VBox textBoxOne = new VBox();
		textBoxOne.setAlignment(Pos.CENTER);
		
		Text textE = getText("E - Interact");
		
		textBoxOne.getChildren().addAll(textE);
		
		VBox textBoxTwo = new VBox();
		textBoxTwo.setAlignment(Pos.CENTER);
		
		Text textUp = getText("Arrow Up - Walk Up");
		Text textLeft = getText("Arrow Left - Walk To Left");
		Text textDown = getText("Arrow Down - Walk Down");
		Text textRight = getText("Arrow Right - Walk To Right");
		
		textBoxTwo.getChildren().addAll(textUp, textLeft, textDown, textRight);
		
		textVBoxes.getChildren().addAll(textBoxOne, textBoxTwo);
		
		return textVBoxes;
	}
	
	private Text getText(String string) {
		
		Text text = new Text(string);
		
		text.setFont(Font.font("Times New Roman", 18));
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFill(Color.WHITE);
		
		return text;
	}
	
	private void openControlls() {
		menu.setCenter(controllsPane);
	}
	
	private void goBack() {
		menu.setCenter(buttonsPane);
	}

	public void resetView() {
		menu.setCenter(buttonsPane);
	}
	
}
