package model;

public class Dialog {
	
	private String text;
	private boolean skip;
	private int itemId;
	
	public Dialog(String text, boolean skip, int itemId) {
		this.text = text;
		this.skip = skip;
		this.itemId = itemId;
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
	
}
