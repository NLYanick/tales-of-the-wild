package model;

public class Dialog {
	
	private String text;
	private boolean skip;
	private int itemId;
	
	private boolean hasPlayed;
	
	public Dialog(String text, boolean skip, int itemId) {
		this.text = text;
		this.skip = skip;
		this.itemId = itemId;
	}
	
	public String toJSON() {
		if(itemId > 0) {			
			return "\"text\": \"" + text + "\", \"skip\": " + skip + ", \"item\": " + itemId + ", \"played\": " + hasPlayed;
		} else {			
			return "\"text\": \"" + text + "\", \"skip\": " + skip + ", \"played\": " + hasPlayed;
		}
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
	
}
