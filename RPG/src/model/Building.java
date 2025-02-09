package model;

public class Building {

	private Location location;
	private boolean canPass;
	private int width;
	private int height;
	
	public Building(Location location, boolean canPass, int width, int height) {
		this.location = location;
		this.canPass = canPass;
		this.width = width;
		this.height = height;
	}
	
	public void enter() {
		if(canPass) {
			System.out.println("Entered");
		}
	}
	
	public void leave() {
		if(canPass) {
			System.out.println("Left");
		}
	}
	
	public void setCanPass(boolean canPass) {
		this.canPass = canPass;
	}
	
	public boolean canPass() {
		return canPass;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getX() {
		return location.getX();
	}
	
	public int getY() {
		return location.getY();
	}
	
}
