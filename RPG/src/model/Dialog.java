package model;

import java.util.HashMap;
import java.util.Map.Entry;

public class Dialog {
	
	private String text;
	private boolean skip;
	private int itemId;
	private boolean interactive;
	private HashMap<Integer, String> options;
	private int optionChosen;
	
	private boolean hasPlayed;
	
	public Dialog(String text, boolean skip, int itemId, boolean interactive, HashMap<Integer, String> options, int optionChosen) {
		this.text = text;
		this.skip = skip;
		this.itemId = itemId;
		this.interactive = interactive;
		this.options = options;
		this.optionChosen = optionChosen;
	}
	
	public String toJSON() {
		String itemText = "";
		String optionChosenText = "";
		
		if(itemId > 0) itemText = ", \"item\": " + itemId;
		if(optionChosen > 0) optionChosenText = ", \"option-chosen\": " + optionChosen;
		
		return "\"text\": \"" + text + "\", \"skip\": " + skip + ", \"interactive\": " + interactive + getOptionsString()
				+ itemText + optionChosenText + ", \"played\": " + hasPlayed;
	}
	
	private String getOptionsString() {
		if(options.size() > 0) {
			String optionsString = ", \"options\": [ ";
			for (Entry<Integer, String> entry : options.entrySet()) {
				String text = entry.getValue();
				optionsString += "\"" + text + "\", ";
			}
			optionsString += " ]";
			
			return optionsString;
		}
		return "";
	}
	
	
	public String getText() {
		return text;
	}
	
	public boolean shouldSkip() {
		return skip;
	}
	
	public int getItemId() {
		return itemId;
	}

	public boolean hasPlayed() {
		return hasPlayed;
	}

	public void setHasPlayed(boolean hasPlayed) {
		this.hasPlayed = hasPlayed;
	}

	public boolean isInteractive() {
		return interactive;
	}
	
	public HashMap<Integer, String> getOptions() {
		return options;
	}
	
	public int getOptionChosen() {
		return optionChosen;
	}
	
}
