package model;

import java.util.ArrayList;

public class Inventory {

	private final static int SIZE = 60;
	
	private ArrayList<Item> items;
	private boolean isFull;
	
	public Inventory() {
		items = new ArrayList<Item>();
	}
	
	public void addItem(Item item) {
		items.add(item);
		if(items.size() >= SIZE) {
			isFull = true;
		}
	}
	
	public void removeItem(Item item) {
		items.remove(item);
		if(items.size() < SIZE) {
			isFull = false;
		} 
	}
	
	public boolean isFull() {
		return isFull;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}

}
