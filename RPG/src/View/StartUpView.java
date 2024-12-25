package View;

import Controller.ApplicationController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StartUpView extends BorderPane {
	
	private MainScene scene;
	
	private VBox layout;
	
	public StartUpView(MainScene scene) {
		this.scene = scene;
		
		setUpStarterView();
	}

	private void setUpStarterView() {
		
		setBackground(new Background(new BackgroundImage(new Image("Images/Background/Grass/Grass.png"), null, null, null, null)));
		
		setMinSize(MainScene.SCENEWIDTH, MainScene.SCENEHEIGHT);
		
		setUpLayout();
		setUpTopText();
		setUpImage();
		setUpButton();
		
		setCenter(layout);
		
	}
	
	private void setUpLayout() {
		
		int spacing = 50;
		
		layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(spacing);
	}
	
	private void setUpTopText() {
		
		int fontSize = 60;
		
		Label welcomeText = new Label(ApplicationController.APPLICATIONNAME);
		welcomeText.setFont(Font.font("Times New Roman", FontWeight.BOLD, fontSize));
		welcomeText.setTextFill(Color.WHITE);
		
		welcomeText.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(10))));
		welcomeText.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, null, null)));
		welcomeText.setPadding(new Insets(50));
		
		HBox topPane = new HBox();
		topPane.getChildren().add(welcomeText);
		topPane.setAlignment(Pos.CENTER);
		
		layout.getChildren().add(topPane);
	}
	
	private void setUpButton() {
		
		int buttonWidth = 200;
		int buttonHeight= buttonWidth / 2;
		
		int fontSize = 30;
		
		Button button = new Button("Continue");
		button.setPrefSize(buttonWidth, buttonHeight);
		
		button.setFont(Font.font("Times New Roman", fontSize));
		button.setTextFill(Color.WHITE);
		button.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, null, null)));
		button.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
		button.setCursor(Cursor.HAND);
		
		button.setOnAction(e -> scene.loadBackground());
		
		layout.getChildren().add(button);
	}
	
	private void setUpImage() {
		Image foxImage = new Image("Images/Fox/FoxStandingStill.gif");
        ImageView foxImageView = new ImageView(foxImage);
        
        layout.getChildren().add(foxImageView);
	}
}
