package view;

public class OptionDialogView extends DialogView {

	private int optionChosen;
	private String action;
	
	private boolean chosen = true;
	
	public OptionDialogView(String dialogText, MainScene scene, int optionChosen, String action) {
		super(dialogText, scene);
		
		this.optionChosen = optionChosen;
		this.action = action;
	}

	public int getOptionChosen() {
		return optionChosen;
	}
	
	public String getAction() {
		return action;
	}

	public boolean isChosen() {
		return chosen;
	}

	public void setChosen(boolean isChosen) {
		this.chosen = isChosen;
	}

}
