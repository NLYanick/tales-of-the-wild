package view;

public class OptionDialogView extends DialogView {

	private int optionChosen;
	
	public OptionDialogView(String dialogText, MainScene scene, int optionChosen) {
		super(dialogText, scene);
		
		this.optionChosen = optionChosen;
	}

	public int getOptionChosen() {
		return optionChosen;
	}

}
