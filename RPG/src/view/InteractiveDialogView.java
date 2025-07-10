package view;

import java.util.HashMap;
import java.util.Map.Entry;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class InteractiveDialogView extends DialogView {
	
	private HashMap<Integer, String> options;
	
	private VBox optionPanes;

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
			
			BorderPane optionPane = new BorderPane();
			optionPane.getStyleClass().add("interactive-dialog");
			optionPane.setOnKeyPressed(e -> handleButtonKeyPressed(e, number));
			optionPane.setOnMouseClicked(e -> next(number));
			optionPane.setFocusTraversable(true);
			
			Label optionLabel = new Label(text);
			optionLabel.getStyleClass().add("dialog-text");
			
			optionPane.setCenter(optionLabel);
			optionPanes.getChildren().add(optionPane);
		}
	}
	
	public void addOptions() {
		setRight(optionPanes);
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
