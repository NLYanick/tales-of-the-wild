package view;

import java.util.HashMap;
import java.util.Map.Entry;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class InteractiveDialogView extends DialogView {
	
	private HashMap<Integer, String> options;
	
	private VBox optionPanes;
	private HBox textAndOptions;
	
	private final int panesSpacing = 20;

	public InteractiveDialogView(String dialogText, MainScene scene, HashMap<Integer, String> options) {
		super(dialogText, scene);
		
		this.options = options;
		
		setOnKeyPressed(null);
		setOnMouseClicked(null);
		
		setUpInteraction();
	}
	
	private void setUpInteraction() {
		int spacing = 20;
		
		optionPanes = new VBox();
		optionPanes.setSpacing(spacing);
		optionPanes.setAlignment(Pos.CENTER);
		
		for (Entry<Integer, String> entry : options.entrySet()) {
			int number = entry.getKey();
			String text = entry.getValue();
			
			BorderPane optionPane = getOption(number);
			
			Text optionText = new Text(text);
			optionText.getStyleClass().add("option-text");
			optionText.wrappingWidthProperty().bind(optionPane.widthProperty().subtract(25));
			
			optionPane.setCenter(optionText);
			optionPanes.getChildren().add(optionPane);
		}
	}
	
	private BorderPane getOption(int number) {
		
		BorderPane optionPane = new BorderPane();
		optionPane.getStyleClass().add("option-pane");
		optionPane.setOnKeyPressed(e -> handleButtonKeyPressed(e, number));
		optionPane.setOnMouseClicked(e -> next(number));
		optionPane.setFocusTraversable(true);
		
		return optionPane;
	}
		
	public void addOptions() {	
		int margin = 200;
		
		textAndOptions = new HBox(textBox, optionPanes);
		textAndOptions.setAlignment(Pos.CENTER);
		textAndOptions.setSpacing(panesSpacing);
		
		dialogPane.setCenter(textAndOptions);
		BorderPane.setMargin(textAndOptions, new Insets(0, 0, 0, margin + panesSpacing));
		
		optionPanes.getChildren().get(0).requestFocus();
	}
	
	private void choose(int number) {
		scene.removeOptionDialogs(number);
	}
	
	private void next(int number) {
		choose(number);
		scene.removeDialog(this);
	}
	
	private void handleButtonKeyPressed(KeyEvent e, int number) {
		if(e.getCode().equals(KeyCode.ENTER)) {
			next(number);
		}
	}

}
