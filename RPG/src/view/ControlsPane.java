package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class ControlsPane extends BorderPane {

	private PauseMenuView pauseMenuView;
	
	public ControlsPane(PauseMenuView pauseMenuView) {
		this.pauseMenuView = pauseMenuView;
		
		setUpLayout();
	}
	
	private void setUpLayout() {
		int margin = 10;
		
		VBox titleBox = getTitleBox();
		ScrollPane textPane = getTextPane();
		HBox buttonBox = getButtonBox();
		
		setMargin(titleBox, new Insets(0, 0, margin, 0));
		setMargin(textPane, new Insets(margin * 1.5, 0, margin * 1.5, 0));
		setMargin(buttonBox, new Insets(margin, 0, 0, 0));

		setTop(titleBox);
		setCenter(textPane);
		setBottom(buttonBox);
	}
	
	private VBox getTitleBox() {
		int indents = 80;
		double lineWidth = Screen.getPrimary().getBounds().getWidth() - indents;
		
		VBox titleBox = new VBox();
		Label title = new Label("Controls");
		title.getStyleClass().add("controls-title");
		title.setMinWidth(lineWidth);
		
		titleBox.setAlignment(Pos.CENTER);
		titleBox.getChildren().addAll(title);
		
		return titleBox;
	}
	
	private HBox getButtonBox() {
		int height = 150;
		
		HBox buttonBox = new HBox();
		Button button = pauseMenuView.getButton("Go Back");
		button.setOnAction(e -> pauseMenuView.goBack());
		
		buttonBox.setAlignment(Pos.TOP_CENTER);
		buttonBox.getChildren().add(button);
		buttonBox.setMinHeight(height);
		
		return buttonBox;
	}
	
	private ScrollPane getTextPane() {
		
		int textSpacing = 20;
		int margin = 40;
		double paneWidth = Screen.getPrimary().getBounds().getWidth() * 0.7;
		
		ScrollPane textPane = new ScrollPane();
		textPane.getStyleClass().add("controls-scroll-pane");
		textPane.setFitToWidth(true);
		textPane.setFitToHeight(true);
		textPane.setFocusTraversable(true);
		textPane.setMaxWidth(paneWidth);
		
		VBox menusTexts = getMenusTexts(textSpacing);
		VBox interactionTexts = getInteractionTexts(textSpacing);
		VBox movementTexts = getMovementTexts(textSpacing);
		
		VBox textVBoxes = new VBox();
		textVBoxes.setAlignment(Pos.CENTER);
		textVBoxes.getChildren().addAll(menusTexts, interactionTexts, movementTexts);
		VBox.setMargin(menusTexts, new Insets(0, 0, margin, 0));
		VBox.setMargin(interactionTexts, new Insets(0, 0, margin, 0));
		
		textPane.setContent(textVBoxes);
		
		return textPane;
	}
	
	private VBox getMenusTexts(int spacing) {
		VBox menusTexts = new VBox(spacing);
		menusTexts.setAlignment(Pos.CENTER_LEFT);
		
		Text interactionSubtitle = getSubTitle("Menus");
		BorderPane textEsc = getTextRow("Esc", "Toggle Pause Menu");
		BorderPane textI = getTextRow("I", "Toggle In Game Menu");
		
		menusTexts.getChildren().addAll(interactionSubtitle, textEsc, textI);
		
		return menusTexts;
	}
	
	private VBox getInteractionTexts(int spacing) {
		VBox interactionTexts = new VBox(spacing);
		interactionTexts.setAlignment(Pos.CENTER_LEFT);
		
		Text interactionSubtitle = getSubTitle("Interaction");
		BorderPane textE = getTextRow("E", "Interact");
		BorderPane textQ = getTextRow("Q", "Drop Selected Item");
		BorderPane textAlt = getTextRow("Alt", "Toggle Button Focus (Only in some views)");
		
		interactionTexts.getChildren().addAll(interactionSubtitle, textE, textQ, textAlt);
		
		return interactionTexts;
	}
	
	private VBox getMovementTexts(int spacing) {
		VBox movementTexts = new VBox(spacing);
		movementTexts.setAlignment(Pos.CENTER_LEFT);
		
		Text movementSubtitle = getSubTitle("Movement");
		BorderPane textUp = getTextRow("W / ↑", "Walk Up");
		BorderPane textLeft = getTextRow("A / ←", "Walk To Left");
		BorderPane textDown = getTextRow("S / ↓", "Walk Down");
		BorderPane textRight = getTextRow("D / →", "Walk To Right");
		
		movementTexts.getChildren().addAll(movementSubtitle, textUp, textLeft, textDown, textRight);
		
		return movementTexts;
	}
	
	private BorderPane getTextRow(String leftString,String rightString) {		
		Text leftText = getText(leftString);
		Text rightText = getText(rightString);
		
		BorderPane textRow = new BorderPane();
		textRow.setLeft(leftText);
		textRow.setRight(rightText);
		textRow.getStyleClass().add("controls-text-row");		
		
		return textRow;
	}
	
	private Text getText(String string) {
		Text text = new Text(string);
		text.getStyleClass().add("controls-text");		
		
		return text;
	}
	
	private Text getSubTitle(String string) {
		Text text = new Text(string);
		text.getStyleClass().add("controls-subtitle");	
		
		return text;
	}
	
}
